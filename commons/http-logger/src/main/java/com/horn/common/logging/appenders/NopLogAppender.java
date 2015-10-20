package com.horn.common.logging.appenders;

import com.horn.common.logging.HttpLogContext;
import com.horn.common.logging.HttpLogAppender;

import java.util.List;

/**
 * @author by lesinsa on 18.10.2015.
 */
public class NopLogAppender implements HttpLogAppender {

    @Override
    public void append(HttpLogContext logContext) {
        // nothing to do
    }

    @Override
    public void append(List<HttpLogContext> logContexts) {
        // nothing to do
    }
}
