package ru.prbb.common.it;

import ru.prbb.common.config.ApplicationSettings;
import ru.prbb.common.config.ConfigParam;

/**
 * @author lesinsa on 13.04.14.
 */
@ApplicationSettings(location = "datasource:jdbc/test?ns=ru.prbb.legacy")
public class TestConfig {
    @ConfigParam(required = true)
    private String param1;

    public String getParam1() {
        return param1;
    }
}
