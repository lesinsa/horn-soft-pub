package com.horn.common.cdi;

import com.horn.common.logging.App;
import org.slf4j.Logger;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.*;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lesinsa
 */
@SuppressWarnings("CdiManagedBeanInconsistencyInspection")
public class BeanPostProcessorExtension implements Extension {

    private static final Logger LOG = App.LOG;

    private List<BeanDefinition> beanDefinitionList = new ArrayList<>();

    <T> void processAnnotatedType(@Observes ProcessAnnotatedType<T> event) {
        AnnotatedType<T> annotatedType = event.getAnnotatedType();

        Class<T> javaClass = annotatedType.getJavaClass();
        // проверяем на наличие BeanPostProcessor
        Annotation[] annotations = javaClass.getAnnotations();
        for (Annotation annotation : annotations) {
            BeanProcessorBinding binding = annotation.annotationType().getAnnotation(BeanProcessorBinding.class);
            if (binding != null) {
                LOG.debug("Found bean post-processor binding: {}; bean post-processor: {}", javaClass.getName(),
                        binding.provider().getName());
                beanDefinitionList.add(new BeanDefinition(annotatedType, binding, annotation));
                event.veto();
            }
        }

    }

    void afterBeanDiscovery(@Observes AfterBeanDiscovery event, BeanManager bm) {
        LOG.info("After bean discovery");
        for (BeanDefinition beanDefinition : beanDefinitionList) {
            AnnotatedType<?> annotatedType = beanDefinition.getAnnotatedType();
            String className = annotatedType.getJavaClass().getName();
            Class<? extends BeanPostProcessor> provider = beanDefinition.getBinding().provider();
            LOG.info("Add bean definition for class: {}", className);
            try {
                InjectionTarget<?> injectionTarget = bm.createInjectionTarget(annotatedType);
                @SuppressWarnings("unchecked")
                Bean<?> bean = new BeanImpl(annotatedType, injectionTarget, provider,
                        beanDefinition.getBindedAnnotation(), bm);
                event.addBean(bean);
            } catch (BeanDefinitionException e) {
                LOG.error("Definition error catched: type = " + annotatedType, e);
                event.addDefinitionError(e);
            }
        }
    }

    @SuppressWarnings("UnusedParameters")
    void afterDeploymentValidation(@Observes AfterDeploymentValidation event) {
        LOG.info("Extension '{}' is initialized", getClass());
    }

    private static class BeanDefinition {
        private final AnnotatedType<?> annotatedType;
        private final BeanProcessorBinding binding;
        private final Annotation bindedAnnotation;

        private BeanDefinition(AnnotatedType<?> annotatedType, BeanProcessorBinding binding, Annotation bindedAnnotation) {
            this.annotatedType = annotatedType;
            this.binding = binding;
            this.bindedAnnotation = bindedAnnotation;
        }

        public AnnotatedType<?> getAnnotatedType() {
            return annotatedType;
        }

        public BeanProcessorBinding getBinding() {
            return binding;
        }

        public Annotation getBindedAnnotation() {
            return bindedAnnotation;
        }
    }
}
