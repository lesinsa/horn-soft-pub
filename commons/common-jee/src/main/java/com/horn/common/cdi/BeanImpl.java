package com.horn.common.cdi;

import javax.enterprise.context.Dependent;
import javax.enterprise.context.NormalScope;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.Alternative;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.spi.*;
import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Qualifier;
import javax.inject.Scope;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.*;

/**
 * @author lesinsa on 23.03.14.
 */
public class BeanImpl<T> implements Bean<T>, PassivationCapable {

    public static final Class<? extends Annotation> DEFAULT_SCOPE = Dependent.class;
    private final Set<Type> types;
    private final Set<Annotation> qualifiers;
    private final Class<? extends Annotation> scope;
    private final InjectionTarget<T> injectionTarget;
    private final AnnotatedType<T> annotatedType;
    private final BeanManager beanManager;
    private Class<? extends BeanPostProcessor> processorType;
    private Annotation boundAnnotation;
    private String id;

    @SuppressWarnings("unchecked")
    public BeanImpl(AnnotatedType<T> annotatedType, InjectionTarget<?> injectionTarget,
                    Class<? extends BeanPostProcessor> processorType, Annotation boundAnnotation, BeanManager beanManager) throws BeanDefinitionException {
        this.annotatedType = annotatedType;
        this.processorType = processorType;
        this.boundAnnotation = boundAnnotation;
        this.beanManager = beanManager;
        this.injectionTarget = (InjectionTarget<T>) injectionTarget;
        this.types = new HashSet<>();
        this.qualifiers = new HashSet<>();
        this.scope = inernalGetScope(annotatedType.getJavaClass());
        processTypes(annotatedType.getJavaClass());
        processQualifiers(annotatedType.getJavaClass());
    }

    private Class<? extends Annotation> inernalGetScope(Class<T> beanClass) throws BeanDefinitionException {
        Class<? extends Annotation> result = null;
        Annotation[] annotations = beanClass.getAnnotations();
        for (Annotation annotation : annotations) {
            Class<? extends Annotation> annotationType = annotation.annotationType();
            if (annotationType.getAnnotation(NormalScope.class) != null || annotationType.getAnnotation(Scope.class) != null) {
                if (result != null) {
                    throw new BeanDefinitionException("More than one scope annotation on class: " + beanClass.getName());
                }
                result = annotationType;
            }
        }
        return result != null ? result : DEFAULT_SCOPE;
    }

    private void processQualifiers(Class<?> clazz) {
        qualifiers.add(new AnnotationLiteral<Default>() {
        });
        qualifiers.add(new AnnotationLiteral<Any>() {
        });
        Annotation[] annotations = clazz.getAnnotations();
        for (Annotation annotation : annotations) {
            if (isQualifier(annotation)) {
                qualifiers.add(annotation);
            }
        }
    }

    private boolean isQualifier(Annotation annotation) {
        Class<? extends Annotation> annotationType = annotation.annotationType();
        return annotationType.getAnnotation(Qualifier.class) != null;
    }

    private void processTypes(Class<?> clazz) {
        types.add(Object.class);
        processType(clazz);
    }

    private void processType(Class<?> clazz) {
        types.add(clazz);
        Class<?>[] interfaces = clazz.getInterfaces();
        types.addAll(Arrays.asList(interfaces));
        if (clazz.getSuperclass() != null) {
            processType(clazz.getSuperclass());
        }
    }

    @Override
    public Set<Type> getTypes() {
        return types;
    }

    @Override
    public Set<Annotation> getQualifiers() {
        return qualifiers;
    }

    @Override
    public Class<? extends Annotation> getScope() {
        return scope;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public Set<Class<? extends Annotation>> getStereotypes() {
        return Collections.emptySet();
    }

    @Override
    public Class<?> getBeanClass() {
        return annotatedType.getJavaClass();
    }

    @Override
    public boolean isAlternative() {
        return annotatedType.isAnnotationPresent(Alternative.class);
    }

    @Override
    public boolean isNullable() {
        return getScope() == Dependent.class;
    }

    @Override
    public Set<InjectionPoint> getInjectionPoints() {
        return Collections.emptySet();
    }

    @SuppressWarnings("unchecked")
    @Override
    public T create(CreationalContext<T> context) {
        T instance = injectionTarget.produce(context);
        injectionTarget.inject(instance, context);
        injectionTarget.postConstruct(instance);
        BeanPostProcessor processor = ProgrammaticCdiBeanLookup.getBeanInstance(beanManager, processorType);
        processor.processBean(boundAnnotation, instance);
        return instance;
    }

    @Override
    public void destroy(T instance, CreationalContext<T> context) {
        injectionTarget.preDestroy(instance);
        injectionTarget.dispose(instance);
        context.release();
    }

    @Override
    public String toString() {
        return annotatedType.getJavaClass().toString();
    }

    @Override
    public String getId() {
        if (id == null) {
            id = UUID.randomUUID().toString();
        }
        return id;
    }
}
