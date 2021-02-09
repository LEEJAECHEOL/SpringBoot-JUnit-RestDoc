package com.cos.book.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

// 단위테스트 (DB관련된 Bean이 IoC에 등록되면 됨.)

@Transactional
@AutoConfigureTestDatabase(replace = Replace.ANY) // 가짜 디비로 테스트. Replace.NONE : 실제 DB로 테스트
@DataJpaTest // jpa와 관련된 애들(로직)만 띄워짐 Repository들을 다 IoC에 등록해줌
public class BookRepositoryUnitTest {
	
//	@Mock으로 띄울 필요없음
	@Autowired
	private BookRepository bookRepository;
	
	@Test
	public void save_테스트() {
		// given
		Book book = new Book(null, "책제목1", "저자1");
		
		// when
		Book bookEntity = bookRepository.save(book);
		
		assertEquals("책제목1", bookEntity.getTitle());
	}
	
	
}
