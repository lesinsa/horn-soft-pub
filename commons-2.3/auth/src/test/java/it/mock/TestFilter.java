package it.mock;

import ru.prbb.security.SecurityFilter;

import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;

/**
 * @author LesinSA
 */
@WebFilter(filterName = "test", urlPatterns = "/test/*",
        initParams = {
                @WebInitParam(name = SecurityFilter.DOMAIN_MARAM, value = TestFilter.TEST_DOMAIN),
                @WebInitParam(name = SecurityFilter.APP_ID_PARAM, value = "test-auth")
        }
)
public class TestFilter extends SecurityFilter {
    public static final String TEST_DOMAIN = "TEST";
}
