package core.di;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import core.mvc.annotation.Bean;
import core.mvc.annotation.Controller;

public class BeanInstaniatorTest {
	private static final Logger logger = LoggerFactory.getLogger(BeanInstaniatorTest.class);
	
	private BeanInstaniator beanInstaniator;

	@Before
	public void setup() {
		beanInstaniator = new BeanInstaniator("core.di");
	}
	
	@Test
	@SuppressWarnings("unchecked")
	public void getBeans() throws Exception {
		Map<Class<?>, Object> beans = beanInstaniator.getBeans(Bean.class, Controller.class);
		for (Class<?> bean : beans.keySet()) {
			logger.debug("bean : {}", bean);
		}
	}
}
