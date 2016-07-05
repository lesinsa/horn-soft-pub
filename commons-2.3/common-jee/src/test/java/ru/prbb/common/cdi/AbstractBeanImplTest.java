package ru.prbb.common.cdi;

import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.InjectionTarget;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author lesinsa on 23.03.14.
 */
public class AbstractBeanImplTest {
    protected <T> BeanImpl<?> getSut(Class<T> clazz) {
        AnnotatedType at = mock(AnnotatedType.class);
        when(at.getJavaClass()).thenReturn(clazz);
        return new BeanImpl<>(at, mock(InjectionTarget.class), null, null, null);
    }

    protected void assertTypes(Set<Type> types, Collection<Type> expectedClasses) {
        assertNotNull(types);
        assertEquals(expectedClasses.size(), types.size());
        types.containsAll(expectedClasses);
    }
}
