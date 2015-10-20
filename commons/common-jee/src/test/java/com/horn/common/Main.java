package com.horn.common;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.config.Configuration;
import net.sf.ehcache.config.TerracottaConfiguration;

/**
 * @author lesinsa
 */
public class Main {
    public static void main(String[] args) {
        Configuration configuration = new Configuration()
                .defaultCache(new CacheConfiguration("defaultCache", 100))
                .cache(new CacheConfiguration("example", 100)
                        .timeToIdleSeconds(5)
                        .timeToLiveSeconds(120)
                        .terracotta(new TerracottaConfiguration()));
        CacheManager manager = new CacheManager(configuration);
    }
}
