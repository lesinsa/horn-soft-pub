package com.horn.common.logging.cdi;

import com.horn.common.cdi.ProgrammaticCdiBeanLookup;
import com.horn.common.logging.HttpLogContext;
import com.horn.common.logging.internal.ContentFilter;
import com.horn.common.logging.internal.HttpLogConfigurator;
import com.horn.common.logging.internal.LogBodyConfig;

/**
 * @author lesinsa on 16.11.2015.
 */
// TODO to be continued...
public class CdiConfigurator implements HttpLogConfigurator {

    private final RBean rBean;

    public CdiConfigurator() {
        rBean = ProgrammaticCdiBeanLookup.getBeanInstance(RBean.class);
    }

    @Override
    public boolean isRequestBodyLoggingEnabled(HttpLogContext context) {
        return false;
    }

    @Override
    public boolean isResponseBodyLoggingEnabled(HttpLogContext context) {
        return false;
    }

    @Override
    public LogBodyConfig getRequestBodyConfig(HttpLogContext logContext) {
        return ContentFilter.DEFAULT_REQUEST_BODY_CONFIG;
    }

    @Override
    public LogBodyConfig getResponseBodyConfig(HttpLogContext logContext) {
        return ContentFilter.DEFAULT_RESPONSE_BODY_CONFIG;
    }
}
