package core.mvc;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HandlerExecution {
	private static final Logger logger = LoggerFactory.getLogger(HandlerExecution.class);
	
	private Object declaredObject;
	private Method method;

	public HandlerExecution(Object declaredObject, Method method) {
		this.declaredObject = declaredObject;
		this.method = method;
	}

	public ModelAndView execute(HttpServletRequest request, HttpServletResponse response) {
		try {
			logger.debug("call execute!");
			return (ModelAndView)method.invoke(declaredObject, request, response);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			logger.error("{} method invoke fail. error message : {}", method, e.getMessage());
			throw new RuntimeException(e);
		}
	}
}
