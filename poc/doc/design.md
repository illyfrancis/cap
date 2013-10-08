# Graph DB

Need to be able to represent all possible scenarios with graph. In order to do so, first we need to establish all possible known use cases.

## Use cases

Capture the known use cases and describe how the graph should be modeled.

Broken up into two parts. One for graph creation when a new message (transaction, fx detail etc) is processed. Once created the second part handles `UTR` message creation based on the current graph for the ` transaction identifier `.

### 1. Graph creation

#### Process a new transaction

    Given a UTR transaction,
    When the transaction is unknown and has account of type b/c,
    Then

    CREATE p=(c:CLIENT { name: 'SEB'})<-[:CLIENT]-(u:UTR { ref:7890 })-[:ID_BY]->(t:TXID { ref:1234 })-[:RELATES_TO]->(tr:TRANSACTION {version: 1})-[:WITH_ACCOUNT]->(a:ACCOUNT { num:4567, type:'B' }) 
    RETURN p

#### Another transaction matching existing transaction id

    MATCH t:TXID
    WHERE t.ref = 1234
    RETURN t

And found it so, it must exists.

    MATCH t:TXID
    WHERE t.ref = 1234
    CREATE p=(tr:TRANSACTION {version: 2})<-[:RELATES_TO]-t
    RETURN p

> I don't know about creating accounts relationship though, if it has any use...

Try this to include `ACCOUNT`

    MATCH t:TXID--()-->a:ACCOUNT 
    WHERE t.ref = 1234 
    RETURN t, a

or even further qualify by specifying with the node label

    MATCH t:TXID--(:TRANSACTION)-->a:ACCOUNT 
    WHERE t.ref = 1234 
    RETURN t, a

Then create the `transaction` node with the found `account` node

    MATCH t:TXID--(:TRANSACTION)-->a:ACCOUNT 
    WHERE t.ref = 1234 
    CREATE UNIQUE p=(t)-[:RELATES_TO]->(tr:TRANSACTION { version:2 })-[:WITH_ACCOUNT]->(a) 
    RETURN p

or with `timestamp` as version (should be using the actual value from the transaction record)

    MATCH t:TXID--(:TRANSACTION)-->a:ACCOUNT 
    WHERE t.ref = 1234 
    CREATE UNIQUE p=(t)-[:RELATES_TO]->(tr:TRANSACTION { version:timestamp() })-[:WITH_ACCOUNT]->(a) 
    RETURN p

> Perhaps the better way might be not to match the path but rather just pick out the two nodes, `TXID` and `ACCOUNT` like below

    MATCH t:TXID, a:ACCOUNT 
    WHERE t.ref = 1234 and a.num = 4567
    CREATE UNIQUE p=(t)-[:RELATES_TO]->(tr:TRANSACTION { version:timestamp() })-[:WITH_ACCOUNT]->(a) 
    RETURN p

#### Retrieval of latest `transaction` (using order by and limit)

    MATCH t:TXID-[:RELATES_TO]->tr:TRANSACTION 
    WHERE t.ref = 1234 
    RETURN tr 
    ORDER BY tr.version DESC 
    LIMIT 1

#### Account for UTR using shortestPath

    MATCH p=shortestPath(u:UTR-[*..3]-a:ACCOUNT) 
    WHERE u.ref = 7890 
    RETURN a

> How's that different to below?

    MATCH u:UTR-[*..3]-a:ACCOUNT 
    WHERE u.ref = 7890 
    RETURN DISTINCT a

### 2. UTR message creation

> **Important !!!**
> 
> **1. Don't do any writes in this phase !!!**
>
> For the reasons of reducing transaction contention and also to allow multiple threads access to this phase (does this sentence make sense?)
> 
> Do writes in the first phase only.

> ***Actually, after a few revisions, it's necessary to do 'writes' in the second phase***
> To save the creation of UTR message and update the details (e.g. version etc)... need to think about this a bit more...

> **2. Why Not relational DB?**

