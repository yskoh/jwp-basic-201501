package next.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import next.dao.AnswerDao;
import next.model.Answer;
import core.mvc.AbstractController;
import core.mvc.ModelAndView;

public class addAnswer extends AbstractController{

	@Override
	public ModelAndView execute(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String writer = request.getParameter("writer");
		String content = request.getParameter("contents");
		int questionId = Integer.parseInt(request.getParameter("questionId"));
		
		Answer answer = new Answer(writer, content, questionId);
		AnswerDao answerDao = new AnswerDao();
		answerDao.insert(answer);
		
		ModelAndView mav = jstlView("redirect:/");
		return mav;
	}
}
