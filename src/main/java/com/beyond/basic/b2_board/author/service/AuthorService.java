package com.beyond.basic.b2_board.author.service;

import com.beyond.basic.b2_board.author.domain.Author;
import com.beyond.basic.b2_board.author.dto.*;
import com.beyond.basic.b2_board.author.repository.AuthorRepository;
import com.beyond.basic.b2_board.post.domain.Post;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
@Service // 트랜잭션 처리가 없는 경우 Controller와 달리 별다른 기능이 없어 Component로 대체 가능
@RequiredArgsConstructor
@Transactional //스프링에서 메서드 단위로 트랜젝션처리(commit)를 하고, 만약 예외(unchecked) 발생시 자동 롤백 처리 지원
// 클래스 차원에 붙히면 모든 메서드에 붙는 것과 같은 효과 -> 속도에 저하가 있을 수 있음(조회만 하는 경우에는 필요없지만 적용하니까)
// 그런 경우는 findAll 메서드와 같이 @Transactional(readOnly = true) 어노테이션을 추가하면 됨(제외되는 효과)
// update의 경우 레포를 거치지 않는다(영속성 컨텍스트로 인해)
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

//    private final AuthorJdbcRepository authorRepository; // JDBC

    private final AuthorRepository authorRepository; // Mybatis 특수한 인터페이스이기 때문에 잘 작동함
//    private final PostRepository postRepository;

    private final PasswordEncoder passwordEncoder;

    private final S3Client s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    //    객체 조립은 서비스 담당
    public AuthorDetailDTO save(AuthorCreateDTO authorCreateDTO, MultipartFile profileImg) {
        // 이메일 중복 검증
        Optional<Author> optionalAuthor = this.authorRepository.findByEmail(authorCreateDTO.getEmail());
        if (optionalAuthor.isPresent()) throw new IllegalArgumentException("중복된 Author 이메일입니다.");

        String encodedPW = passwordEncoder.encode(authorCreateDTO.getPassword());

        Author author = authorCreateDTO.toEntity(encodedPW);
        this.authorRepository.save(author);
//
////        cascading 테스트 : 회원이 생성될 때, 곧 바로 "가입인사 글을 생성하는 상황
////        방법 1) 직접 POST 객체 생성 후 저장
//        Post post = Post.builder()
//                .title("안녕하세요")
//                .contents(authorCreateDTO.getName() + "입니다. 반값습니다.")
//                .author(author)
//                .build();
//        this.postRepository.save(post);
//
//        log.info(author.getPostList().size()+"");
//        방법 2) casecade옵션 활용
        Post post = Post.builder()
                .title("안녕하세요")
                .contents(authorCreateDTO.getName() + "입니다. 반값습니다.")
                .isBooked(false)
                .bookedTime(null)
                .author(author)
                .build();
        author.getPostList().add(post);
//        System.out.println(profileImg.getOriginalFilename());

        if (profileImg != null)
            try {
                String[] s = profileImg.getOriginalFilename().split("\\.");
                String newProfileImgName = "author-" + author.getId() + "-profileimg." + s[s.length - 1];
//        String newProfileImgName = "author-" + author.getId() + "-profileimg" + profileImg.getOriginalFilename();

                // 이미지를 byte 형태로 업로드
                PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                        .bucket(bucket)
                        .key(newProfileImgName)
                        .contentType(profileImg.getContentType()) //image/jpeg, video/mp4 ...
                        .build();
                s3Client.putObject(putObjectRequest, RequestBody.fromBytes(profileImg.getBytes()));

                String profileImgUrl = s3Client.utilities().getUrl(a -> a.bucket(bucket).key(newProfileImgName)).toExternalForm();
                author.updateProfileImgUrl(profileImgUrl);
            } catch (Exception e) {
                // checkedException을 uncheckedException으로 변경해 rollback 되도록 예외 처리
                throw new IllegalArgumentException("이미지 업로드 실패");
            }

        return AuthorDetailDTO.fromEntity(author);
    }

    //    Transactional이 필요 없는 경우는 아래의 어노테이션 적용
    @Transactional(readOnly = true)
    public List<AuthorListDTO> findAll() {
        return this.authorRepository.findAll().stream().map(AuthorListDTO::fromEntity).toList();
    }

    @Transactional(readOnly = true)
    public AuthorDetailDTO findById(Long id) throws NoSuchElementException {
        Author author = this.authorRepository.findById(id).orElseThrow(() -> new NoSuchElementException("없는 Author ID 입니다."));

        return AuthorDetailDTO.fromEntity(author);
    }

    public void updatePassword(AuthorUpdatePwDTO authorUpdatePwDTO) throws NoSuchElementException {
//        dirty checking : 객체를 수정한 후 별도의 update 쿼리 발생 시키지 않아도, 영속성 컨텍스트에 의해 객체 변경사항을 자동으로 반영
        this.authorRepository.findByEmail(authorUpdatePwDTO.getEmail()).orElseThrow(() -> new NoSuchElementException("없는 Author 이메일입니다."))
                .updatePW(authorUpdatePwDTO.getPassword());
    }

    public void delete(Long id) {
        Author author = this.authorRepository.findById(id).orElseThrow(() -> new NoSuchElementException("없는 Author ID 입니다."));
        this.authorRepository.delete(author);
    }

    public Author doLogin(AuthorLoginDto authorLoginDto) {
//        Author author = this.authorRepository.findByEmail(authorLoginDto.getEmail())
//                .orElseThrow(() -> new EntityNotFoundException("없는 Author Email 입니다."));
//
//        if (!passwordEncoder.matches(authorLoginDto.getPassword(), author.getPassword()))
//            throw new BadCredentialsException("비밀번호가 틀렸습니다.");

        // 보안을 위해 어떤 것이 틀렸는 지 안 알려주는 방법
        Author author = this.authorRepository.findByEmail(authorLoginDto.getEmail())
                .orElseThrow(() -> new BadCredentialsException("이메일 또는 비밀번호가 틀렸습니다."));

        if (!passwordEncoder.matches(authorLoginDto.getPassword(), author.getPassword()))
            throw new BadCredentialsException("이메일 또는 비밀번호가 틀렸습니다.");

        return author;
    }

    public AuthorDetailDTO findMyInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Author author = this.authorRepository.findByEmail(email).orElseThrow(() -> new NoSuchElementException("없는 Author 이메일입니다."));
        return AuthorDetailDTO.fromEntity(author);
    }
}