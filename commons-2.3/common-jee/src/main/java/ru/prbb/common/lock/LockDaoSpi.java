package ru.prbb.common.lock;

import javax.ejb.Local;
import javax.persistence.OptimisticLockException;

/**
 * @author LesinSA
 */
@Local
public interface LockDaoSpi {
    GlobalLock find(String name);

    void save(GlobalLock globalLock);

    GlobalLock update(GlobalLock globalLock) throws OptimisticLockException;
}
