package ru.prbb.common.cdi;

import org.junit.Test;
import ru.prbb.common.cdi.sampleclass.*;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Set;

/**
 * @author lesinsa on 23.03.14.
 */
public class BeanImplTypesTest extends AbstractBeanImplTest {

    @Test
    public void test1() throws Exception {
        BeanImpl<?> sut = getSut(A.class);
        Set<Type> types = sut.getTypes();
        assertTypes(types, Arrays.<Type>asList(Object.class, A.class));
    }

    @Test
    public void test2() throws Exception {
        BeanImpl<?> sut = getSut(BA.class);
        Set<Type> types = sut.getTypes();
        assertTypes(types, Arrays.<Type>asList(Object.class, A.class, BA.class));
    }

    @Test
    public void test3() throws Exception {
        BeanImpl<?> sut = getSut(BA.class);
        Set<Type> types = sut.getTypes();
        assertTypes(types, Arrays.<Type>asList(Object.class, A.class, BA.class));
    }

    @Test
    public void test4() throws Exception {
        BeanImpl<?> sut = getSut(C1.class);
        Set<Type> types = sut.getTypes();
        assertTypes(types, Arrays.<Type>asList(Object.class, C1.class, B2.class, Intf1.class));
    }

    @Test
    public void test5() throws Exception {
        BeanImpl<?> sut = getSut(C2.class);
        Set<Type> types = sut.getTypes();
        assertTypes(types, Arrays.<Type>asList(Object.class, C2.class, BA.class, A.class, Intf1.class, Intf2.class));
    }
}
