package com.horn.common.config;

import com.horn.common.cdi.BeanPostProcessor;
import com.horn.common.conversion.ConversionException;
import com.horn.common.conversion.ConverterManager;
import com.horn.common.conversion.ConverterSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * @author lesinsa on 23.03.14.
 */
public class ConfigBeanPostProcessor implements BeanPostProcessor<ApplicationSettings> {
    private static final Logger LOG = LoggerFactory.getLogger(ConfigBeanPostProcessor.class);

    @Any
    @Inject
    @SuppressWarnings("CdiInjectionPointsInspection")
    private Instance<ConfigLoader> configLoaderInstance;
    private LoaderResolver resolver;

    @SuppressWarnings("UnusedDeclaration")
    public ConfigBeanPostProcessor() {
        // required by CDI
    }

    public ConfigBeanPostProcessor(LoaderResolver resolver) {
        this.resolver = resolver;
    }

    @Override
    public void processBean(ApplicationSettings annotation, Object object) {
        try {
            String location = annotation.location();
            UrlInfo urlInfo = new UrlInfo(location);

            ConfigLoader configLoader = resolveConfigLoader(urlInfo);
            Properties properties = configLoader.load(urlInfo);

            // map properties to config bean
            Field[] fields = getParentFields(object.getClass());
            mapPropertiesToFields(annotation, object, properties, fields);
            mapPropertiesToFields(annotation, object, properties, object.getClass().getDeclaredFields());
        } catch (RuntimeException e) {
            LoggerFactory.getLogger(ConfigBeanPostProcessor.class)
                    .error("", e);
            throw e;
        }
    }

    private Field[] getParentFields(Class claz) {
        List<Field> fields = new ArrayList<>();
        Class superclass = claz.getSuperclass();
        while (superclass != null) {
            fields.addAll(Arrays.asList(superclass.getDeclaredFields()));
            superclass = superclass.getSuperclass();
        }
        Field[] fieldsArray = new Field[fields.size()];
        int i = 0;
        for (Field field : fields) {
            fieldsArray[i] = field;
            i++;
        }
        return fieldsArray;
    }

    private void mapPropertiesToFields(ApplicationSettings annotation, Object object, Properties properties, Field[] declaredFields) {
        List<ConfigParamError> errors = new ArrayList<>();
        ConverterManager converterManager = ConverterManager.getInstance(ConverterSet.STANDARD);
        for (Field field : declaredFields) {
            ConfigParam configAnnotation = field.getAnnotation(ConfigParam.class);
            if (configAnnotation != null) {
                String name = "".equals(configAnnotation.name()) ? field.getName() : configAnnotation.name();
                try {
                    String defaultValue = !"".equals(configAnnotation.defaultValue()) ? configAnnotation.defaultValue() : null;
                    boolean required = configAnnotation.required();
                    String paramValue = (String) properties.get(name);

                    paramValue = takeRequiredParam(defaultValue, required, paramValue);

                    // конвертируем из строки в целевой тип
                    Object converted = converterManager.convert(paramValue, field.getType());
                    field.setAccessible(true);
                    field.set(object, converted);
                } catch (AddException | IllegalAccessException | RuntimeException | ConversionException e) {
                    addParamError(errors, name, e);
                }
            }
        }
        if (!errors.isEmpty()) {
            // выводим в лог список нарушений по полям
            logErrors(annotation, errors);
        }
    }

    private void addParamError(List<ConfigParamError> errors, String name, Exception e) {
        LOG.debug("", e);
        errors.add(new ConfigParamError(name, e.getMessage()));
    }

    private String takeRequiredParam(String defaultValue, boolean required, String paramValue) throws AddException {
        if (paramValue == null) {
            if (required) {
                throw new AddException("Missed required configuration parameter");
            } else {
                return defaultValue;
            }
        }
        return paramValue;
    }

    private void logErrors(ApplicationSettings annotation, List<ConfigParamError> errors) {
        String message = "Errors occurred for configuration: " + annotation.location();
        LOG.error(message);
        LOG.error("Details are follows:");
        for (ConfigParamError error : errors) {
            LOG.error("Param '{}' - '{}'", error.getName(), error.getDescription());
        }
        throw new ConfigException(message);
    }

    private ConfigLoader resolveConfigLoader(UrlInfo urlInfo) {
        if (resolver == null) {
            return configLoaderInstance.select(new ConfigProtocol.ConfigProtocolLiteral(urlInfo.getProtocol())).get();
        } else {
            return resolver.resolve(urlInfo.getProtocol());
        }
    }

    private static class ConfigParamError {
        private final String name;
        private final String description;

        private ConfigParamError(String name, String description) {
            this.name = name;
            this.description = description;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }
    }

    private static class AddException extends Exception {
        private AddException(String message) {
            super(message);
        }
    }
}
