package com.cos.book.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity // 서버 실행시에 Object Relation Mapping이 됨.( 테이블이 H2에 생성됨.)
public class Book {
	@Id // PK를 해당 변수로 하겠다는 뜻
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	// 해당데이터베이스 번호증가 전략을 띠라감
	private Long id;
	
	private String title;
	private String author;
}
