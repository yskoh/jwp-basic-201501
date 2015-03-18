package next.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import next.ExistedAnotherUserException;
import next.ResourceNotFoundException;
import next.dao.AnswerDao;
import next.dao.JdbcAnswerDao;
import next.dao.JdbcQuestionDao;
import next.dao.QuestionDao;
import next.model.Result;
import next.service.QnaService;
import core.mvc.AbstractController;
import core.mvc.ModelAndView;
import core.utils.ServletRequestUtils;

public class ApiDeleteController extends AbstractController {
	private QuestionDao questionDao = JdbcQuestionDao.getInstance();
	private AnswerDao answerDao = JdbcAnswerDao.getInstance();
	
	private QnaService qnaService;
	
	public ApiDeleteController() {
		this.qnaService = new QnaService(questionDao, answerDao);
	}

	@Override
	public ModelAndView execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		long questionId = ServletRequestUtils.getRequiredLongParameter(request, "questionId");
		
		ModelAndView mav = jsonView();
		try {
			qnaService.delete(questionId);
			mav.addObject("result", Result.ok());
		} catch (ResourceNotFoundException|ExistedAnotherUserException ex) {
			mav.addObject("result", Result.fail(ex.getMessage()));
		}
		
		return mav;
	}

}
