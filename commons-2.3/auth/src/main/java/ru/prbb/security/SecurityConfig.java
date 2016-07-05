package ru.prbb.security;

import javax.inject.Singleton;

/**
 * @author LesinSA
 */
@Singleton
public class SecurityConfig {
    private String userDomain;
    private String appId;

    public String getUserDomain() {
        return userDomain;
    }

    public void setUserDomain(String userDomain) {
        this.userDomain = userDomain;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }
}
