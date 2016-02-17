package com.horn.common.logging.internal;

import com.horn.common.logging.HttpLogAppender;
import com.horn.common.logging.HttpLogConfigException;
import com.horn.common.logging.config.AppenderDef;
import com.horn.common.logging.config.AppenderRef;
import com.horn.common.logging.config.HttpLogConfiguration;
import com.horn.common.logging.config.LoggerDef;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.horn.common.StringUtils.isEmpty;

/**
 * @author by lesinsa on 15.10.2015.
 */
public class Loggers {

    private final PathMap<List<HttpLogger>> pathMap;

    public Loggers(HttpLogConfiguration rootConfig) {
        this.pathMap = new PathMap<>();

        // creating appenders
        Map<String, HttpLogAppender> appenders = buildAppenders(rootConfig.getAppenderDefs());

        for (LoggerDef loggerDef : rootConfig.getLoggerDefs()) {
            List<HttpLogger> httpLoggerList = pathMap.get(loggerDef.getPath());
            if (httpLoggerList == null) {
                httpLoggerList = new ArrayList<>();
                pathMap.put(loggerDef.getPath(), httpLoggerList);
            }
            // peeking appenders mentioned in Http logger definition
            List<HttpLogAppender> selectedAppenders = takeMatchingAppenders(appenders, loggerDef);
            HttpLogger httpLogger = createHttpLogger(loggerDef, selectedAppenders);
            httpLoggerList.add(httpLogger);
        }
    }

    private HttpLogger createHttpLogger(LoggerDef loggerDef, List<HttpLogAppender> selectedAppenders) {
        if (isEmpty(loggerDef.getCustomConfiguratorClass())) {
            return new HttpLoggerImpl(loggerDef, selectedAppenders);
        } else {
            HttpLogConfigurator configurator = HttpLogConfigurator.getInstance(loggerDef.getCustomConfiguratorClass());
            return new HttpLoggerCustom(selectedAppenders, configurator);
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

    private Map<String, HttpLogAppender> buildAppenders(List<AppenderDef> appenderDefs) {
        Map<String, HttpLogAppender> appenders = new HashMap<>();
        for (AppenderDef appenderDef : appenderDefs) {
            HttpLogAppender a = appenders.get(appenderDef.getName());
            if (a != null) {
                throw new HttpLogConfigException("Duplicate for appender with name '" + appenderDef.getName() + "'");
            }
            HttpLogAppender appender = AppenderBuilder.build(appenderDef);
            appenders.put(appenderDef.getName(), appender);
        }
        return appenders;
    }

    private List<HttpLogAppender> takeMatchingAppenders(Map<String, HttpLogAppender> appenders, LoggerDef loggerDef) {
        List<HttpLogAppender> selectedAppenders = new ArrayList<>();
        for (AppenderRef appenderRef : loggerDef.getAppenderRefs()) {
            AppenderDef appenderDef = appenderRef.getAppenderDef();
            String name = appenderDef.getName();
            HttpLogAppender appender = appenders.get(name);
            selectedAppenders.add(appender);
        }
        return selectedAppenders;
    }
}
