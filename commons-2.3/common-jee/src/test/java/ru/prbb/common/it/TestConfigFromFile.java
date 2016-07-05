package ru.prbb.common.it;

import ru.prbb.common.config.ApplicationSettings;

import javax.enterprise.context.Dependent;

/**
 * @author lesinsa
 */
@ApplicationSettings(location = "classpath:/test.properties")
@Dependent
public class TestConfigFromFile extends TestConfig {
}
