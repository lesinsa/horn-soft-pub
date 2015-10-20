package com.horn.common.cdi.sampleclass;

import javax.inject.Qualifier;
import java.lang.annotation.*;

/**
 * @author lesinsa on 23.03.14.
 */
@Qualifier
@Inherited
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER, ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.RUNTIME)
public @interface Q4 {
}
