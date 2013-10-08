### Create UTR from nothing

    CREATE g=(u:UTR { ref:7890 })-[:ID_BY]->(t:TXID { num:1234 })-[:RELATES_TO]->(tr:TRANSACTION)-[:WITH_ACCOUNT]->(a:ACCOUNT { num:4567, type:'B' }) 
    RETURN g

### Create UTR from nothing with Client SEB

    CREATE p=(c:CLIENT { name: 'SEB'})<-[:CLIENT]-(u:UTR { ref:7890 })-[:ID_BY]->(t:TXID { num:1234 })-[:RELATES_TO]->(tr:TRANSACTION {version: 1})-[:WITH_ACCOUNT]->(a:ACCOUNT { num:4567, type:'B' }) 
    RETURN p

### Attach UTR with Client node

    MATCH u:UTR 
    WHERE u.ref = 7890 
    CREATE UNIQUE u-[:CLIENT]->(c:Client { name:'SEB' }) 
    RETURN c

### Create index on a label

    CREATE INDEX ON :UTR(ref)

### Using label index on query

    match u:UTR
    using index u:UTR(ref)
    where u.ref = 7890 and u.another_prop = 'xyz'
    return u.ref

### ???

CREATE (u:UTR {ref:900, tx:100})

MATCH u:UTR 
WHERE u.tx = 100 
WITH u 
MATCH u--()--a:ACCOUNT 
RETURN a


MATCH u:UTR 
WHERE u.tx = 100 
CREATE UNIQUE p =(u)-[:relates_to]->(t:TRANSACTION {version:timestamp()})-[:account]->(a:ACCOUNT { num:567 }) 
RETURN p


MATCH u:UTR, a:ACCOUNT 
WHERE u.tx = 100 AND a.num = 567 
CREATE UNIQUE p =(u)-[:relates_to]->(t:TRANSACTION { version:timestamp()})-[:account]->(a) 
RETURN p

