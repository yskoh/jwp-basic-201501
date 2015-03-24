package next.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import next.ExistedAnotherUserException;
import next.ResourceNotFoundException;
import next.model.Answer;
import next.model.Question;
import next.service.QnaService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import core.mvc.AbstractController;
import core.mvc.ModelAndView;
import core.mvc.annotation.Controller;
import core.mvc.annotation.Inject;
import core.mvc.annotation.RequestMapping;
import core.mvc.annotation.RequestMethod;
import core.utils.ServletRequestUtils;

@Controller
public class QnaController extends AbstractController {
	private static final Logger logger = LoggerFactory.getLogger(QnaController.class);

	private QnaService qnaService;

	@Inject
	public void setQnaService(QnaService qnaService) {
		this.qnaService = qnaService;
	}
	
	@RequestMapping("/list.next")
	public ModelAndView execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<Question> questions = qnaService.findAll();
		ModelAndView mav = jstlView("list.jsp");
		mav.addObject("questions", questions);
		return mav;
	}
	
	@RequestMapping("/form.next")
	public ModelAndView form(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return jstlView("form.jsp");
	}

	@RequestMapping(value = "/save.next", method = RequestMethod.POST)
	public ModelAndView save(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String writer = ServletRequestUtils.getRequiredStringParameter(request, "writer");
		String title = ServletRequestUtils.getRequiredStringParameter(request, "title");
		String contents = ServletRequestUtils.getRequiredStringParameter(request, "contents");
		qnaService.save(new Question(writer, title, contents));
		return jstlView("redirect:/");
	}
	
	@RequestMapping("/show.next")
	public ModelAndView show(HttpServletRequest request, HttpServletResponse response) throws Exception {
		long questionId = ServletRequestUtils.getRequiredLongParameter(request, "questionId");
		logger.debug("questionId : {}", questionId);
		Question question = qnaService.findById(questionId);
		List<Answer> answers = qnaService.findAnswersByQuestionId(questionId);
		ModelAndView mav = jstlView("show.jsp");
		mav.addObject("question", question);
		mav.addObject("answers", answers);
		return mav;
	}
	
	@RequestMapping(value = "/delete.next", method = RequestMethod.POST)
	public ModelAndView delete(HttpServletRequest request, HttpServletResponse response) throws Exception {
		long questionId = ServletRequestUtils.getRequiredLongParameter(request, "questionId");
		
		try {
			qnaService.delete(questionId);
			return jstlView("redirect:/list.next");
		} catch (ResourceNotFoundException|ExistedAnotherUserException ex) {
			ModelAndView mav = jstlView("show.jsp");
			mav.addObject("question", qnaService.findById(questionId));
			mav.addObject("answers", qnaService.findAnswersByQuestionId(questionId));
			mav.addObject("errorMessage", "다른 사용자가 추가한 댓글이 존재해 삭제할 수 없습니다.");
			return mav;
		}
	}
}
