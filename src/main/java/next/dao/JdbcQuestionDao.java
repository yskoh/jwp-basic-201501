package next.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import next.model.Question;
import core.jdbc.JdbcTemplate;
import core.jdbc.RowMapper;

public class JdbcQuestionDao implements QuestionDao {
	private static QuestionDao questionDao = new JdbcQuestionDao();
	private JdbcTemplate jdbcTemplate = JdbcTemplate.getInstance();
	
	private JdbcQuestionDao() {
	}
	
	public static QuestionDao getInstance() {
		return questionDao;
	}
	
	/* (non-Javadoc)
	 * @see next.dao.QDao#insert(next.model.Question)
	 */
	@Override
	public void insert(Question question) {
		String sql = "INSERT INTO QUESTIONS (writer, title, contents, createdDate, countOfComment) VALUES (?, ?, ?, ?, ?)";
		jdbcTemplate.update(sql, 
				question.getWriter(), 
				question.getTitle(), 
				question.getContents(),
				new Timestamp(question.getTimeFromCreateDate()), 
				question.getCountOfComment());
	}
	
	/* (non-Javadoc)
	 * @see next.dao.QDao#findAll()
	 */
	@Override
	public List<Question> findAll() {
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

	/* (non-Javadoc)
	 * @see next.dao.QDao#findById(long)
	 */
	@Override
	public Question findById(long questionId) {
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

	/* (non-Javadoc)
	 * @see next.dao.QDao#updateCommentCount(long)
	 */
	@Override
	public void updateCommentCount(long questionId) {
		String sql = "UPDATE QUESTIONS set countOfComment = countOfComment + 1 WHERE questionId = ?";
		jdbcTemplate.update(sql, questionId);
	}

	/* (non-Javadoc)
	 * @see next.dao.QDao#delete(long)
	 */
	@Override
	public void delete(long questionId) {
		String sql = "DELETE FROM QUESTIONS WHERE questionId = ?";
		jdbcTemplate.update(sql, questionId);
	}
}
