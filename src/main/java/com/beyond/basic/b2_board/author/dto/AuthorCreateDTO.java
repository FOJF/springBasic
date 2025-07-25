package com.beyond.basic.b2_board.author.dto;

import com.beyond.basic.b2_board.author.domain.Author;
import com.beyond.basic.b2_board.author.domain.Role;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // DTO계층 데이터의 안정성이 엔티티만큼 중요하지는 않으므로, setter도 일반적으로 추가
@AllArgsConstructor
@NoArgsConstructor
public class AuthorCreateDTO {
    @NotEmpty(message = "이름을 입력해주세요.")
    private String name;
    @NotEmpty(message = "이메일을 입력해주세요.")
    private String email;
    @NotEmpty(message = "비밀번호를 입력해주세요.")
    @Size(min = 8, message = "비밀번호는 8자리 이상으로 입력해주세요.")
    private String password;

//    사실 실제 현실에서 ADMIN의 가입을 api로 만들어주는 게 말이 안 됨(현실에선 직접 DB에 넣어주는게 맞음)
//    private Role role = Role.USER;

    public Author toEntity(String encodedPW) {
        // 빌더 패턴은 매개변수의 개수와 순서에 상관없이 객체 생성 가능
        return Author.builder()
                .name(this.name)
                .password(encodedPW)
                .email(this.email)
                .build();
    }
}
