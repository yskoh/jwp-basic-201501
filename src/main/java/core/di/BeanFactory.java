package core.di;

import java.lang.annotation.Annotation;
import java.util.Map;

public class BeanFactory {
	private Map<Class<?>, Object> beans;

	private String basePackage;
	
	public BeanFactory(String basePackage) {
		this.basePackage = basePackage;
	}
	
	public void initialize(@SuppressWarnings("unchecked") Class<? extends Annotation>... annotations) {
		BeanInstaniator beanInstaniator = new BeanInstaniator(basePackage);
		beans = beanInstaniator.getBeans(annotations);
	}

	@SuppressWarnings("unchecked")
	public <T> T getBean(Class<T> requiredType) {
		return (T)beans.get(requiredType);
	}
}
