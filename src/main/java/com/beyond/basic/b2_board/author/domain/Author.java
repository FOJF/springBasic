package com.beyond.basic.b2_board.author.domain;

import com.beyond.basic.b2_board.common.domain.BaseTimeEntity;
import com.beyond.basic.b2_board.post.domain.Post;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
// JPA를 사용할 경우 Entity에 반드시 붙여야하는 어노테이션,
// JPA의 EntityManager에게 객체를 위임하기 위한 어노테이션
// EntityManager는 영속성 컨텍스트(엔티티의 현재상황)를 통해 DB 데이터 관리
@Entity
@Builder // Builder 어노테이션을 통해 유연하게 객체 생성가능
public class Author extends BaseTimeEntity {
    @Id //pk 설정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // IDENTITY : auto_increment, AUTO : jpa가 알아서 하도록 넘김
    private Long id;
    //    컬럼에 별다른 설정이 없을 경우 기본은 varchar(255)
    private String name;
    @Column(length = 50, unique = true, nullable = false)
    private String email;
    //    @Column(name = "pw") // 스프링에서는 password로 쓰고 db 컬럼명은 pw로 쓰고싶다는 뜻 (되도록이면 컬럼명과 변수명을 일치시켜야 개발의 혼선을 줄일 수 있음
    private String password;

    @Enumerated(EnumType.STRING) // enum 타입 사용시 문자열로 들어가게 하는 어노테이션
    @Builder.Default // 빌더패턴에서 디폴트값 사용시 선언해야하는 어노테이션(사용하지 않으면 빌더 내부에서 디폴트값을 무시하기 때문에)
    private Role role = Role.USER;

//    OneToMany는 기본적으로 FetchType.LAZY 이지만, 혼동의 가능성이 있으니까 상관없이 명시하는 것이 좋아보임
//    mappedBy에는 ManyToOne쪽에 변수명을 문자열로 지정. fk관리를 반대편(post)쪽에서 한다는 의미 -> 연관 관계의 주인 설정
//    자동으로 초기화해주지않아 직접 초기화 해줘야하고, 따라서 빌더패턴 적용시 @Builder.Default 어노테이션도 같이 적용해야 함
//    cascade : 부모객체의 변화에 따라 자식 객체가 같이 변하는 옵션 1) persist : 저장, 2) remove : 삭제
//    orphanRemoval 고아 삭제 여부 (자식의 자식까지 모두 삭제할 경우)
    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Post> postList = new ArrayList<>();

    @OneToOne(mappedBy = "author", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, orphanRemoval = true) // OneTo인 경우 mappedBy인듯
    private Address address;

    private String profileImgUrl;

    public void updateProfileImgUrl(String url) {this.profileImgUrl = url;}
    public void updatePW(String password) {
        this.password = password;
    }
}
