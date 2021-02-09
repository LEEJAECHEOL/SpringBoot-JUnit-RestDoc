package com.cos.book.web;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import com.cos.book.domain.Book;
import com.cos.book.service.BookService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

// 단위 테스트(Controller관련 로직만 띄우기) Filter, ControllerAdvice(Exception처리할때 사용 해봄)

//@ExtendWith // spring에서 테스트할때 꼭 붙여야됨(JUnit4에서만)
@Slf4j
@WebMvcTest // 여기에 ExtendWith가 있음, 단위테스트할 때 붙혀쓴다.
// @WebMvcTest는 실제 컨트롤러, 필터, 컨트롤어드바이스를 IoC에 띄움
public class BookControllerUnitTest {
	@Autowired
	private MockMvc mockMvc;
	
//	@Mock메모리에안뜸(스프링환경에 안뜨고 모키토환경에 뜸) 그래서 @MockBean을 사용함. 
	// @WebMvcTest 자체가 컨트롤러가 띄워줄거기 때문에 Mock라고 적혀있으면 메모리에 안뜸.
	@MockBean // bookService가 IoC환경에 bean 등록됨. (가짜서비스를 넣은것)
	private BookService bookService;
	
	
	// BDDMockito패턴 을 사용
	// 스텁 : 미리 행동을 지정함 -> 대표적인 함수 : given, when, then
	@Test
	public void save_테스트() throws Exception {
//		log.info("save_테스트()시작 ===============================");
//		Book book = bookService.저장하기(new Book(null, "제목", "코스"));
//		System.out.println("book : " + book);
		
		// given (테스트를 하기 위한 준비) -> save를 하기 위해서 준비함 (준비단계)
		Book book = new Book(null, "스프링따라하기", "코스");
		String content = new ObjectMapper().writeValueAsString(book); // json데이터를string으로 바꿔줌
//		log.info(content);
		// 실제로 bookService.저장하기(book)을 실행하면return값을 받음.
		//하지만 가짜 데이터를 넣어봤자 실행안되기때문에 정해주는 것(스텁) thenReturn 행동을 지정
		when(bookService.저장하기(book)).thenReturn(new Book(1L, "스프링따라하기", "코스"));
		
		// when(테스트 실행)
		// ResultActions 응답을 받을 수 있음.
		ResultActions resultAction = mockMvc.perform(post("/book")
				.contentType(MediaType.APPLICATION_JSON_UTF8)	// 내가 던지는 데이터가 먼지 알려줌
				.content(content)	// 실제로 던질 데이터
				.accept(MediaType.APPLICATION_JSON_UTF8));	// 응답을 멀로 할지 알려줌
		
		// then (검증)
		 // $ 모든 값을 나타낸다 (문법 간단히 알아놓자)
		resultAction
			.andExpect(status().isCreated())	// 응답을 201을 기대한다
			.andExpect(jsonPath("$.title").value("스프링따라하기")) // title이 스프링따라하기 인것을 기대한다.
			.andDo(MockMvcResultHandlers.print());	// 그 다음 행동 = 결과를 콘솔창에 보여줌.
	}
	
	@Test
	public void findAll_테스트() throws Exception {
		// given
		List<Book> books = new ArrayList<>();
		books.add(new Book(1L, "스프링따라하기", "코스"));
		books.add(new Book(2L, "리액트따라하기", "코스"));
		when(bookService.모두가져오기()).thenReturn(books);
		
		// when
		ResultActions resultActions = mockMvc.perform(get("/book")
				.accept(MediaType.APPLICATION_JSON_UTF8));
		
		// 문서에서 mockMvc를 찾아보자
		// then
		resultActions
		.andExpect(status().isOk())
		.andExpect(jsonPath("$", Matchers.hasSize(2)))
		.andExpect(jsonPath("$.[0].title").value("스프링따라하기"))
		.andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	public void findById_테스트() throws Exception {
		// given
		Long id = 1L;
		when(bookService.한건가져오기(id)).thenReturn(new Book(1L, "자바 공부하기", "쌀"));
		
		// when
		ResultActions resultActions = mockMvc.perform(get("/book/{id}", id)
				.accept(MediaType.APPLICATION_JSON_UTF8));
		
		// then
		resultActions
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.title").value("자바 공부하기"))
		.andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	public void update_테스트() throws Exception {
		// given
		Long id = 1L;
		Book book = new Book(null, "C++ 따라하기", "코스");
		String content = new ObjectMapper().writeValueAsString(book); 
		
		when(bookService.수정하기(id, book)).thenReturn(new Book(1L, "C++ 따라하기", "쌀"));
		
		// when
		ResultActions resultAction = mockMvc.perform(put("/book/{id}",id)
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(content)
				.accept(MediaType.APPLICATION_JSON_UTF8));
		
		// then
		resultAction
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.title").value("C++ 따라하기"))
			.andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	public void delete_테스트() throws Exception {
		// given
		Long id = 1L;
		
		when(bookService.삭제하기(id)).thenReturn("ok");
		
		// when
		ResultActions resultAction = mockMvc.perform(delete("/book/{id}",id)
				.accept(MediaType.TEXT_PLAIN));
		
		// then
		resultAction
			.andExpect(status().isOk())
			.andDo(MockMvcResultHandlers.print());
		
		MvcResult requestResult = resultAction.andReturn();
		String result = requestResult.getResponse().getContentAsString();
		
		assertEquals("ok", result);
	}
}
