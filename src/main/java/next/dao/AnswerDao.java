package next.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import javax.servlet.ServletResponse;

import next.model.Answer;
import core.jdbc.JdbcTemplate;
import core.jdbc.RowMapper;

public class AnswerDao {

	public void delete(long answerId){
		JdbcTemplate jdbcTemplate = new JdbcTemplate();
		String sql = "DELETE FROM answers WHERE answerId = ?";
		jdbcTemplate.update(sql, answerId);
	}
	public void insert(Answer answer) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate();
		String sql = "INSERT INTO ANSWERS (writer, contents, createdDate, questionId) VALUES (?, ?, ?, ?)";
		String sql2 = "UPDATE questions SET countOfComment = countOfComment +1 WHERE questionId = ?";
		jdbcTemplate.update(sql, answer.getWriter(),
				answer.getContents(),
				new Timestamp(answer.getTimeFromCreateDate()),
				answer.getQuestionId());
		jdbcTemplate.update(sql2, answer.getQuestionId());
	}

	public List<Answer> findAllByQuestionId(long questionId) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate();
		String sql = "SELECT answerId, writer, contents, createdDate FROM ANSWERS WHERE questionId = ? "
				+ "order by answerId desc";
		
		RowMapper<Answer> rm = new RowMapper<Answer>() {
			@Override
			public Answer mapRow(ResultSet rs) throws SQLException {
				return new Answer(
						rs.getLong("answerId"),
						rs.getString("writer"), 
						rs.getString("contents"),
						rs.getTimestamp("createdDate"), 
						questionId);
			}
		};
		
		return jdbcTemplate.query(sql, rm, questionId);
	}
	
	public Answer findWriter(long questionId) {
		RowMapper<Answer> rm = new RowMapper<Answer>() {
						@Override
					public Answer mapRow(ResultSet rs) throws SQLException {
							return new Answer(rs.getString("writer"));
						}
					};
					
					JdbcTemplate jdbcTemplate = new JdbcTemplate();
					String sql = "SELECT writer FROM ANSWERS WHERE questionId = ?";
					return jdbcTemplate.queryForObject(sql, rm, questionId);
	}	
}
