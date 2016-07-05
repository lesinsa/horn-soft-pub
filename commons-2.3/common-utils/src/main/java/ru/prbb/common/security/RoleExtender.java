package ru.prbb.common.security;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Выдает список ролей пользователя с учетом наследования
 *
 * @author lesinsa on 18.06.2015
 */
public final class RoleExtender {

    private RoleExtender() {
        // nothing to do
    }

    public static Set<Class<? extends AuthenticatedUser>> extend(Collection<Class<? extends AuthenticatedUser>> roles) {
        Set<Class<? extends AuthenticatedUser>> result = new HashSet<>();
        for (Class<? extends AuthenticatedUser> role : roles) {
            extractInterfaces(result, role);
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    private static void extractInterfaces(Set<Class<? extends AuthenticatedUser>> result, Class<? extends AuthenticatedUser> role) {
        result.add(role);
        Class<?>[] interfaces = role.getInterfaces();
        for (Class<?> intf : interfaces) {
            if (AuthenticatedUser.class.isAssignableFrom(intf) && intf != AuthenticatedUser.class) {
                extractInterfaces(result, (Class<? extends AuthenticatedUser>) intf);
            }
        }
    }
}
