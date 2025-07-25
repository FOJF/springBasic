package com.beyond.basic.b2_board.post.domain;

import com.beyond.basic.b2_board.author.domain.Author;
import com.beyond.basic.b2_board.common.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Post extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String category;

    @Column(nullable = false)
    private String title;
    @Column(length = 3000)
    private String contents;
    @Builder.Default
    private String delYN = "N";

    private Boolean isBooked;
    private LocalDateTime bookedTime;

    //    FK설정시 ManyToOne필수
//    ManyToOne에서는 default fetch EAGER(즉시로딩) // 무조건 author테이블에서 조회하는 쿼리가 나감(Author객체를 사용하지 않아도)
//    그래서 일반적으로 fetch LAZY(지연로딩) 설정 // Author객체를 사용하지 않으면 쿼리가 나가지 않음
    @ManyToOne(fetch = FetchType.LAZY) // post(현재클래스를 기준으로) 여러개를 author 하나가 작성할 수 있으니까
    @JoinColumn(name = "author_id") // fk 관계성
    private Author author;

    public void updateBooked() {
        this.isBooked = Boolean.FALSE;
    }
}
