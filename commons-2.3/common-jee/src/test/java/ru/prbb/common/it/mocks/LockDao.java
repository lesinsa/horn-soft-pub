package ru.prbb.common.it.mocks;

import ru.prbb.common.lock.AbstractLockDaoImpl;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author LesinSA
 */
@Stateless
public class LockDao extends AbstractLockDaoImpl implements ru.prbb.common.lock.LockDaoSpi {

    @PersistenceContext
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
