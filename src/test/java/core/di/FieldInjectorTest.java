package core.di;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Map;

import org.junit.Test;

import com.google.common.collect.Maps;

import core.mvc.annotation.Inject;

public class FieldInjectorTest {
	@Test
	public void inject() throws Exception {
		Map<Class<?>, Object> beans = Maps.newHashMap();
		beans.put(InjectedBean.class, new InjectedBean());
		beans.put(MyClass.class, new MyClass());
		beans.put(InjectBean.class, new InjectBean());
		
		FieldInjector fieldInjector = new FieldInjector(beans);
		fieldInjector.inject();
		
		InjectBean injectBean = (InjectBean)beans.get(InjectBean.class);
		assertThat(injectBean.getInjectedBean(), is((InjectedBean)beans.get(InjectedBean.class)));
		assertThat(injectBean.getMyInterface(), is((MyInterface)beans.get(MyClass.class)));
	}
	
	public class InjectBean {
		@Inject
		private InjectedBean injectedBean;
		
		@Inject
		private MyInterface myInterface;
		
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
