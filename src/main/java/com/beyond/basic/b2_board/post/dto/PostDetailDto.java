package com.beyond.basic.b2_board.post.dto;

import com.beyond.basic.b2_board.post.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostDetailDto {
    private Long id;
    private String title;
    private String contents;
    private String authorEmail;
    private String delYN;

    public static PostDetailDto fromEntity(Post post) {
        return PostDetailDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .contents(post.getContents())
                .authorEmail(post.getAuthor().getEmail())
                .delYN(post.getDelYN())
                .build();
    }


// 관계성 설정을 하지 않은 경우

//    public static PostDetailDto fromEntity(Post post, String authorEmail) {
//        return PostDetailDto.builder()
//                .id(post.getId())
//                .title(post.getTitle())
//                .contents(post.getContents())
//                .authorEmail(authorEmail)
//                .build();
//    }
}
