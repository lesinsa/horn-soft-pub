package com.horn.common.jaxb;

import com.horn.common.cdi.ProgrammaticCdiBeanLookup;
import com.horn.common.exception.StandardException;
import com.horn.common.logging.App;
import org.slf4j.Logger;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.*;
import javax.xml.bind.JAXBException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author lesinsa on 20.05.2015.
 */
public class SerializerExtension implements Extension {
    private static final Logger LOG = App.LOG;

    private List<Bean<?>> repositoriesList;

    public SerializerExtension() {
        repositoriesList = new ArrayList<>();
    }

    <T> void processBean(@Observes ProcessBean<T> event) {
        Annotated annotated = event.getAnnotated();
        ContextRepository annotation = annotated.getAnnotation(ContextRepository.class);
        if (annotation != null) {
            Bean<T> bean = event.getBean();
            LOG.info("Found serialization context repository: {}", bean);
            repositoriesList.add(bean);
        }
    }

    void afterDeploymentValidation(@Observes AfterDeploymentValidation event, BeanManager bm) throws JAXBException, InvocationTargetException, IllegalAccessException {
        if (!repositoriesList.isEmpty()) {
            ContextHolderBean holderBean = ProgrammaticCdiBeanLookup.getBeanInstance(bm, ContextHolderBean.class);

            for (Bean<?> bean : repositoriesList) {
                try {
                    LOG.info("Processing serialization context repository: {}", bean);
                    CreationalContext<?> creationalContext = bm.createCreationalContext(bean);

                    Class<?> clazz = bean.getBeanClass();
                    Object reference = bm.getReference(bean, clazz, creationalContext);

                    Method[] methods = clazz.getDeclaredMethods();
                    for (Method method : methods) {
                        if (method.getAnnotation(ContextDef.class) != null) {
                            DataFromMethod dataFromMethod = new DataFromMethod(bean, reference, method).invoke();
                            ContextDef contextDef = dataFromMethod.getContextDef();
                            Class[] classes = dataFromMethod.getClasses();
                            holderBean.addContext(contextDef.name(), contextDef.type(), classes);
                        }
                    }
                } catch (Exception e) {
                    LOG.error("", e);
                    event.addDeploymentProblem(e);
                }
            }
        }
    }

    private void validateMethod(Bean<?> bean, Method method) throws DefinitionException {
        if (method.getParameterTypes().length != 0) {
            LOG.error("Context definition method must not has parameters: " +
                    "bean = {}, method = {}", bean, method);
            throw new DefinitionException("Error processing serializer context repository: " + bean);
        }
    }

    private class DataFromMethod {
        private Bean<?> bean;
        private Object reference;
        private Method method;
        private ContextDef contextDef;
        private Class[] classes;

        public DataFromMethod(Bean<?> bean, Object reference, Method method) {
            this.bean = bean;
            this.reference = reference;
            this.method = method;
        }

        public ContextDef getContextDef() {
            return contextDef;
        }

        public Class[] getClasses() {
            return classes;
        }

        public DataFromMethod invoke() throws InvocationTargetException, IllegalAccessException, DefinitionException {
            contextDef = method.getAnnotation(ContextDef.class);
            if (contextDef != null) {
                // проверяем, что метод определен корректно
                validateMethod(bean, method);
            }

            Class<?> returnType = method.getReturnType();
            Object result = method.invoke(reference);
            if (returnType.isArray()) {
                classes = (Class[]) result;
            } else if (Collection.class.isAssignableFrom(returnType)) {
                Collection collection = (Collection) result;
                Object[] returnValue = collection.toArray();
                this.classes = new Class[collection.size()];
                for (int i = 0; i < this.classes.length; i++) {
                    this.classes[i] = (Class) returnValue[i];
                }
            } else if (result.getClass() == Class.class) {
                classes = new Class[]{(Class) result};
            } else {
                throw new IllegalStateException("Unsupported return type: " + returnType);
            }
            return this;
        }
    }

    private class DefinitionException extends StandardException {
        public DefinitionException(String message) {
            super(message);
        }
    }
}
