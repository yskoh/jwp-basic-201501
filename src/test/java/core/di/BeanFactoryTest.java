package core.di;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import core.mvc.annotation.Bean;
import core.mvc.annotation.Controller;

public class BeanFactoryTest {
	private BeanFactory beanFactory;

	@Before
	@SuppressWarnings("unchecked")
	public void setup() {
		beanFactory = new BeanFactory("core.di");
		beanFactory.initialize(Bean.class, Controller.class);
	}

	@Test
	public void getBean() throws Exception {
		MyService myService = beanFactory.getBean(MyService.class);
		assertThat(myService, is(notNullValue()));
	}
}
