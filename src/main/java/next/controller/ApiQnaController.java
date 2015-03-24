package next.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import next.ExistedAnotherUserException;
import next.ResourceNotFoundException;
import next.model.Answer;
import next.model.Result;
import next.service.QnaService;
import core.mvc.AbstractController;
import core.mvc.ModelAndView;
import core.mvc.annotation.Controller;
import core.mvc.annotation.Inject;
import core.mvc.annotation.RequestMapping;
import core.mvc.annotation.RequestMethod;
import core.utils.ServletRequestUtils;

@Controller
public class ApiQnaController extends AbstractController {
	private QnaService qnaService;

	@Inject
	public void setQnaService(QnaService qnaService) {
		this.qnaService = qnaService;
	}
	
	@RequestMapping("/api/list.next")
	public ModelAndView list(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView mav = jsonView();
		mav.addObject("questions", qnaService.findAll());
		return mav;
	}
	
	@RequestMapping(value = "/api/addanswer.next", method = RequestMethod.POST)
	public ModelAndView addAnswser(HttpServletRequest request, HttpServletResponse response) throws Exception {
		long questionId = ServletRequestUtils.getRequiredLongParameter(request, "questionId");
		String writer = ServletRequestUtils.getRequiredStringParameter(request, "writer");
		String contents = ServletRequestUtils.getRequiredStringParameter(request, "contents");
		Answer answer = new Answer(writer, contents, questionId);
		qnaService.addAnswer(answer);
		qnaService.updateCommentCount(questionId);
		return jsonView();
	}
	
	@RequestMapping(value = "/api/delete.next", method = RequestMethod.POST)
	public ModelAndView delete(HttpServletRequest request, HttpServletResponse response) throws Exception {
		long questionId = ServletRequestUtils.getRequiredLongParameter(request, "questionId");

		ModelAndView mav = jsonView();
		try {
			qnaService.delete(questionId);
			mav.addObject("result", Result.ok());
		} catch (ResourceNotFoundException | ExistedAnotherUserException ex) {
			mav.addObject("result", Result.fail(ex.getMessage()));
		}

		return mav;
	}
}
