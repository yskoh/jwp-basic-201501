package core.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import next.dao.QuestionDao;
import next.model.Question;
import core.utils.ServletRequestUtils;

public class EditController extends AbstractController {

	private String forwardUrl;

	public EditController(String forwardUrl) {
		this.forwardUrl = forwardUrl;
	}
	
	@Override
	public ModelAndView execute(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		long questionId= ServletRequestUtils.getRequiredLongParameter(request,"questionId");
		QuestionDao questionDao = new QuestionDao();
		Question question = questionDao.findById(questionId);
		
		if(question != null){
			ModelAndView mav = jstlView("form.jsp");
			mav.addObject("question", question);
			return mav;
		}
		
		if (forwardUrl == null) {
			throw new NullPointerException(
					"forwardUrl is null. 이동할 URL을 입력하세요.");
		}
		
		return jstlView(forwardUrl);
	}

}
