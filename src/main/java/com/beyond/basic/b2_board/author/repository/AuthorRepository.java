package com.beyond.basic.b2_board.author.repository;

import com.beyond.basic.b2_board.author.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
// SpringDataJpa를 사용하기 위해서는 JpaRepository를 상속해야하고, 상속시에 Entity명과 Pk의 타입을 지정해 줌
// JpaRepository를 상속함으로서 JpaRepository의 주요기능(CRUD기능이 사전에 구현) 상속

//    save : 저장
//    findAll : 목록 조회
//    findById : pk로 찾기
//    delete : 삭제

//    등등이 이미 구현되어 있음

    //    그 외에 다른 컬럼으로 조회할 때는 findBy + 칼럼명 형식으로 선언만 하면 실행시점에 자동 구현.
    Optional<Author> findByEmail(String email);
}
