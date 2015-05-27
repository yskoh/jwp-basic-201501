package core.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import core.utils.ServletRequestUtils;
import next.dao.AnswerDao;

public class DeleteAnswer extends AbstractController {

	@Override
	public ModelAndView execute(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		long answerId = ServletRequestUtils.getRequiredLongParameter(request, "answerId");
		
		AnswerDao answerdao = new AnswerDao();
		answerdao.delete(answerId);
		
		ModelAndView mav = jstlView("redirect:/show.next");
		return mav;
	}

}
