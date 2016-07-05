package ru.prbb.common.cdi;

import org.junit.Test;
import ru.prbb.common.cdi.sampleclass.*;

import javax.enterprise.inject.Any;
import javax.enterprise.inject.Default;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author lesinsa on 23.03.14.
 */
public class BeanImplQualifiersTest extends AbstractBeanImplTest {

    @Test
    public void test0() throws Exception {
        BeanImpl<?> sut = getSut(A.class);
        Set<Annotation> qualifiers = sut.getQualifiers();
        Set<Type> classes = getQualifiersTypes(qualifiers);
        assertTypes(classes, Arrays.<Type>asList(Default.class, Any.class));
    }

    @Test
    public void test1() throws Exception {
        BeanImpl<?> sut = getSut(D1.class);
        Set<Annotation> qualifiers = sut.getQualifiers();
        Set<Type> classes = getQualifiersTypes(qualifiers);
        assertTypes(classes, Arrays.<Type>asList(Q1.class, Default.class, Any.class));
    }

    @Test
    public void test2() throws Exception {
        BeanImpl<?> sut = getSut(D2.class);
        Set<Annotation> qualifiers = sut.getQualifiers();
        Set<Type> classes = getQualifiersTypes(qualifiers);
        assertTypes(classes, Arrays.<Type>asList(Default.class, Any.class));
    }

    @Test
    public void test3() throws Exception {
        BeanImpl<?> sut = getSut(D3.class);
        Set<Annotation> qualifiers = sut.getQualifiers();
        Set<Type> classes = getQualifiersTypes(qualifiers);
        assertTypes(classes, Arrays.<Type>asList(Q3.class, Default.class, Any.class));
    }

    @Test
    public void test4() throws Exception {
        BeanImpl<?> sut = getSut(D5.class);
        Set<Annotation> qualifiers = sut.getQualifiers();
        Set<Type> classes = getQualifiersTypes(qualifiers);
        assertTypes(classes, Arrays.<Type>asList(Q1.class, Q4.class, Default.class, Any.class));
    }

    private Set<Type> getQualifiersTypes(Set<Annotation> qualifiers) {
        Set<Type> classes = new HashSet<>();
        Object[] array = qualifiers.toArray();
        for (Object o : array) {
            classes.add(((Annotation) o).annotationType());
        }
        return classes;
    }
}
