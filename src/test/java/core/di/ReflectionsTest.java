package core.di;

import static org.reflections.ReflectionUtils.getAllMethods;
import static org.reflections.ReflectionUtils.withAnnotation;
import static org.reflections.ReflectionUtils.withReturnType;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

import core.mvc.annotation.Bean;
import core.mvc.annotation.Inject;

public class ReflectionsTest {
	private static final Logger logger = LoggerFactory.getLogger(ReflectionsTest.class);

	private Reflections reflections;

	@Before
	public void setup() {
		reflections = new Reflections("core.di");
	}

	@Test
	public void getTypesAnnotatedWith() throws Exception {
		Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(Bean.class);
		for (Class<?> clazz : annotated) {
			logger.debug("class name : {}", clazz.getName());
		}
	}

	@SuppressWarnings("unchecked")
	@Test
	public void getMethodsAnnotatedWith() throws Exception {
		Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(Bean.class);
		Map<Class<?>, Object> instantiatedBeans = instantiateBeforeBeans(annotated);
		for (Class<?> clazz : annotated) {
			logger.debug("class name : {}", clazz.getName());
			
			Set<Method> injectMethods =  getAllMethods(clazz, withAnnotation(Inject.class), withReturnType(void.class));
			for (Method method : injectMethods) {
				Parameter[] parameters = method.getParameters();
				for (Parameter parameter : parameters) {
					logger.debug("parameter type : {}", parameter.getType());
					method.invoke(instantiatedBeans.get(clazz), instantiatedBeans.get(parameter.getType()));
				}
			}
		}
	}
	
	private Map<Class<?>, Object> instantiateBeforeBeans(Set<Class<?>> beansBeforeInject) {
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
