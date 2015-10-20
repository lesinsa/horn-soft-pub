package com.horn.common.it;

import com.horn.common.config.ConfigParam;
import com.horn.common.config.ApplicationSettings;

/**
 * @author lesinsa on 13.04.14.
 */
@ApplicationSettings(location = "datasource:jdbc/test?ns=com.horn.legacy")
public class TestConfig {
    @ConfigParam(required = true)
    private String param1;

    public String getParam1() {
        return param1;
    }
}
