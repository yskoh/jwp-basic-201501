package core.di;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import core.mvc.annotation.Bean;
import core.mvc.annotation.Inject;

@Bean
public class MyService {
	private static final Logger logger = LoggerFactory.getLogger(MyService.class);
	
	private MyDao myDao;
	
	@Inject
	public void setMyDao(MyDao myDao) {
		logger.debug("call setMyDao!");
		
		this.myDao = myDao;
	}
}
