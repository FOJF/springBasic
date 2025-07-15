package com.beyond.basic.b2_board.service;

import com.beyond.basic.b2_board.domain.Author;
import com.beyond.basic.b2_board.dto.AuthorCreateDTO;
import com.beyond.basic.b2_board.dto.AuthorDetailDTO;
import com.beyond.basic.b2_board.dto.AuthorListDTO;
import com.beyond.basic.b2_board.dto.AuthorUpdatePwDTO;
import com.beyond.basic.b2_board.repository.AuthorJdbcRepository;
import com.beyond.basic.b2_board.repository.AuthorMemoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service // 트랜잭션 처리가 없는 경우 Controller와 달리 별다른 기능이 없어 Component로 대체 가능
@RequiredArgsConstructor
@Transactional //스프링에서 메서드 단위로 트랜젝션처리를 하고, 만약 예외(unchecked) 발생시 자동 롤백 처리 지원
// 클래스 차원에 붙히면 모든 메서드에 붙는 것과 같은 효과 -> 속도에 저하가 있을 수 있음(조회만 하는 경우에는 필요없지만 적용하니까)
// 그런 경우는 findAll 메서드와 같이 @Transactional(readOnly = true) 어노테이션을 추가하면 됨(제외되는 효과)
public class AuthorService {
////    의존성 주입(DI) 방법 1. Autowired 어노테이션 사용 -> 필드 주입. , 언제든지 새로 객체를 만들 수 있는 단점(안정성이 깨짐)이 있음(final로 선언하지 못해서 막을 방법이 없음)
//    @Autowired
//    private AuthorRepository authorRepository;

////    의존성 주입(DI) 방법 2. 생성자 주입 방식(가장 많이 쓰는 방식)
////    장점 1) final을 통해 상수로 사용가능(안정성 향상)
////    2) 다형성 구현 가능
////    3) 순환참조 방지(컴파일 타임에 체크)
//    private final AuthorRepository authorRepository;
//
    /// /    객체로 만들어지는 시점에 스프링에서 AuthorRepository 객체를 매개변수로 주입
//    @Autowired //  생성자가 하나밖에 없을 때에는 Autowired 생략 가능
//    public AuthorService(AuthorRepository authorRepository) {
//        this.authorRepository = authorRepository;
//    }

//    의존성 주입(DI) 방법 3. RequiredArgs 어노테이션 사용 -> 반드시 초기화 되어야 하는 필드(final 등)을 대상으로 생성자를 자동으로 생성
//    다형성 설계는 불가
//    private final AuthorMemoryRepository authorRepository; // 메모리 db 용
    private final AuthorJdbcRepository authorRepository;

    //    객체 조립은 서비스 담당
    public Author save(AuthorCreateDTO authorCreateDTO) {
        // 이메일 중복 검증
        Optional<Author> optionalAuthor = this.authorRepository.findByEmail(authorCreateDTO.getEmail());
        if (optionalAuthor.isPresent()) throw new IllegalArgumentException("중복된 이메일입니다.");

        Author author = authorCreateDTO.toEntity();
        this.authorRepository.save(author);
        return author;
    }

//    Transactional이 필요 없는 경우는 아래의 어노테이션 적용
    @Transactional(readOnly = true)
    public List<AuthorListDTO> findAll() {
        return this.authorRepository.findAll().stream().map(AuthorListDTO::fromEntity).toList();
    }

    @Transactional(readOnly = true)
    public AuthorDetailDTO findById(Long id) throws NoSuchElementException {
        Author author = this.authorRepository.findById(id).orElseThrow(() -> new NoSuchElementException(id + "번은 없는 ID입니다."));
        return AuthorDetailDTO.fromEntity(author);
    }

    public Author updatePassword(AuthorUpdatePwDTO authorUpdatePwDTO) throws NoSuchElementException {
        this.authorRepository.findByEmail(authorUpdatePwDTO.getEmail()).orElseThrow(() -> new NoSuchElementException("없는 이메일입니다."))
                .updatePW(authorUpdatePwDTO.getPassword());
        return this.authorRepository.findByEmail(authorUpdatePwDTO.getEmail()).get();
    }

    public void delete(Long id) {
        this.authorRepository.delete(id);
    }
}