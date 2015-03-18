package next.dao;

import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.sql.DataSource;

import next.model.Question;

import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import core.jdbc.ConnectionManager;
import core.jdbc.JdbcTemplate;

public class QuestionDaoTest {
	private QuestionDao dut;

	@Before
	public void setup() {
		ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
		populator.addScript(new ClassPathResource("jwp.sql"));
		DatabasePopulatorUtils.execute(populator, ConnectionManager.getDataSource());
		
		DataSource dataSource = ConnectionManager.getDataSource();
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		dut = new JdbcQuestionDao(jdbcTemplate);
	}

	@Test
	public void crud() throws Exception {
		Question expected = new Question("자바지기", "title", "contents");
		dut.insert(expected);
		
		List<Question> questions = dut.findAll();
		assertTrue(questions.size() > 0);
	}
}
