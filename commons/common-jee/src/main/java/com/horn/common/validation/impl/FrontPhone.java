package com.horn.common.validation.impl;

import javax.validation.Constraint;
import javax.validation.constraints.Pattern;

/**
 * @author LesinSA
 */
@Constraint(validatedBy = {})
@Pattern(regexp = "[0-9]{7,14}")
public @interface FrontPhone {

}
