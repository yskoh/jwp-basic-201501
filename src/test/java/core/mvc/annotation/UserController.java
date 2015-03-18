package core.mvc.annotation;

import core.mvc.ModelAndView;

@Controller
public class UserController {
	private UserDao userDao;
	
	@Inject
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
	
	@RequestMapping("/users/updateForm.next")
	public ModelAndView updateForm(String userId) {
		User user = userDao.findById(userId);
		ModelAndView mav = new ModelAndView();
		mav.addObject("user", user);
		return mav;
	}
	
}
