package com.horn.common.lock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.enterprise.event.Observes;
import javax.enterprise.event.TransactionPhase;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.persistence.OptimisticLockException;
import java.util.concurrent.TimeUnit;

/**
 * @author lesinsa
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class LockManager {
    private static final Logger LOG = LoggerFactory.getLogger(LockManager.class);

    @Inject
    private RestoreSchedule restoreSchedule;
    @Inject
    @SuppressWarnings("CdiInjectionPointsInspection")
    private Instance<LockDaoSpi> lockDaoInstance;

    public boolean acquireGlobalLock(String name, TimeUnit unit, long unlockAfter) {
        boolean result = acquireGlobalLock(name);
        if (result) {
            // создаем отложенное гарантированное освобождение семафора, на случай,
            // если releaseLock не будет вызван по какой-либо причине
            long duration = unit.toMillis(unlockAfter);
            restoreSchedule.triggerAfter(name, duration);
        }
        return result;
    }

    public boolean acquireGlobalLock(String name) {
        LockDaoSpi lockDao = lockDaoInstance.get();
        try {
            GlobalLock lock = lockDao.find(name);
            if (lock == null) {
                lock = new GlobalLock(name, 1);
                lockDao.save(lock);
            } else {
                if (lock.getLocked() != 0) {
                    return false;
                }
                lock.setLocked(1);
                lockDao.update(lock);
            }
            return true;
        } catch (OptimisticLockException e) {
            LOG.debug("Update conflict", e);
            return false;
        }
    }


    public void releaseLock(String name) {
        GlobalLock lock = doReleaseLock(name);
        if (lock == null) {
            return;
        }
        restoreSchedule.stopTimer(lock.getName());
    }

    public void onUnlock(@Observes(during = TransactionPhase.AFTER_COMPLETION) RestoreSchedule.GlobalUnlockEvent event) {
        doReleaseLock(event.getName());
    }

    private GlobalLock doReleaseLock(String name) throws OptimisticLockException {
        LockDaoSpi lockDao = lockDaoInstance.get();
        GlobalLock lock = lockDao.find(name);
        if (lock == null) {
            LOG.warn("Trying release lock '{}' which does not exists", name);
            return null;
        }
        if (lock.getLocked() != 1) {
            LOG.warn("Trying release lock '{}' which already free", name);
            return null;
        }
        lock.setLocked(0);
        lockDao.update(lock);
        return lock;
    }
}
