package ru.prbb.common.cdi;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.lang.annotation.Annotation;
import java.util.Set;

/**
 * @author lesinsa on 13.04.14.
 */
public final class ProgrammaticCdiBeanLookup {

    private ProgrammaticCdiBeanLookup() {
        // nothing to do
    }

    public static <T> T getBeanInstance(Class<T> clazz, Annotation... qualifiers) {
        try {
            BeanManager beanManager = InitialContext.doLookup("java:comp/BeanManager");
            return getBeanInstance(beanManager, clazz, qualifiers);
        } catch (NamingException | BeanDefinitionException e) {
            throw new IllegalStateException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T getBeanInstance(BeanManager bm, Class<T> clazz, Annotation... qualifiers) {
        Set<Bean<?>> beans = bm.getBeans(clazz, qualifiers);
        if (beans.isEmpty()) {
            throw new BeanDefinitionException("Bean implementation is not found for: " + clazz.getName());
        }
        if (beans.size() > 1) {
            throw new BeanDefinitionException("More than one bean implementation is found for: " + clazz.getName());
        }
        Bean<T> bean = (Bean<T>) beans.iterator().next();
        CreationalContext<T> creationalContext = bm.createCreationalContext(bean);
        return (T) bm.getReference(bean, clazz, creationalContext);
    }

    public static <T> T getBeanInstance(BeanManager bm, Class<T> clazz, Set<Annotation> qualifiers) {
        Annotation[] annotations = takeAsArray(qualifiers);
        return getBeanInstance(bm, clazz, annotations);
    }

    private static Annotation[] takeAsArray(Set<Annotation> qualifiers) {
        Annotation[] annotations = new Annotation[qualifiers.size() - 2];
        int i = 0;
        for (Annotation qualifier : qualifiers) {
            annotations[i++] = qualifier;
        }
        return annotations;
    }
}
