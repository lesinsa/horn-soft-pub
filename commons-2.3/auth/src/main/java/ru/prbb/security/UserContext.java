package ru.prbb.security;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;

/**
 * @author LesinSA
 */
@RequestScoped
public class UserContext {

    private UserSession userSession;

    @Current
    @RequestScoped
    @Produces
    public UserSession getUserSession() {
        if (userSession == null) {
            throw new IllegalStateException("User session is not initialized");
        }
        return userSession;
    }

    public void setUserSession(@Observes @Current UserSession userSession) {
        this.userSession = userSession;
    }
}
