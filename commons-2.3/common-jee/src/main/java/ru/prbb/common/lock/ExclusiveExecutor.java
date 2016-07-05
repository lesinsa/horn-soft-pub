package ru.prbb.common.lock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.concurrent.TimeUnit;

/**
 * @author lesinsa
 */
@Singleton
public class ExclusiveExecutor {
    private static final Logger LOG = LoggerFactory.getLogger(ExclusiveExecutor.class);
    @Inject
    private LockManager lockManager;

    public boolean doExclusiveJob(String name, RunnableJob runnable) {
        // чтобы избежать одновременного выполнения в кластерной среде
        boolean locked = lockManager.acquireGlobalLock(name, TimeUnit.HOURS, 4);
        if (!locked) {
            LOG.info("Job '{}' is not started - another executor has taken lock", name);
            return false;
        }
        try {
            runnable.run();
        } finally {
            lockManager.releaseLock(name);
        }
        LOG.info("Job '{}' has been successfully performed", name);
        return true;
    }

}
