package com.beyond.basic.b1_hello.Controller;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

//@Getter // 클래스 내의 모든 변수를 대상으로 getter를 생성
//@Setter // 클래스 내의 모든 변수를 대상으로 setter를 생성
//@ToString // 클래스 toString을 자동으로 생성
@Data // Getter, Setter, toString을 모두 자동으로 생성
@NoArgsConstructor // 기본 생성자를 자동으로 만들어줌
@AllArgsConstructor // 모든 매개변수가 있는 생성자를 만들어줌
// 기본생성자와 Getter가 있어야지만 json을 객체로 자동생성이 가능하기 때문에 반드시 만들어야함 (Jackson의 ObjectMapper을 사용했을 때, 경험했었음)
@RequiredArgsConstructor
public class Hello {
    @NonNull
    private String name;
    @NonNull
    private String email;
//    private MultipartFile photo; // 이 방법이 일반적이지만 다른 테스트의 영향을 줄 수 있어 다른 방식으로 해봄
    private List<Score> scores;

//    public Hello(String name, String email) {
//        this.name = name;
//        this.email = email;
//    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class Score {
        private String subject;
        private int score;
    }
}

