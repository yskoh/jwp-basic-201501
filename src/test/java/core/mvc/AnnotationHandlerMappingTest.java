package core.mvc;

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
		execution.execute(request, response);
	}

}
