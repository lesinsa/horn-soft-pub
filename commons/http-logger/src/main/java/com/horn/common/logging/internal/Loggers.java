package com.horn.common.logging.internal;

import com.horn.common.logging.config.AppenderDef;
import com.horn.common.logging.config.AppenderRef;
import com.horn.common.logging.HttpLogAppender;
import com.horn.common.logging.HttpLogConfigException;
import com.horn.common.logging.config.HttpLogConfiguration;
import com.horn.common.logging.config.LoggerDef;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author by lesinsa on 15.10.2015.
 */
public class Loggers {

    private final PathMap<List<HttpLogger>> pathMap;

    public Loggers(HttpLogConfiguration rootConfig) {
        this.pathMap = new PathMap<>();

        // creating appenders
        Map<String, HttpLogAppender> appenders = new HashMap<>();
        List<AppenderDef> appenderDefs = rootConfig.getAppenderDefs();
        for (AppenderDef appenderDef : appenderDefs) {
            HttpLogAppender a = appenders.get(appenderDef.getName());
            if (a != null) {
                throw new HttpLogConfigException("Duplicate for appender with name '" + appenderDef.getName() + "'");
            }
            HttpLogAppender appender = AppenderBuilder.build(appenderDef);
            appenders.put(appenderDef.getName(), appender);
        }

        List<LoggerDef> loggerDefs = rootConfig.getLoggerDefs();

        for (LoggerDef loggerDef : loggerDefs) {
            List<HttpLogger> httpLoggerList = pathMap.get(loggerDef.getPath());
            if (httpLoggerList == null) {
                httpLoggerList = new ArrayList<>();
                pathMap.put(loggerDef.getPath(), httpLoggerList);
            }
            HttpLogger httpLogger = new HttpLogger(loggerDef);
            httpLoggerList.add(httpLogger);

            List<AppenderRef> appenderRefs = loggerDef.getAppenderRefs();
            for (AppenderRef appenderRef : appenderRefs) {
                AppenderDef appenderDef = appenderRef.getAppenderDef();
                String name = appenderDef.getName();
                HttpLogAppender appender = appenders.get(name);
                httpLogger.getAppenders().add(appender);
            }
        }
    }

    public HttpLogger match(HttpServletRequest request) {
        String path = Utils.takePath(request);
        List<HttpLogger> httpLoggerList = pathMap.match(path);
        if (httpLoggerList == null) {
            return null;
        }
        for (HttpLogger httpLogger : httpLoggerList) {
            if (httpLogger.accept(request)) {
                return httpLogger;
            }
        }
        return null;
    }
}
