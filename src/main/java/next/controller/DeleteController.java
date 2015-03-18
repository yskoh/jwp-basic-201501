package next.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import next.ExistedAnotherUserException;
import next.ResourceNotFoundException;
import next.dao.AnswerDao;
import next.dao.JdbcAnswerDao;
import next.dao.JdbcQuestionDao;
import next.dao.QuestionDao;
import next.service.QnaService;
import core.mvc.AbstractController;
import core.mvc.ModelAndView;
import core.utils.ServletRequestUtils;

public class DeleteController extends AbstractController {
	private QuestionDao questionDao = JdbcQuestionDao.getInstance();
	private AnswerDao answerDao = JdbcAnswerDao.getInstance();
	
	private QnaService qnaService;
	
	public DeleteController() {
		this.qnaService = new QnaService(questionDao, answerDao);
	}

	@Override
	public ModelAndView execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		long questionId = ServletRequestUtils.getRequiredLongParameter(request, "questionId");
		
		try {
			qnaService.delete(questionId);
			return jstlView("redirect:/list.next");
		} catch (ResourceNotFoundException|ExistedAnotherUserException ex) {
			ModelAndView mav = jstlView("show.jsp");
			mav.addObject("question", questionDao.findById(questionId));
			mav.addObject("answers", answerDao.findAllByQuestionId(questionId));
			mav.addObject("errorMessage", "다른 사용자가 추가한 댓글이 존재해 삭제할 수 없습니다.");
			return mav;
		}
	}
}
