package core.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import core.utils.ServletRequestUtils;
import next.dao.AnswerDao;
import next.dao.QuestionDao;

public class DeleteAnswer extends AbstractController {

	@Override
	public ModelAndView execute(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		long answerId = ServletRequestUtils.getRequiredLongParameter(request, "answerId");
		long questionId = ServletRequestUtils.getRequiredLongParameter(request, "questionId");
		
		AnswerDao answerdao = new AnswerDao();
		answerdao.delete(answerId);
		QuestionDao questiondao = new QuestionDao();
		questiondao.subtractCount(questionId);
		
		ModelAndView mav = jstlView("redirect:/");
		return mav;
	}

}
