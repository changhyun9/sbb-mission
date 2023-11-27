package com.example.sbb;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.sbb.answer.Answer;
import com.example.sbb.answer.AnswerRepository;
import com.example.sbb.question.Question;
import com.example.sbb.question.QuestionRepository;
import com.example.sbb.question.QuestionService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class SbbApplicationTests {

	@Autowired
	private QuestionRepository questionRepository;
	@Autowired
	private AnswerRepository answerRepository;
	@Autowired
	private QuestionService questionService;

	/*@Test
	void testJpa() {
		Question q1 = new Question();
		q1.setSubject("sbb가 무엇인가요?");
		q1.setContent("sbb에 대해 알고 싶습니다");
		q1.setCreateDate(LocalDateTime.now());
		questionRepository.save(q1);

		Question q2 = new Question();
		q2.setSubject("스프링부트 모델 질문입니다.");
		q2.setContent("id는 자동으로 생성되나요?");
		q2.setCreateDate(LocalDateTime.now());
		questionRepository.save(q2);
	}*/

	@Test
	void testFindAll() {
		List<Question> all = questionRepository.findAll();
		assertEquals(2, all.size());

		Question question = all.get(0);
		assertEquals("sbb가 무엇인가?", question.getSubject());
	}

	@Test
	void testFindById() {
		Optional<Question> findById = questionRepository.findById(1);
		if(findById.isPresent()){
			Question question = findById.get();
			assertEquals("sbb가 무엇인가요?", question.getSubject());
		}
	}

	@Test
	void testFindBySubject() {
		Question q = questionRepository.findBySubject("sbb가 무엇인가요?");
		assertEquals(1,q.getId());
	}

	@Test
	void testFindBySubjectAndContent() {
		Question q = questionRepository.findBySubjectAndContent(
				"sbb가 무엇인가요?", "sbb에 대해 알고 싶습니다");
		assertEquals(1, q.getId());
	}

	@Test
	void testFindBySubjectLike() {
		List<Question> list = questionRepository.findBySubjectLike("sbb%");
		Question question = list.get(0);
		assertEquals("sbb가 무엇인가요?", question.getSubject());
	}

	@Test
	void testModify() {
		Optional<Question> oq = questionRepository.findById(1);
		assertTrue(oq.isPresent());
		Question question = oq.get();
		question.setSubject("수정된 제목");
		questionRepository.save(question);
	}

	@Test
	void testDelete() {
		assertEquals(2, questionRepository.count());
		Optional<Question> oq = questionRepository.findById(1);
		assertTrue(oq.isPresent());
		Question question = oq.get();
		questionRepository.delete(question);
		assertEquals(1, questionRepository.count());
	}

	@Test
	void testSaveAnswer() {
		Optional<Question> oq = questionRepository.findById(2);
		assertTrue(oq.isPresent());
		Question question = oq.get();

		Answer a = new Answer();
		a.setContent("네 자동으로 생성됩니다.");
		a.setQuestion(question);
		a.setCreateDate(LocalDateTime.now());
		answerRepository.save(a);
	}

	@Test
	void testFindByIdAnswer() {
		Optional<Answer> oa = answerRepository.findById(1);
		assertTrue(oa.isPresent());
		Answer answer = oa.get();
		assertEquals(2, answer.getQuestion().getId());
	}

	@Transactional // DB 세션을 메서드 종료까지 유지시켜줌
	@Test
	void testFindAnswerAtQuestionRepo() {
		Optional<Question> oq = questionRepository.findById(2);
		assertTrue(oq.isPresent());
		Question question = oq.get();

		List<Answer> answerList = question.getAnswerList();

		assertEquals(1, answerList.size());
		assertEquals("네 자동으로 생성됩니다.", answerList.get(0).getContent());
	}

	@Test
	void testDataInsert() {
		for (int i = 1; i <= 300; i++) {
			String subject = String.format("테스트 데이터입니다 : [%3d]", i);
			String content = "내용무";
			questionService.create(subject, content);
		}
	}
}
