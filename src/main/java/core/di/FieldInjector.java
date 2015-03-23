package core.di;

import static org.reflections.ReflectionUtils.getAllFields;
import static org.reflections.ReflectionUtils.withAnnotation;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;
import com.sun.xml.internal.txw2.IllegalSignatureException;

import core.mvc.annotation.Inject;

public class FieldInjector {
	private static final Logger logger = LoggerFactory.getLogger(FieldInjector.class);
	
	private Map<Class<?>, Object> beans;

	public FieldInjector(Map<Class<?>, Object> beans) {
		this.beans = beans;
	}

	public void inject() {
		Set<Field> injectedFields = getInjectedFields(beans.keySet());
		injectAllFields(beans, injectedFields);
	}
	
	void injectAllFields(Map<Class<?>, Object> beans, Set<Field> injectedFields) {
		for (Field field : injectedFields) {
			logger.debug("invoke field : {}", field);
			Class<?> injectedType = findInjectedType(beans, field.getType());
			Object injected = beans.get(injectedType);
			if (injected == null) {
				throw new IllegalSignatureException(injectedType + "인스턴스가 존재하지 않습니다.");
			}
			try {
				field.setAccessible(true);
				field.set(beans.get(field.getDeclaringClass()), injected);
			} catch (IllegalAccessException | IllegalArgumentException e) {
				logger.error(e.getMessage());
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	Set<Field> getInjectedFields(Set<Class<?>> beans) {
		Set<Field> injectedFields = Sets.newHashSet();
		for (Class<?> clazz : beans) {
			injectedFields.addAll(getAllFields(clazz, withAnnotation(Inject.class)));
		}
		return injectedFields;
	}
	
	private Class<?> findInjectedType(Map<Class<?>, Object> beans, Class<?> type) {
		logger.debug("parameter type : {}", type);
		if (!type.isInterface()) {
			return type;
		}
		return findConcreteClass(beans, type);
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
