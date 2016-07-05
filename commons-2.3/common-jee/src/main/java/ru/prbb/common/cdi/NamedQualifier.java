package ru.prbb.common.cdi;

import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Named;

/**
 * @author LesinSA
 */
public class NamedQualifier extends AnnotationLiteral<Named> implements Named {

    private final String value;

    public NamedQualifier(String value) {
        this.value = value;
    }

    @Override
    public String value() {
        return value;
    }
}
