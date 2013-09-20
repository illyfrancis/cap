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

#### UTR

	UTR (
	  id (seq),
	  version (num not null),
	  utr_register_id (fk not null)
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
	CoreMessage {
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
	CoreMessage {
		TransactionRef,
		Transaction id,
	  	UtrRegister id,		// ** added **
		Account type,
		Account number
	}

#### 4. Determine if UTR need to be generated

##### Assuming latest transactions will have **all** data accumulated in it
1. With `UtrRegister.id` retrieve the **latest** `Transaction`
2. For mutual funds fee, we may need to store them in a separate table like `MutualFundsTransaction` then also retrieve the **latest** transaction for it.
3. If FX messages were there, should perform similar process by retrieving the FX information

In step (1) if the latest `Transaction.id` is not the same as what's in the message, it should skip. Implies there's another transaction process in-flight and it should be done there. But there's still a race condition where by:

	T1 ... [Step 4] : loads latest and loads T2, so ignore this one

	T2 ... [Step 4] : loads latest and load T2 so continue 

	T3 ... [Step 4] : loads latest and load T3 so continue

> ***If the assumption doesn't hold, some sort of 'merge' will have to take place - todo***
