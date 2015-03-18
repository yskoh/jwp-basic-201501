package next.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import next.dao.JdbcAnswerDao;
import next.dao.AnswerDao;
import next.dao.QuestionDao;
import next.dao.JdbcQuestionDao;
import next.model.Answer;
import core.mvc.AbstractController;
import core.mvc.ModelAndView;
import core.utils.ServletRequestUtils;

public class AddAnswerController extends AbstractController {
	private QuestionDao questionDao = JdbcQuestionDao.getInstance();
	private AnswerDao answerDao = JdbcAnswerDao.getInstance();

	@Override
	public ModelAndView execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		long questionId = ServletRequestUtils.getRequiredLongParameter(request, "questionId");
		String writer = ServletRequestUtils.getRequiredStringParameter(request, "writer");
		String contents = ServletRequestUtils.getRequiredStringParameter(request, "contents");
		Answer answer = new Answer(writer, contents, questionId);
		answerDao.insert(answer);
		questionDao.updateCommentCount(questionId);
		return jsonView();
	}
}
