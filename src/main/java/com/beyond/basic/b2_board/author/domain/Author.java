package com.beyond.basic.b2_board.author.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
// JPA를 사용할 경우 Entity에 반드시 붙여야하는 어노테이션,
// JPA의 EntityManager에게 객체를 위임하기 위한 어노테이션
// EntityManager는 영속성 컨텍스트(엔티티의 현재상황)를 통해 DB 데이터 관리
@Entity
@Builder // Builder 어노테이션을 통해 유연하게 객체 생성가능
public class Author {
    @Id //pk 설정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // IDENTITY : auto_increment, AUTO : jpa가 알아서 하도록 넘김
    private Long id;
    //    컬럼에 별다른 설정이 없을 경우 기본은 varchar(255)
    private String name;
    @Column(length = 50, unique = true, nullable = false)
    private String email;
    //    @Column(name = "pw") // 스프링에서는 password로 쓰고 db 컬럼명은 pw로 쓰고싶다는 뜻 (되도록이면 컬럼명과 변수명을 일치시켜야 개발의 혼선을 줄일 수 있음
    private String password;
//    컬럼명에 캐멀케이스 사용시, db에는 created_time과 같이 언더스코어로 자동 변경해줌

    //    아래 두 어노테이션은 실제로는 작동하지만 db에서는 알 수 없음(describe해도 정보가 없음)
    @CreationTimestamp // db의 current timestamp와 같은 기능
    private LocalDateTime createdTime;
    @UpdateTimestamp // entity의 값이 변경되면 자동으로 시간 저장
    private LocalDateTime updatedTime;
    @Enumerated(EnumType.STRING) // enum 타입 사용시 문자열로 들어가게 하는 어노테이션
    @Builder.Default // 빌더패턴에서 디폴트값 사용시 선언해야하는 어노테이션(사용하지 않으면 빌더 내부에서 디폴트값을 무시하기 때문에)
    private Role role = Role.USER;

    public void updatePW(String password) {
        this.password = password;
    }
}
