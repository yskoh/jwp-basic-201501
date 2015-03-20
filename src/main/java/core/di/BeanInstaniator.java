package core.di;

import static org.reflections.ReflectionUtils.getAllMethods;
import static org.reflections.ReflectionUtils.withAnnotation;
import static org.reflections.ReflectionUtils.withReturnType;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;
import java.util.Set;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.sun.xml.internal.txw2.IllegalSignatureException;

import core.mvc.annotation.Inject;

public class BeanInstaniator {
	private static final Logger logger = LoggerFactory.getLogger(BeanInstaniator.class);
	
	private Reflections reflections;

	public BeanInstaniator(String basePackage) {
		reflections = new Reflections(basePackage);
	}
	
	public Map<Class<?>, Object> getBeans(@SuppressWarnings("unchecked") Class<? extends Annotation>... annotations) {
		Set<Class<?>> preInitiatedBeans = getTypesAnnotatedWith(annotations);
		Map<Class<?>, Object> beans = instantiateBeforeBeans(preInitiatedBeans);
		Set<Method> injectedMethods = getInjectedMethods(preInitiatedBeans);
		injectAllMethods(beans, injectedMethods);
		return beans;
	}
	
	@SuppressWarnings("unchecked")
	Set<Method> getInjectedMethods(Set<Class<?>> beans) {
		Set<Method> injectedMethods = Sets.newHashSet();
		for (Class<?> clazz : beans) {
			injectedMethods.addAll(getAllMethods(clazz, withAnnotation(Inject.class), withReturnType(void.class)));
		}
		return injectedMethods;
	}
	
	void injectAllMethods(Map<Class<?>, Object> beans, Set<Method> injectedMethods) {
		for (Method method : injectedMethods) {
			logger.debug("invoke method : {}", method);
			Parameter[] parameters = method.getParameters();
			for (Parameter parameter : parameters) {
				logger.debug("parameter type : {}", parameter.getType());
				Object injected = beans.get(parameter.getType());
				if (injected == null) {
					throw new IllegalSignatureException(parameter.getType() + "인스턴스가 존재하지 않습니다.");
				}
				
				try {
					method.invoke(beans.get(method.getDeclaringClass()), beans.get(parameter.getType()));
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					logger.error(e.getMessage());
				}
			}
		}
	}
	
	Set<Class<?>> getTypesAnnotatedWith(@SuppressWarnings("unchecked") Class<? extends Annotation>... annotations) {
		Set<Class<?>> beans = Sets.newHashSet();
		for (Class<? extends Annotation> annotation : annotations) {
			beans.addAll(reflections.getTypesAnnotatedWith(annotation));
		}
		return beans;
	}
	
	Map<Class<?>, Object> instantiateBeforeBeans(Set<Class<?>> beansBeforeInject) {
		Map<Class<?>, Object> beans = Maps.newHashMap();
		for (Class<?> clazz : beansBeforeInject) {
			try {
				beans.put(clazz, clazz.newInstance());
			} catch (InstantiationException | IllegalAccessException e) {
				logger.error(e.getMessage());
			}
		}
		
		return beans;
	}
}
