# How to process cash transactions

Think about the process in terms of cash transaction being fed into utr process. Then also think about other types of messages/data as input to utr.

What about:

- mutual funds transactions (UTR7)
- fx message

## Assumption

I keep forgetting this but the assumption on how the transactions for A account are related to transactions for B account is that they both have the same transaction reference. This is scenario needed only for corporate actions and under provisional payment situation.

If this assumption doesn't hold, and there's no simple way of correlating transactions between A & B accounts, this could potentially change everything.

## Table structure

#### Utr Register

	UTR_REGISTER (
	  id (seq),
	  transaction_ref (varchar unique),
	  a_account (varchar null),  // ????  i don't think so...
	  b_account (varchar null)   // ????  i don't think so...
	)

#### UTR Snapshot

	UTR_SNAPSHOT (
	  version (num not null),
	  utr_register_id (fk not null),
	  cash_transaction_id (null),
	  fx_detail_id (null), // what about is_fx
	  mf_transaction_id (null)	// for mf fee
	)

	unique const on [version & utr_register_id]

#### UTR

	UTR (
	  id (seq),
	  version (num not null),
	  utr_register_id (fk not null),
	  ... // actual fields and
	  ...xxx_date,	// for each data source transaction, store its transaction timestamp for comparison
	  created_timestamp
	)

	unique const on [version & utr_register_id]

#### Cash Transaction

	CASH_TRANSACTION (
	  id (from cash system),
	  ...
	  utr_register_id (fk null),
	  created_timestamp
	)

#### Mutual Funds

	???

#### FX Away

	???

## Process

#### 1. First step

1. Receive input transaction data/message
2. Somehow validate/filter/transform to workable representation
	- Need to know its Account type (A or B) - via DB lookup
	- Add the Account type info in the message
	- In case of A type, check for Provisional Income Indicator to be 'Y'
	- Ignore all others
3. Somehow store as `Transaction` in db

#### 2. Construct core message

1. Determine account type for the transaction
	- DB lookup
> Already done in previous step?
2. Determine `transactionRef` from the object 
	- Corporate action
	- Custody
	- MF
	- Anything else?
> It could fail because it's invalid

3. Reduce the 'transaction' message down to `CoreMessage` below
4. Then pass on this new message to the next route

##### 
	RegisterReferenceMessage {
		TransactionRef,
		Transaction id,
		Account type,
		Account number
	}


#### 3. Register the `transactionRef`

1. Create or get `UtrRegister` entity based on `transactionRef`
	- Store the `account number` also
	- ***What if there's exception due to tx conflict? - retry etc??? I think it should feed it back***
2. Place `UtrRegister.id` in the exchange message for the next step
3. Update the `Transaction` entity with the `UtrRegister.id`

> * Think about transaction boundary. Having it on (1) reduces conflict but having it over (1-4) may be more correct.
>
> * Also what is there to do with Account Type info at this stage???

##### 
	CreateSnapshotMessage {
		TransactionRef,
		Transaction id,
	  	UtrRegister id,		// ** added **
		Account type,
		Account number
	}

#### 4. Create Snapshot

##### Gather data (assuming latest transactions will have **all** data accumulated in it so that 'merge' isn't needed)

This process is assuming that there's a separate pipeline for each data source (expand on this). Hence the `Transaction id` is context bound in that it refers to fx detail id for FX detail pipe and it may refer to cash transaction id in cash pipeline.

Perform the following within a transaction boundary.

