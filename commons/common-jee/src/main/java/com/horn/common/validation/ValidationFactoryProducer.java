package com.horn.common.validation;

import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Produces;
import javax.inject.Singleton;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;

/**
 * @author LesinSA
 */
@Singleton
public class ValidationFactoryProducer {

    private ValidatorFactory validatorFactory;

    @PostConstruct
    public void init() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        LoggerFactory.getLogger(getClass()).info("Validator factory is initialized");
    }

    @Factory
    @Singleton
    @Produces
    public ValidatorFactory getValidatorFactory() {
        return validatorFactory;
    }
}
