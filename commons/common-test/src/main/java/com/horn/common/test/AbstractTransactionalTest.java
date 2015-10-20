package com.horn.common.test;

import org.junit.After;
import org.junit.Before;

import javax.inject.Inject;
import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

/**
 * @author lesinsa
 */
public abstract class AbstractTransactionalTest {

    @Inject
    private UserTransaction ut;

    @Before
    public void setUp() throws SystemException, NotSupportedException {
        ut.begin();
    }

    @After
    public void tearDown() throws SystemException {
        ut.rollback();
    }
}
