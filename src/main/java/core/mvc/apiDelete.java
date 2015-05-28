package core.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import next.dao.AnswerDao;
import next.dao.QuestionDao;
import core.utils.ServletRequestUtils;

public class apiDelete extends AbstractController {
	
	@Override
	public ModelAndView execute(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		int questionId =ServletRequestUtils.getRequiredIntParameter(request, "questionId");
		
		QuestionDao questionDao = new QuestionDao();
		AnswerDao answerDao = new AnswerDao();
		if(questionDao.findById(questionId).getCountOfComment() ==0){
			questionDao.delete(questionId);			
		}
		else{
			String answerWriter = answerDao.findWriter(questionId).getWriter();
			String questionWriter = questionDao.findById(questionId).getWriter();
			
			if(answerWriter == questionWriter){
				questionDao.delete(questionId);
			}
			
		}
		
		ModelAndView mav = jsonView();
		return mav;
	}

}
