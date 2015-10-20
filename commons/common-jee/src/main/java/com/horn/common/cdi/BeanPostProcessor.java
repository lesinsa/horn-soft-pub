package com.horn.common.cdi;

import java.lang.annotation.Annotation;

/**
 * @author lesinsa on 23.03.14.
 */
public interface BeanPostProcessor<T extends Annotation> {
    void processBean(T annotation, Object object);
}
