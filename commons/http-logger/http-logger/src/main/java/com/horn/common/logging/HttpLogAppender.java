package com.horn.common.logging;

import java.util.List;

/**
 * @author by lesinsa on 02.10.2015.
 */
public interface HttpLogAppender {

    void append(HttpLogContext logContext);

    void append(List<HttpLogContext> logContexts);
}
