package com.cos.book.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cos.book.domain.Book;
import com.cos.book.domain.BookRepository;

import lombok.RequiredArgsConstructor;

// 기능을 정의할 수 있고, 트랜잭션을 관리할 수 있음.

@RequiredArgsConstructor
@Service
public class BookService {
	// 함수 => 송금() ->레파지토리에 여러개의 함수 실행 -> commit or rollback
	private final BookRepository bookRepository;
	
	@Transactional // 서비스 함수가 종료될때 commit할지 rollback할지 트랜잭션관리하겠다
	public Book 저장하기(Book book) {
		return bookRepository.save(book);
	}
	
	@Transactional(readOnly = true)	
	// JPA에는 변경감지라는 내부 기능 활성화 X, 
	// update시의 정합성을 유지해줌 ,
	// insert의 유령데이터현상(팬텀현상) 못막음
	public Book 한건가져오기(Long id) {
		return bookRepository.findById(id)
				.orElseThrow(()->new IllegalArgumentException("id를 확인해주세요."));
	}
	
	@Transactional(readOnly = true)	
	public List<Book> 모두가져오기(){
		return  bookRepository.findAll();
	}
	
	@Transactional
	public Book 수정하기(Long id, Book book) {
		// 더티체팅 update치기
		Book bookEntity = bookRepository.findById(id)
				.orElseThrow(()->new IllegalArgumentException("id를 확인해주세요."));	// 영속화(book 오브젝) -> 영속성 컨텍스트 보관
		bookEntity.setTitle(book.getTitle());
		bookEntity.setAuthor(book.getAuthor());
		return bookEntity;
	}// 함수 종료 => 트랜젝션 종료 => 영속화되어있는 데이터를 DB로 갱신(flush) => commit == 더티체킹
	
	@Transactional
	public String 삭제하기(Long id) {
		bookRepository.deleteById(id); // 오류가 터지면 익셉션을 타니깐 신경쓰지말고
		return "ok";
	}
}
