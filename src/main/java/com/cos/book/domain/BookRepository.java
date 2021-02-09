package com.cos.book.domain;

import org.springframework.data.jpa.repository.JpaRepository;

// @Repository적어야 스프링 IoC에 Bean으로 등록이 되는데
// JpaRepository를 extends를 하면 생략가능 (JpaRepository 안에 @Repository가 있기때문에.)
// JpaRepository는 CRUD함수가 있음

public interface BookRepository extends JpaRepository<Book, Long> {

}
