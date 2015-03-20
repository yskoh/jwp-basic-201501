package core.mvc;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

public class AnnotationHandlerMappingTest {
	private AnnotationHandlerMapping handlerMapping;
	
	@Before
	public void setup() {
		handlerMapping = new AnnotationHandlerMapping("core.di");
		handlerMapping.initialize();
	}

	@Test
	public void getHandler() {
		MockHttpServletRequest request = new MockHttpServletRequest("GET", "/users/findUserId?userId=javajigi");
		MockHttpServletResponse response = new MockHttpServletResponse();
		HandlerExecution execution = handlerMapping.getHandler(request);
		ModelAndView mav = execution.execute(request, response);
	}

}
