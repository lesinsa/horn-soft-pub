package com.horn.common.security;

import java.io.Serializable;

/**
 * @author lesinsa on 17.06.2015
 */
public abstract class UserContext<T extends Serializable> implements Serializable {
    private T user;

    /**
     * Требуется реализовать в прикладном коде
     *
     * @param role класс роли
     * @return логический флаг, входит в пользователь в роль
     */
    public abstract boolean isUserInRole(Class<? extends AuthenticatedUser> role);

    public boolean isAuthenticated() {
        return user != null;
    }

    public T getUser() {
        return user;
    }

    public void setUser(T user) {
        this.user = user;
    }
}
