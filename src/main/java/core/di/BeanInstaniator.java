package core.di;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Set;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class BeanInstaniator {
	private static final Logger logger = LoggerFactory.getLogger(BeanInstaniator.class);
	
	private Reflections reflections;
	
	public BeanInstaniator(String... basePackage) {
		reflections = new Reflections(basePackage);
	}
	
	public Map<Class<?>, Object> getBeans(@SuppressWarnings("unchecked") Class<? extends Annotation>... annotations) {
		Set<Class<?>> preInitiatedBeans = getTypesAnnotatedWith(annotations);
		Map<Class<?>, Object> beans = instantiateBeforeBeans(preInitiatedBeans);
		FieldInjector fieldInjector = new FieldInjector(beans);
		fieldInjector.inject();
		MethodInjector methodInjector = new MethodInjector(beans);
		methodInjector.inject();
		return beans;
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
