package core.mvc;

import java.util.HashMap;
import java.util.Map;

import next.controller.ListController;
import next.controller.ShowController;
import next.controller.addAnswer;
import next.controller.addQuestion;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestMapping {
	private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);
	private Map<String, Controller> mappings = new HashMap<String, Controller>();
	
	public void initMapping() {
		mappings.put("/list.next", new ListController());
		mappings.put("/show.next", new ShowController());
		mappings.put("/form.next", new ForwardController("form.jsp"));
		mappings.put("/save.next", new addQuestion());
		mappings.put("/api/addanswer.next", new addAnswer());
		mappings.put("/deleteanswer.next", new DeleteAnswer());
		mappings.put("/api/list.next", new Moblie());
//		mappings.put("/editForm.next", new ForwardController("form.jsp"));
		mappings.put("/editForm.next", new ForwardController("form.jsp"));
		mappings.put("/updateForm.next", new UpdateForm());
		mappings.put("/delete.next", new DeleteQuestion());
		mappings.put("/api/delete.next", new apiDelete());
		
		logger.info("Initialized Request Mapping!");
	}

	public Controller findController(String url) {
		return mappings.get(url);
	}

	void put(String url, Controller controller) {
		mappings.put(url, controller);
	}

}
