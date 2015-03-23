package core.di;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import core.mvc.annotation.Bean;
import core.mvc.annotation.Controller;
import core.mvc.annotation.Inject;

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
	
	@Test
	public void injectMethod() throws Exception {
		Set<Class<?>> preInitiatedBeans = Sets.newHashSet(MyBean.class);
		Set<Method> methods = beanInstaniator.getInjectedMethods(preInitiatedBeans);
		assertThat(methods.size(), is(2));
	}
	
	@Test
	public void injectAllMethods() throws Exception {
		Map<Class<?>, Object> beans = Maps.newHashMap();
		beans.put(MyBean.class, new MyBean());
		beans.put(InjectedBean.class, new InjectedBean());
		beans.put(MyClass.class, new MyClass());
		
		Set<Class<?>> preInitiatedBeans = Sets.newHashSet(MyBean.class);
		Set<Method> injectedMethods = beanInstaniator.getInjectedMethods(preInitiatedBeans);
		
		beanInstaniator.injectAllMethods(beans, injectedMethods);
		
		MyBean myBean = (MyBean)beans.get(MyBean.class);
		assertThat(myBean.getInjectedBean(), is((InjectedBean)beans.get(InjectedBean.class)));
	}
	
	@Test
	public void findConcreteClass() throws Exception {
		Map<Class<?>, Object> beans = Maps.newHashMap();
		beans.put(InjectedBean.class, new InjectedBean());
		beans.put(MyClass.class, new MyClass());
		
		Class<?> concreteClazz = beanInstaniator.findConcreteClass(beans, MyInterface.class);
		assertThat(concreteClazz.getName(), is("core.di.BeanInstaniatorTest$MyClass"));
	}
	
	class MyBean {
		private InjectedBean injectedBean;
		private MyInterface myInterface;
		
		@Inject
		public void setInjectedBean(InjectedBean injectedBean) {
			this.injectedBean = injectedBean;
		}
		
		@Inject
		public void setMyInterface(MyInterface myInterface) {
			this.myInterface = myInterface;
		}
		
		public InjectedBean getInjectedBean() {
			return injectedBean;
		}
		
		public MyInterface getMyInterface() {
			return myInterface;
		}
	};
	
	private class InjectedBean {
	};
	
	interface MyInterface {};
	
	class MyClass implements MyInterface {
		
	}
}
