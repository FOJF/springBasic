package com.beyond.basic.b2_board.author.dto;

import com.beyond.basic.b2_board.author.domain.Author;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class AuthorListDTO {
    private Long id;
    private String name;
    private String email;

    public static AuthorListDTO fromEntity(Author author) {
        return AuthorListDTO.builder()
                .id(author.getId())
                .name(author.getName())
                .email(author.getEmail())
                .build();
    }
}
