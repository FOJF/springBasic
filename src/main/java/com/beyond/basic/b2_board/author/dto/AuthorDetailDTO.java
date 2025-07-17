package com.beyond.basic.b2_board.author.dto;

import com.beyond.basic.b2_board.author.domain.Author;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AuthorDetailDTO {
    private Long id;
    private String name;
    private String email;

    //    1개의 entity로만 dto가 조립되는 것이 아니기에, dto클래스에서 fromEntity를 설계.
    public static AuthorDetailDTO fromEntity(Author author) {
        return new AuthorDetailDTO(author.getId(), author.getName(), author.getEmail());
    }
}
