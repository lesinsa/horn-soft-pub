package ru.prbb.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static ru.prbb.common.NullUtils.coalesce;

/**
 * @author LesinSA
 */
public class SecurityFilter implements Filter {

    public static final String DOMAIN_MARAM = "USER_DOMAIN";
    public static final String AUTH_HEADER_PARAM = "AUTH_HEADER";
    public static final String ACCESS_TOKEN_NAME_PARAM = "TOKEN_NAME";
    public static final String DEFAULT_TOKEN_NAME = "access_token";
    public static final String APP_ID_PARAM = "APP_ID";
    public static final String ACCESS_TOKEN_PLACE_PARAM = "ACCESS_TOKEN_PLACE";
    public static final String DEFAULT_ACCESS_TOKEN_PLACE = "PARAMETER";

    private static final Logger LOG = LoggerFactory.getLogger(SecurityFilter.class);

    @Inject
    private UserService service;
    @Inject
    private SecurityConfig config;

    private String tokenName;
    private String authHeader;
    private String tokenPlace;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String token;
        //если передается токен в Header-е, то берем соответствующий хэдер
        if ("HEADER".equals(tokenPlace)) {
            HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
            token = httpServletRequest.getHeader(tokenName);
        } else {
            token = servletRequest.getParameter(tokenName);
        }

        UserSession session = service.checkSession(token);
        if (session != null) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            HttpServletRequest request = (HttpServletRequest) servletRequest;
            LOG.debug("Unauthorized session: id={}, path={}{}{}", token, request.getContextPath(),
                    request.getServletPath(), request.getPathInfo());
            HttpServletResponse response = (HttpServletResponse) servletResponse;
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            if (authHeader != null) {
                response.addHeader("WWW-Authenticate", authHeader);
            }
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        config.setUserDomain(getRequiredParameter(filterConfig, DOMAIN_MARAM));
        config.setAppId(getRequiredParameter(filterConfig, APP_ID_PARAM));
        authHeader = filterConfig.getInitParameter(AUTH_HEADER_PARAM);
        tokenName = coalesce(filterConfig.getInitParameter(ACCESS_TOKEN_NAME_PARAM), DEFAULT_TOKEN_NAME);
        tokenPlace = coalesce(filterConfig.getInitParameter(ACCESS_TOKEN_PLACE_PARAM), DEFAULT_ACCESS_TOKEN_PLACE);
    }

    @Override
    public void destroy() {
        // nothing to do
    }

    private String getRequiredParameter(FilterConfig filterConfig, String paramName) {
        String userDomain = filterConfig.getInitParameter(paramName);
        if (userDomain == null) {
            throw new IllegalStateException("Undefined required init parameter: " + paramName);
        }
        return userDomain;
    }
}

