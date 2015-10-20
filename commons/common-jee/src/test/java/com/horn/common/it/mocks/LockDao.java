package com.horn.common.it.mocks;

import com.horn.common.lock.AbstractLockDaoImpl;
import com.horn.common.lock.LockDaoSpi;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author LesinSA
 */
@Stateless
public class LockDao extends AbstractLockDaoImpl implements LockDaoSpi {

    @PersistenceContext
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
