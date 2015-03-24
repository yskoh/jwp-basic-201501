package next.service;

import java.util.List;

import next.ExistedAnotherUserException;
import next.ResourceNotFoundException;
import next.dao.AnswerDao;
import next.dao.QuestionDao;
import next.model.Answer;
import next.model.Question;
import core.mvc.annotation.Bean;
import core.mvc.annotation.Inject;

@Bean
public class QnaService {
	private QuestionDao questionDao;
	private AnswerDao answerDao;

	@Inject
	public void setQuestionDao(QuestionDao questionDao) {
		this.questionDao = questionDao;
	}
	
	@Inject
	public void setAnswerDao(AnswerDao answerDao) {
		this.answerDao = answerDao;
	}
	
	public void save(Question question) {
		this.questionDao.insert(question);
	}

	public void delete(final long questionId) throws ResourceNotFoundException, ExistedAnotherUserException {
		Question question = questionDao.findWithAnswersById(questionId);
		
		if (!question.canDelete()) {
			throw new ExistedAnotherUserException("다른 사용자가 추가한 댓글이 존재해 삭제할 수 없습니다.");
		}

		questionDao.delete(questionId);
	}
	
	public Question findById(long questionId) {
		return questionDao.findById(questionId);
	}

	public List<Answer> findAnswersByQuestionId(long questionId) {
		return answerDao.findAllByQuestionId(questionId);
	}

	public List<Question> findAll() {
		return questionDao.findAll();
	}

	public void addAnswer(Answer answer) {
		answerDao.insert(answer);
	}

	public void updateCommentCount(long questionId) {
		questionDao.updateCommentCount(questionId);
	}
}