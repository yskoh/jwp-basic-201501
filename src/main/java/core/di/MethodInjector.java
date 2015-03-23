package core.di;

import static org.reflections.ReflectionUtils.getAllMethods;
import static org.reflections.ReflectionUtils.withAnnotation;
import static org.reflections.ReflectionUtils.withReturnType;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;
import com.sun.xml.internal.txw2.IllegalSignatureException;

import core.mvc.annotation.Inject;

public class MethodInjector {
	private static final Logger logger = LoggerFactory.getLogger(MethodInjector.class);
	
	private Map<Class<?>, Object> beans;

	public MethodInjector(Map<Class<?>, Object> beans) {
		this.beans = beans;
	}

	public void inject() {
		Set<Method> injectedMethods = getInjectedMethods(beans.keySet());
		injectAllMethods(beans, injectedMethods);
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
				Class<?> injectedType = findInjectedType(beans, parameter);
				Object injected = beans.get(injectedType);
				if (injected == null) {
					throw new IllegalSignatureException(parameter.getType() + "인스턴스가 존재하지 않습니다.");
				}
				try {
					method.invoke(beans.get(method.getDeclaringClass()), injected);
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					logger.error(e.getMessage());
				}
			}
		}
	}
	
	private Class<?> findInjectedType(Map<Class<?>, Object> beans, Parameter parameter) {
		Class<?> parameterClazz = parameter.getType();
		logger.debug("parameter type : {}", parameterClazz);
		if (!parameterClazz.isInterface()) {
			return parameterClazz;
		}
		return findConcreteClass(beans, parameterClazz);
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
}