> Another thought, what's the benefit of using Graph DB instead of relational DB with the following cases? It seems that the relationship can be expressed with RDBMS just as efficiently for given graph structure (refer to Visio diagram).
> 
> At this point, the only advantage of Graph DB over relational is that it is schema-less and as the requirement is still being revised it would be difficult to model it with relational DB.
>
> Also it is easier to reason with graph structure as it is more natural than FK-PK relationship.
>
> But need to consider performance / admin / maintenance aspects with choosing GraphDB over relational.

The UTR message creation is somewhat different to the way graph creation processes the message in that it needs to pull in all the nodes connected to `UTR` node then *decide* what action to take.

Three different types of *relationship* that can be connected to `UTR` node.

1. Transactions (from RTCR)
2. Provisional transaction (from RTCR)
3. FX Detail (FX Away)
4. FX Required (FX Away & MFPS)
5. Mutual Funds Fee

The general process is, for a given UTR ref (or even transaction id), get all related entities (nodes), by its relationship type. Then using the results create necessary UTR messages accordingly.

#### 2.1. Get the latest transaction

We assume the latest version will hold all necessary information.

    MATCH u:UTR-[r:transaction]->t (or t:Transaction)
    WHERE u.ref = {7800}
    RETURN t
    ORDER BY r.version DESC
    LIMIT 1

If this assumption turns out to be invalid, need to get all transactions to 'merge' the information.

    MATCH u:UTR-[r:transaction]->t (or t:Transaction)
    WHERE u.ref = {7800}
    RETURN collect(t)

Optionally add `ORDER BY r.version DESC` if needed.

#### 2.2. Determine if it is 'Provisional'

The relationship type to transaction for provisional payment is `:provisional`.

    MATCH u:UTR-[:provisional]->t (or t:Transaction)
    WHERE u.ref={7800}
    RETURN t

The `t` should be associated with `Account` of type `A`.

#### 2.3. Is FX Required?

There are multiple sources that indicates if FX is required. 

1. Transaction record itself (in FX Required = 'Y' field)
2. A message from `FX Away`
3. A message from `MFPS`

For 2 & 3, there will be a relationship established with `FX Required` node.

    MATCH u:UTR-[:require_fx]->fx:FxRequired
    WHERE u.ref={7800}
    RETURN fx

Any returned value (not none) indicates that FX is required and that is enough to determine FX required indicator.

But if none returned, the FX required value needs to be obtained from the latest transaction.

Value of 'Y' indicates yes. But an empty value may not indicate `No`.

> For these value fields, what is the agreed convention of an empty value? 
> For example, if the field should be boolean, and it doesn't have any value, does it indicate 'N' or null?

Another approach here could be to create `FX required` node when processing the transaction in phase 1. This will reduce further look up.

#### 2.4. FX Detail

Obtaining FX Detail node is trivial. But before creating a UTR message based on `FX Detail` node, the transaction must indicate if FX is required, which is related to 2.3 above.

#### 2.5. Mutual Fund fee

TBD

## Issues to resolve

1. How to determine if UTR can be archived?
2. An empty value on boolean fields (e.g. "empty" on Y/N fields - FX required). How should it be treated?
3. Want to minimize writes in the second phase of message processing but when creating the UTR message it requires to track its version number.
4. How to enforce single thread model for the same transaction identifier?



## Query

For each use case identified, develop a query (possibly with Cypher) for retrieval of information.

## Prototype

#### Consider how much to store in each node

How much is too much data on the node? Potentially the transaction information could be very large. Does it make sense and does Neo4j handle large amount of data on each node effectively?

    CREATE (a:Person {name:'Bob', age:41, title:"Some bigwig having large gut", salary:'1 billion dollars', startdate: timestamp(), hobby: 'golf & other sports stuff & really boring things like feeding goldfish', eyes: 'brown', height: 180, unit: 'cm', weight: '80kg'})
    RETURN a

Only valid way to verify this is by developing a prototype.

#### Execute Cypher query from Java?

Establish examples. Also consider using pure Java API (apparently it provides better performance - logical I suppose).

#### Single thread model

Design and code it out

# Transaction from RTCR

> Need to confirm what the structure is and the columns available.

****

# Actor

_TBD_

# Camel

_TBD_

# MQ

_TBD_
