package next.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import next.dao.AnswerDao;
import next.dao.QuestionDao;
import next.model.Answer;
import next.model.Question;
import next.model.Result;
import core.mvc.AbstractController;
import core.mvc.ModelAndView;
import core.utils.ServletRequestUtils;

public class ApiDeleteController extends AbstractController {
	private QuestionDao questionDao = QuestionDao.getInstance();
	private AnswerDao answerDao = AnswerDao.getInstance();

	@Override
	public ModelAndView execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		long questionId = ServletRequestUtils.getRequiredLongParameter(request, "questionId");
		
		Question question = questionDao.findById(questionId);
		ModelAndView mav = jsonView();
		if (question == null) {
			mav.addObject("result", Result.fail("존재하지 않는 질문입니다."));
			return mav;
		}
		
		List<Answer> answers = answerDao.findAllByQuestionId(questionId);
		if (answers.isEmpty()) {
			questionDao.delete(questionId);
			mav.addObject("result", Result.ok());
			return mav;
		}
		
		boolean canDelete = true;
		for (Answer answer : answers) {
			String writer = question.getWriter();
			if (!writer.equals(answer.getWriter())) {
				canDelete = false;
				break;
			}
		}
		
		if (canDelete) {
			questionDao.delete(questionId);
			mav.addObject("result", Result.ok());
			return mav;
		}
		
		mav.addObject("result", Result.fail("다른 사용자가 추가한 댓글이 존재해 삭제할 수 없습니다."));
		return mav;
	}

}
