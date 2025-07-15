package com.beyond.basic.b2_board.dto;

import com.beyond.basic.b2_board.domain.Author;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AuthorListDTO {
    private Long id;
    private String name;
    private String email;

    public static AuthorListDTO fromEntity(Author author) {
        return new AuthorListDTO(author.getId(), author.getName(), author.getEmail());
    }
}
