package core.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import next.dao.AnswerDao;
import next.dao.QuestionDao;
import next.model.Question;
import core.utils.ServletRequestUtils;

public class UpdateForm extends AbstractController {

	QuestionDao questionDao = new QuestionDao();
	AnswerDao answerDao = new AnswerDao();

	@Override
	public ModelAndView execute(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String writer = ServletRequestUtils.getRequiredStringParameter(request,
				"writer");
		String title = ServletRequestUtils.getRequiredStringParameter(request,
				"title");
		String contents = ServletRequestUtils.getRequiredStringParameter(request,
				"contents");

		int questionId = Integer.parseInt(request.getParameter("questionId"));
		questionDao.update(new Question(writer, title, contents), questionId);

		return jstlView("redirect:/list.next");
	}

}
