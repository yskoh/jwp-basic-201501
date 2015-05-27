package core.mvc;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import next.dao.QuestionDao;
import next.model.Question;

public class Moblie extends AbstractController {

	@Override
	public ModelAndView execute(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		QuestionDao questionDao = new QuestionDao();
		List<Question>questions = questionDao.findAll();
		Gson gson = new Gson();
		response.getWriter().print(gson.toJson(questions));
		
		ModelAndView mav = jsonView();
		return mav;
	}

}
