package ru.prbb.common.config;

import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Qualifier;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author lesinsa on 23.03.14.
 */
@Qualifier
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ConfigProtocol {
    String value();

    public static class ConfigProtocolLiteral extends AnnotationLiteral<ConfigProtocol> implements ConfigProtocol {

        private final String value;

        public ConfigProtocolLiteral(String value) {
            this.value = value;
        }

        @Override
        public String value() {
            return value;
        }
    }
}
