package next.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import javax.servlet.ServletResponse;

import next.model.Question;
import core.jdbc.JdbcTemplate;
import core.jdbc.RowMapper;

public class QuestionDao {
	
	public void update(Question question, int questionId){
		JdbcTemplate jdbcTemplate = new JdbcTemplate();
		String sql = "UPDATE questions SET writer =?, title =?, contents=? WHERE questionId= ?";
		jdbcTemplate.update(sql, question.getWriter(), question.getTitle(), question.getContents(), questionId);
	}
	
	public void delete(long questionId){
		JdbcTemplate jdbcTemplate = new JdbcTemplate();
		String sql = " DELETE FROM questions where questionId = ?";
		jdbcTemplate.update(sql, questionId);
	}
	
	public void subtractCount(long questionId) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate();
		String sql = "UPDATE questions SET countOfComment = countOfComment-1 WHERE questionId = ?";
		jdbcTemplate.update(sql, questionId);
	}
	public void insert(Question question) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate();
		String sql = "INSERT INTO QUESTIONS (writer, title, contents, createdDate, countOfComment) VALUES (?, ?, ?, ?, ?)";
		jdbcTemplate.update(sql, 
				question.getWriter(), 
				question.getTitle(), 
				question.getContents(),
				new Timestamp(question.getTimeFromCreateDate()), 
				question.getCountOfComment());
	}
	
	public List<Question> findAll() {
		JdbcTemplate jdbcTemplate = new JdbcTemplate();
		String sql = "SELECT questionId, writer, title, createdDate, countOfComment FROM QUESTIONS "
				+ "order by questionId desc";
		
		RowMapper<Question> rm = new RowMapper<Question>() {
			@Override
			public Question mapRow(ResultSet rs) throws SQLException {
				return new Question(rs.getLong("questionId"),
						rs.getString("writer"), rs.getString("title"), null,
						rs.getTimestamp("createdDate"),
						rs.getInt("countOfComment"));
			}
			
		};
		
		return jdbcTemplate.query(sql, rm);
	}

	public Question findById(long questionId) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate();
		String sql = "SELECT questionId, writer, title, contents, createdDate, countOfComment FROM QUESTIONS "
				+ "WHERE questionId = ?";
		
		RowMapper<Question> rm = new RowMapper<Question>() {
			@Override
			public Question mapRow(ResultSet rs) throws SQLException {
				return new Question(rs.getLong("questionId"),
						rs.getString("writer"), rs.getString("title"),
						rs.getString("contents"),
						rs.getTimestamp("createdDate"),
						rs.getInt("countOfComment"));
			}
			
		};
		
		return jdbcTemplate.queryForObject(sql, rm, questionId);
	}

}
