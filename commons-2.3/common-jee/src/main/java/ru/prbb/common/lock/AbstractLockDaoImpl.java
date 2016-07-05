package ru.prbb.common.lock;

import javax.persistence.EntityManager;
import javax.persistence.OptimisticLockException;

/**
 * @author LesinSA
 */
public abstract class AbstractLockDaoImpl {

    protected abstract EntityManager getEntityManager();

    public GlobalLock find(String name) {
        return getEntityManager().find(GlobalLock.class, name);
    }

    public void save(GlobalLock globalLock) {
        getEntityManager().persist(globalLock);
    }

    public GlobalLock update(GlobalLock globalLock) throws OptimisticLockException {
        EntityManager em = getEntityManager();
        GlobalLock mergedResult = em.merge(globalLock);
        em.flush();
        return mergedResult;
    }

}
