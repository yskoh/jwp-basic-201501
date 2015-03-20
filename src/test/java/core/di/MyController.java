package core.di;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import core.mvc.ModelAndView;
import core.mvc.annotation.Controller;
import core.mvc.annotation.Inject;
import core.mvc.annotation.RequestMapping;
import core.mvc.annotation.RequestMethod;

@Controller
public class MyController {
	private static final Logger logger = LoggerFactory.getLogger(MyController.class);
	
	private MyService myService;
	
	@Inject
	public void setMyService(MyService myService) {
		logger.debug("call setMyService!");
		this.myService = myService;
	}
	
	@RequestMapping("/users/findUserId")
	public ModelAndView findUserId(HttpServletRequest request, HttpServletResponse response) {
		return null;
	}
	
	@RequestMapping(value="/users", method=RequestMethod.POST)
	public ModelAndView save(HttpServletRequest request, HttpServletResponse response) {
		return null;
	}
	
}
