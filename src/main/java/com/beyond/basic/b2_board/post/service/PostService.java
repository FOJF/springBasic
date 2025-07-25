package com.beyond.basic.b2_board.post.service;

import com.beyond.basic.b2_board.author.domain.Author;
import com.beyond.basic.b2_board.author.repository.AuthorRepository;
import com.beyond.basic.b2_board.post.domain.Post;
import com.beyond.basic.b2_board.post.dto.PostCreateDto;
import com.beyond.basic.b2_board.post.dto.PostDetailDto;
import com.beyond.basic.b2_board.post.dto.PostListDto;
import com.beyond.basic.b2_board.post.dto.PostSearchDto;
import com.beyond.basic.b2_board.post.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    //    private final AuthorService authorService; // 의도치 않게 순환참조가 발생할 가능성이 있어서 별로임. 아래와 같이 Repository를 사용하는 것이 어지간하면 더 좋아보임
    private final AuthorRepository authorRepository;

    public void save(PostCreateDto dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName(); // claims subject : email, 우리가 세팅했던 이메일(Subject)을 꺼내오는 방법
        Author author = this.authorRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("없는 ID 입니다."));

        this.postRepository.save(dto.toEntity(author));
    }

    public PostDetailDto findById(Long id) {
        Post post = this.postRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("없는 ID 입니다."));
// 엔티티간의 관계성 설정을 하지 않았을 경우
//        String email = this.authorRepository.findById(post.getAuthorId()).orElseThrow(() -> new EntityNotFoundException("없는 Author ID 입니다.")).getEmail();
//        return PostDetailDto.fromEntity(post, email);

//        엔티티간의 관계성이 설정되어 있는 경우
        return PostDetailDto.fromEntity(post);
    }

    public List<PostListDto> findAll() {
//        postList를 조회할 때 참조 관계에 있는 author까지 조회하게 되므로, N(Author 쿼리) + 1(Post쿼리) 문제 발생
//        jpa는 기본방향성이 fetch lazy라서, 참조하는 시점에 쿼리를 내보내게 되어 직접 Join문을 안하고 N+1문제가 발생한다.
//        실제로는 페이징처리를 해서 큰 문제가 되지 않을 가능성은 있지만, 문제가 발생할 소지가 분명하기 때문에 해결하는 방법을 알아야 할 듯

//        return this.postRepository.findAll().stream().map(PostListDto::fromEntity).toList();
//Hibernate: select p1_0.id,p1_0.author_id,p1_0.contents,p1_0.created_time,p1_0.title,p1_0.updated_time from post p1_0
//Hibernate: select a1_0.id,a1_0.created_time,a1_0.email,a1_0.name,a1_0.password,a1_0.role,a1_0.updated_time from author a1_0 where a1_0.id=?
//Hibernate: select a1_0.id,a1_0.created_time,a1_0.email,a1_0.name,a1_0.password,a1_0.role,a1_0.updated_time from author a1_0 where a1_0.id=?

// 여전히 N+1 문제 발생(author를 영속화는 해주지 않음), author객체의 정보는 필요하지만 영속화는 필요하지 않을때 사용하는 건가?
//        return this.postRepository.findAllJoin().stream().map(PostListDto::fromEntity).toList();
//Hibernate: select p1_0.id,p1_0.author_id,p1_0.contents,p1_0.created_time,p1_0.title,p1_0.updated_time from post p1_0 join author a1_0 on a1_0.id=p1_0.author_id
//Hibernate: select a1_0.id,a1_0.created_time,a1_0.email,a1_0.name,a1_0.password,a1_0.role,a1_0.updated_time from author a1_0 where a1_0.id=?
//Hibernate: select a1_0.id,a1_0.created_time,a1_0.email,a1_0.name,a1_0.password,a1_0.role,a1_0.updated_time from author a1_0 where a1_0.id=?

//        FetchJoin을 통해 N+1문제 해결
        return this.postRepository.findAllFetchJoin().stream().map(PostListDto::fromEntity).toList();
//Hibernate: select p1_0.id,a1_0.id,a1_0.created_time,a1_0.email,a1_0.name,a1_0.password,a1_0.role,a1_0.updated_time,p1_0.contents,p1_0.created_time,p1_0.title,p1_0.updated_time from post p1_0 join author a1_0 on a1_0.id=p1_0.author_id
    }

    //    페이징 처리
    public Page<PostListDto> findAll(Pageable pageable) {
        Page<Post> posts = this.postRepository.findAll(pageable);
        return posts.map(PostListDto::fromEntity);
    }

    //    페이징, 삭제여부, 예약여부까지
    public Page<PostListDto> findAllByDelYNAndIsBooked(Pageable pageable) {
        Page<Post> posts = this.postRepository.findAllByDelYNAndIsBooked(pageable, "N", Boolean.FALSE);
        return posts.map(PostListDto::fromEntity);
    }

    // 검색을 위해 Specification 객체를 스프링에서 제공
    // Specification 객체는 복잡한 쿼리를 명세를 이용하여 정의하는 방식으로, 쿼리를 쉽게 생성.
    public Page<PostListDto> findAll(Pageable pageable, PostSearchDto dto) {
        Specification<Post> specification = new Specification<Post>() {
            // select * from post where del_yn = "N" and is_booked = false and title like "%dto.getTitle()%" and category=dto.getCategory();
            @Override
            public Predicate toPredicate(Root<Post> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                // Root : 엔티티의 속성을 접근하기 위한 객체
                // criteriaBuilder : 쿼리를 생성하기 위한 객체
                List<Predicate> predicates = new ArrayList<>();
                predicates.add(criteriaBuilder.equal(root.get("delYN"), "N"));
                predicates.add(criteriaBuilder.equal(root.get("isBooked"), Boolean.FALSE));
                if (dto.getCategory() != null)
                    predicates.add(criteriaBuilder.equal(root.get("category"), dto.getCategory()));

                if (dto.getTitle() != null)
                    predicates.add(criteriaBuilder.like(root.get("title"), "%" + dto.getTitle() + "%"));

                Predicate[] predicateArr = predicates.toArray(Predicate[]::new);
                return criteriaBuilder.and(predicateArr);
            }
        };
        Page<Post> posts = this.postRepository.findAll(specification, pageable);
        return posts.map(PostListDto::fromEntity);
    }
}
