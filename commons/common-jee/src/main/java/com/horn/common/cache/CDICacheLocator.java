package com.horn.common.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.horn.common.cdi.NamedQualifier;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

/**
 * @author by lesinsa on 19.09.2015.
 */
@ApplicationScoped
public class CDICacheLocator implements CacheLocator {
    private static final Logger LOG = LoggerFactory.getLogger(CDICacheLocator.class);

    @Any
    @Inject
    private Instance<CacheStore> cacheStoreInstance;

    private CacheStore storeStub = CacheStoreStub.getInstance();

    @Override
    public CacheStore lookup(String name) {
        Instance<CacheStore> instance = cacheStoreInstance.select(new NamedQualifier(name));
        if (instance.isAmbiguous()) {
            LOG.warn("CacheStore implementation is ambiguous for name '{}'", name);
            return storeStub;
        } else if (instance.isUnsatisfied()) {
            LOG.warn("CacheStore implementation is unsatisfied for name '{}'", name);
            return storeStub;
        }
        return cacheStoreInstance.select(new NamedQualifier(name)).get();
    }
}
