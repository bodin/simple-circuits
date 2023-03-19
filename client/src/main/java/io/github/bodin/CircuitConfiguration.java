package io.github.bodin;

import io.github.bodin.annotation.Circuit;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.ClassUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

@Configuration
@Import(CircuitConfiguration.CircuitProxyRegistrar.class)
public class CircuitConfiguration {
    private final static Logger log = LoggerFactory.getLogger("mckesson.config.feature");

    @Bean(name = "circuitProxyFactory")
    public CircuitProxyBeanFactory circuitProxyFactory() {
        return new CircuitProxyBeanFactory();
    }


    public static class ClassPathScanner extends ClassPathScanningCandidateComponentProvider {

        public ClassPathScanner(final boolean useDefaultFilters) {
            super(useDefaultFilters);
        }

        @Override
        protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
            return beanDefinition.getMetadata().isIndependent();
        }
    }

    public static class CircuitProxyRegistrar implements ImportBeanDefinitionRegistrar, BeanClassLoaderAware {

        private final ClassPathScanner classpathScanner;
        private ClassLoader classLoader;

        public CircuitProxyRegistrar() {
            classpathScanner = new ClassPathScanner(false);
            classpathScanner.addIncludeFilter(new AnnotationTypeFilter(io.github.bodin.annotation.Circuit.class));
        }

        @Override
        public void setBeanClassLoader(ClassLoader classLoader) {
            this.classLoader = classLoader;
        }

        @Override
        public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
            try {
                for (BeanDefinition beanDefinition : classpathScanner.findCandidateComponents("")) {

                    Class<?> clazz = Class.forName(beanDefinition.getBeanClassName());
                    io.github.bodin.annotation.Circuit c = clazz.getAnnotation(io.github.bodin.annotation.Circuit.class);
                    String beanName = ClassUtils.getShortNameAsProperty(clazz);

                    GenericBeanDefinition proxy = new GenericBeanDefinition();
                    proxy.setBeanClass(clazz);

                    ConstructorArgumentValues args = new ConstructorArgumentValues();
                    args.addGenericArgumentValue(classLoader);
                    args.addGenericArgumentValue(clazz);
                    proxy.setConstructorArgumentValues(args);

                    proxy.setFactoryBeanName("circuitProxyFactory");
                    proxy.setFactoryMethodName("createCircuitProxy");

                    log.info("[CIRCUIT] BEAN {} {}", beanName, Arrays.asList(c.value()));

                    registry.registerBeanDefinition(beanName, proxy);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static class CircuitProxy implements InvocationHandler {

        private final io.github.bodin.Circuit circuit;

        public CircuitProxy(io.github.bodin.Circuit circuit) {
            this.circuit = circuit;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            return method.invoke(circuit, args);
        }
    }

    public static class CircuitProxyBeanFactory {

        @Autowired
        private CircuitService service;

        @SuppressWarnings("unchecked")
        public <WS> WS createCircuitProxy(ClassLoader classLoader, Class<WS> clazz) {
            String circuitName = clazz.getAnnotation(Circuit.class).value();
            io.github.bodin.Circuit circuit = service.create(circuitName);
            return (WS) Proxy.newProxyInstance(classLoader, new Class[]{clazz}, new CircuitProxy(circuit));
        }
    }
}