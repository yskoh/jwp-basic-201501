package core.mvc;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import next.controller.AddAnswerController;
import next.controller.ApiDeleteController;
import next.controller.ApiListController;
import next.controller.DeleteController;
import next.controller.ListController;
import next.controller.SaveController;
import next.controller.ShowController;
import next.dao.AnswerDao;
import next.dao.JdbcAnswerDao;
import next.dao.JdbcQuestionDao;
import next.dao.QuestionDao;
import next.service.QnaService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import core.jdbc.ConnectionManager;
import core.jdbc.JdbcTemplate;

public class HandlerMapping {
	private static final Logger logger = LoggerFactory.getLogger(HandlerMapping.class);
	
	private Map<String, Controller> mappings = new HashMap<String, Controller>();
	
	public void initMapping() {
		DataSource dataSource = ConnectionManager.getDataSource();
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		
		AnswerDao answerDao = new JdbcAnswerDao(jdbcTemplate);
		QuestionDao questionDao = new JdbcQuestionDao(jdbcTemplate, answerDao);
		
		QnaService qnaService = new QnaService(questionDao, answerDao);
		
		mappings.put("/list.next", new ListController(questionDao));
		mappings.put("/show.next", new ShowController(questionDao, answerDao));
		mappings.put("/form.next", new ForwardController("form.jsp"));
		mappings.put("/save.next", new SaveController(questionDao));
		mappings.put("/delete.next", new DeleteController(qnaService));
		mappings.put("/api/addanswer.next", new AddAnswerController(questionDao, answerDao));
		mappings.put("/api/delete.next", new ApiDeleteController(qnaService));
		mappings.put("/api/list.next", new ApiListController(questionDao));
		
		logger.info("Initialized Mapping Completed!");
	}

	public Controller findController(String url) {
		return mappings.get(url);
	}

	void put(String url, Controller controller) {
		mappings.put(url, controller);
	}

}
