package com.beyond.basic.b2_board.author.dto;

import com.beyond.basic.b2_board.author.domain.Author;
import com.beyond.basic.b2_board.author.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class AuthorDetailDTO {
    private Long id;
    private String name;
    private String email;
    private Role role;
    private Integer postCount;
    private LocalDateTime createdTime;

    //    1개의 entity로만 dto가 조립되는 것이 아니기에, dto클래스에서 fromEntity를 설계.
    public static AuthorDetailDTO fromEntity(Author author) {
        return AuthorDetailDTO.builder()
                .id(author.getId())
                .email(author.getEmail())
                .name(author.getName())
                .role(author.getRole())
                .createdTime(author.getCreatedTime())
                .postCount(author.getPostList().size())
                .build();
    }
}
