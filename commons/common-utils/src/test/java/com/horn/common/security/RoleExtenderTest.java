package com.horn.common.security;

import org.junit.Test;

import java.io.Serializable;
import java.util.Collections;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author by lesinsa on 24.08.2015.
 */
public class RoleExtenderTest {

    @Test
    public void test1() throws Exception {
        Set<Class<? extends AuthenticatedUser>> result = RoleExtender.extend(Collections.singleton(Role1.class));
        assertEquals(1, result.size());
        assertEquals(Role1.class, result.iterator().next());
    }

    @Test
    public void test2() throws Exception {
        Set<Class<? extends AuthenticatedUser>> result = RoleExtender.extend(Collections.singleton(Role2.class));
        assertEquals(2, result.size());
        assertTrue(result.contains(Role2.class));
        assertTrue(result.contains(Role1.class));
    }

    @Test
    public void test3() throws Exception {
        Set<Class<? extends AuthenticatedUser>> result = RoleExtender.extend(Collections.singleton(Role3.class));
        assertEquals(3, result.size());
        assertTrue(result.contains(Role3.class));
        assertTrue(result.contains(Role2.class));
        assertTrue(result.contains(Role1.class));
    }

    interface Role1 extends AuthenticatedUser, Serializable {
    }

    interface Role2 extends Role1, Serializable {
    }

    interface Role3 extends Role2, Serializable {
    }
}