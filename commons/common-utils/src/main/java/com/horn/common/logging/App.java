package com.horn.common.logging;

import org.slf4j.Logger;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author lesinsa on 05.12.2015.
 */
public interface App {
    /**
     * <p>Logger for application lifecycle logging.</p>
     * <p>
     * This logger message wil be redirected to server log if configured in logback-xxx.xml (see module common-logging).
     */
    Logger LOG = getLogger("com.horn.app");

}

