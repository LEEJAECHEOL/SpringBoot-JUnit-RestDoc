package com.cos.book.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cos.book.domain.BookRepository;

/**
 * 단위테스트(Service와 관련된 애들만 메모리(IoC)에 띄우면 됨.)
 * bookRepository=> 가짜객체로 만들 수 있음.
 * 
 */

@ExtendWith(MockitoExtension.class)
public class BookServiceUnitTest {
	
	@InjectMocks // BookService객체가 만들어질 때 BookServiceUnitTest 파일에 @Mock로 등록된 모든 애들을 주입받는다.
	private BookService bookService;
	
	@Mock
	private BookRepository bookRepository;
	
	// 일단 가지고 있으면 됨
	@Test
	public void 저장하기_테스트() {
		
	}
	
	
}
