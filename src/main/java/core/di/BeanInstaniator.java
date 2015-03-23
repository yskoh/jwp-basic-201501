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
				Object injected = findInjectedObject(beans, parameter);
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

	private Object findInjectedObject(Map<Class<?>, Object> beans, Parameter parameter) {
		Class<?> parameterClazz = parameter.getType();
		logger.debug("parameter type : {}", parameterClazz);
		if (parameterClazz.isInterface()) {
			return beans.get(findConcreteClass(beans, parameterClazz));
		}
		return beans.get(parameterClazz);
	}
	
	Class<?> findConcreteClass(Map<Class<?>, Object> beans, Class<?> interfaceClazz) {
		Set<Class<?>> keys = beans.keySet();
		for (Class<?> clazz : keys) {
			Set<Class<?>> interfaces = Sets.newHashSet(clazz.getInterfaces());
			if (interfaces.contains(interfaceClazz)) {
				return clazz;
			}
		}
		return null;
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
