package core.mvc;

import static org.reflections.ReflectionUtils.getAllMethods;
import static org.reflections.ReflectionUtils.withAnnotation;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import core.di.BeanInstaniator;
import core.mvc.annotation.Bean;
import core.mvc.annotation.Controller;
import core.mvc.annotation.RequestMapping;

public class AnnotationHandlerMapping {
	private static final Logger logger = LoggerFactory.getLogger(AnnotationHandlerMapping.class);
	
	private String basePackage;
	
	private Map<String, HandlerExecution> handlerExecutions;
	
	public AnnotationHandlerMapping(String basePackage) {
		this.basePackage = basePackage;
		this.handlerExecutions= Maps.newHashMap(); 
	}
	
	@SuppressWarnings("unchecked")
	public void initialize() {
		BeanInstaniator beanInstaniator = new BeanInstaniator(basePackage);
		Map<Class<?>, Object> beans = beanInstaniator.getBeans(Bean.class, Controller.class);
		Set<Method> methods = getRequestMappingMethods(beans.keySet());
		for (Method method : methods) {
			RequestMapping rm = method.getAnnotation(RequestMapping.class);
			logger.debug("register handlerExecution : url is {}, method is {}", rm.value(), method);
			handlerExecutions.put(rm.value(), new HandlerExecution(beans.get(method.getDeclaringClass()), method));
		}
	}
	
	Set<Method> getRequestMappingMethods(Set<Class<?>> beans) {
		Set<Method> requestMappingMethods = Sets.newHashSet();
		for (Class<?> clazz : beans) {
			requestMappingMethods.addAll(getAllMethods(clazz, withAnnotation(RequestMapping.class)));
		}
		return requestMappingMethods;
	}

	public HandlerExecution getHandler(HttpServletRequest request) {
		String requestUri = urlExceptParameter(request.getRequestURI());
		logger.debug("requestUri : {}", requestUri);
		return handlerExecutions.get(requestUri);
	}
	
	String urlExceptParameter(String forwardUrl) {
		int index = forwardUrl.indexOf("?");
		if (index > 0) {
			return forwardUrl.substring(0, index);
		}
		
		return forwardUrl;
	}
}
