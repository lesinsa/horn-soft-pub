package com.horn.common.it;

import com.horn.common.config.ApplicationSettings;

import javax.enterprise.context.Dependent;

/**
 * @author lesinsa
 */
@ApplicationSettings(location = "classpath:/test.properties")
@Dependent
public class TestConfigFromFile extends TestConfig {
}
