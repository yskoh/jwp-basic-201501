package next.dao;

import java.util.List;

import next.model.Answer;

public interface AnswerDao {

	void insert(Answer answer);

	List<Answer> findAllByQuestionId(long questionId);

	void delete(long questionId);

}