1. With `UtrRegister.id` retrieve the **latest** `Transaction`
2. For mutual funds fee, we may need to store them in a separate table like `MutualFundsTransaction` then also retrieve the **latest** transaction for it.
3. If FX messages were there, should perform similar process by retrieving the FX information
4. Compare the `Transaction id` in the message with the 'latest' id found. If they are different stop this process and log. No further processing is needed. It's handled by another process.
5. Find the latest `UtrSnapshot` with `UtrRegister.id`. If found the next version number is current version + 1, else the version is 1 (new)
6. Include `cash_transaction_id`, `fx_detail_id`, `mf_transaction_id` in the `UtrSnapshot` if available
7. Attempt to save / commit this. Because of unique constraint on `version + UtrRegister.id` on `UTR_SNAPSHOT` table the duplicate will fail. Which implies another transaction processing got there first hence this message needs to be re-processed. Route it back to the beginning of 'Create Snapshot'.
8. Include the snapshot `version` number in the message

##### Gather data (if merge is required)

Same as above, except that for the source transaction it's processing, don't load the latest, but use `Transaction id` in the message. But for 'other' transactions, load the latest data.

##### 
	CoreMessage {
		TransactionRef,
		Transaction id,
		UtrRegister id,
	  	snapshot_version,	// ** added **
		Account type,
		Account number
	}

#### 5. Determine UTR message generation

1. Get `UtrSnapshot` with `UtrRegisterId` and `snapshot_version` from the message
2. Retrieve all the latest data with `UtrSnapshot`
3. Transform and collate all into a UTR object
4. Get latest `UTR` 
	1. if none, save the UTR object with version 1
	2. if found one, inc the version and
		- for each data source
			merge(Utr object from step 3, found one) - using merge strategy
		- also set the date fields for each transaction id/source
	3. Validate the merges UTR message
	4. then save
	5. if exception (duplicate) re-process
5. forward the message to next node

##### Merge process

Let's assume all UTR messages are created by 'merge' of all transaction information. But for 'merge' to work, there has to be a baseline. In this case, it's a UTR message and let's assume it is persisted and can be uniquely identified with its 'UtrRegister.id + version'.

To start there's an empty UTR. When the first transaction is processes and after the UTR Snapshot is created, it retrieves all associated transaction data. The UTR message is constructed 

If there's an existing UTR message (findLatestByRegoId), retrieve related UTRSnapshot

##### Per field

1. if the new UTR has transaction that is newer than the transaction on existing UTR
	- if the new UTR field is null, copy from old
	- otherwise do nothing
2. if the new UTR has transaction that is older than the transaction on existing UTR
	- copy from existing UTR to the new UTR as long as it is not null


# Trash

#### Old 4. Determine if UTR need to be generated

##### Gather data (Assuming latest transactions will have **all** data accumulated in it)

1. With `UtrRegister.id` retrieve the **latest** `Transaction`
2. For mutual funds fee, we may need to store them in a separate table like `MutualFundsTransaction` then also retrieve the **latest** transaction for it.
3. If FX messages were there, should perform similar process by retrieving the FX information
4. Find latest `Utr` with `UtrRegister.id` if found the new version is current version + 1 else version number is 1 (new).
5. Because there's unique constraint on `UTR` table for `version` + `utr_register_id` the db commit will fail if there's a constraint violation exception. This case is illustrated as an example below. The flow (b) & (c) participates in this scenario.
6. The idea is that the failed flow/transaction should be 're-tried' so that the message is not lost.
7. To support append the `Utr.version` number in the message

#####
	CoreMessage {
		TransactionRef,
		Transaction id,
	  	UtrRegister id,
	  	Utr Version,		// ** added **
		Account type,
		Account number
	}

##### Analyze 

In step (1) if the latest `Transaction.id` is not the same as what's in the message, it should log and skip. It implies there's another transaction process in-flight and it should be done there. So done with (a). But there's still a race condition where by:

	(a) T1 ... [Step 4] : loads latest and loads T2, so ignore this one

	(b) T2 ... [Step 4] : loads latest and load T2 so continue 

	(c) T3 ... [Step 4] : loads latest and load T3 so continue

The two possible outcomes:

1. (b) fails & (c) succeeds
2. (b) succeeds & (c) fails




> ***If the assumption doesn't hold, some sort of 'merge' will have to take place - todo***
