package com.acme.cap.rdb.domain.mock;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.RollbackException;
import javax.transaction.Synchronization;
import javax.transaction.SystemException;
import javax.transaction.xa.XAResource;

public class MockTransaction implements javax.transaction.Transaction {
    
    public MockTransaction() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public void commit() throws HeuristicMixedException, HeuristicRollbackException, RollbackException,
            SecurityException, SystemException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public boolean delistResource(XAResource xaRes, int flag) throws IllegalStateException, SystemException {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean enlistResource(XAResource xaRes) throws IllegalStateException, RollbackException,
            SystemException {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public int getStatus() throws SystemException {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void registerSynchronization(Synchronization synch) throws IllegalStateException,
            RollbackException, SystemException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void rollback() throws IllegalStateException, SystemException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setRollbackOnly() throws IllegalStateException, SystemException {
        // TODO Auto-generated method stub
        
    }

}
