package com.beyond.basic.b2_board.post.repository;

import com.beyond.basic.b2_board.author.domain.Author;
import com.beyond.basic.b2_board.post.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
//    select * from post where id=? and title=?;
//    List<Post> findByAuthorIdAndTitle(Long authorId, String Title);

//    select * from post where id=? and title=? order by createdTime desc;
//    List<Post> findByAuthorIdAndTitleOrderByCreatedTimeDesc(Long authorId, String Title);

//    변수명은 author_id이지만 author 객체가지고도 조회 가능
//    List<Post> findByAuthorId(Long authorId);
//    List<Post> findByAuthor(Author author);

//    클래스 기반으로 작성해야함
//    jpql을 사용한 일반 inner join
//    jpa는 기본적으로 lazy로딩을 지향해서, inner join 으로 filtering은 하되 post객체만 조회 -> N+1문제는 여전히 발생
//    select p.* from post p inner join author a on a.id=p.author_id;
//    @Query("select p from Post p inner join p.author")
//    List<Post> findAllJoin();

//    jpql을 사용한 fetch inner join
//    join시 post 뿐만 아니라 author 객체 까지 한번에 조립하여 조회 -> N+1문제 해결(우리가 알고 있는 일반적인 조인상황)
//    select * from post p inner join author a on a.id=p.author_id;
    @Query("select p from Post p inner join fetch p.author")
    List<Post> findAllFetchJoin();

//    paging 처리 + delYN 적용

    @Override
    // org.springframework.data.domain.Pageable을 임포트해야함(같은 이름의 클래스가 여러개 있어서 주의)
    // Page 객체 안에 List<Post>포함, 전체 페이지 수 등의 정보 포함.
    // Pageable 객체 안에는 페이지 size, 페이지 번호, 정렬기준등이 포함.
    Page<Post> findAll(Pageable pageable);

    Page<Post> findAllByDelYN(Pageable pageable, String delYN);
}
