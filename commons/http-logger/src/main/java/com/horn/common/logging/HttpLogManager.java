package com.horn.common.logging;

import org.slf4j.LoggerFactory;
import com.horn.common.logging.config.HttpLogConfiguration;
import com.horn.common.logging.internal.HttpLogReaderImpl;
import com.horn.common.logging.internal.HttpLogger;
import com.horn.common.logging.internal.Loggers;
import com.horn.common.logging.internal.Utils;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 * @author by lesinsa on 26.09.2015.
 */
public final class HttpLogManager {
    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(HttpLogManager.class);

    private static ThreadLocal<HttpLogContext> threadLocal = new ThreadLocal<>();
    private final Loggers loggers;
    private HttpLogReader logReader;

    private HttpLogManager(Loggers loggers) {
        this.loggers = loggers;
    }

    public static HttpLogManager getInstance(HttpLogConfiguration configuration) {
        Loggers loggers = new Loggers(configuration);
        HttpLogManager instance = new HttpLogManager(loggers);
        if (configuration.getReaderDataSource() != null) {
            try {
                DataSource dataSource = InitialContext.doLookup(configuration.getReaderDataSource());
                instance.logReader = new HttpLogReaderImpl(dataSource);
            } catch (NamingException e) {
                throw new HttpLogConfigException("DataSource is not found", e);
            }
        }
        return instance;
    }

    public static void removeThreadRef() {
        threadLocal.remove();
    }

    public HttpLogContext handle(HttpServletRequest request, HttpServletResponse response) {
        HttpLogger httpLogger = loggers.match(request);
        if (httpLogger == null) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("No logger found - skipping: {}", Utils.requestToString(request));
            }
            return null;
        }
        // creating thread-local Logging Request Context
        HttpLogContext logContext = new HttpLogContext(request, response, httpLogger);
        threadLocal.set(logContext);
        // returning wrappers
        return logContext;
    }

    public HttpLogReader getLogReader() {
        return logReader;
    }
}
