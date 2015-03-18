package next.service;

import java.util.List;

import next.ExistedAnotherUserException;
import next.ResourceNotFoundException;
import next.dao.AnswerDao;
import next.dao.QuestionDao;
import next.model.Answer;
import next.model.Question;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QnaService {
	private static final Logger logger = LoggerFactory.getLogger(QnaService.class);
	
	private QuestionDao questionDao;
	private AnswerDao answerDao;
	
	public QnaService(QuestionDao questionDao, AnswerDao answerDao) {
		this.questionDao = questionDao;
		this.answerDao = answerDao;
	}
	
	public void delete(final long questionId) throws ResourceNotFoundException, ExistedAnotherUserException {
		Question question = questionDao.findById(questionId);
		if (question == null) {
			throw new ResourceNotFoundException("존재하지 않는 질문입니다.");
		}

		List<Answer> answers = answerDao.findAllByQuestionId(questionId);
		if (answers.isEmpty()) {
			questionDao.delete(questionId);
			return;
		}

		
		for (Answer answer : answers) {
			String writer = question.getWriter();
			logger.debug("question writer : {}, answer writer : {}", writer, answer.getWriter());
			if (!writer.equals(answer.getWriter())) {
				throw new ExistedAnotherUserException("다른 사용자가 추가한 댓글이 존재해 삭제할 수 없습니다.");
			}
		}

		answerDao.delete(questionId);
		questionDao.delete(questionId);
	}
}
