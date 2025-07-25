package com.beyond.basic.b2_board.post.dto;

import com.beyond.basic.b2_board.author.domain.Author;
import com.beyond.basic.b2_board.post.domain.Post;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostCreateDto {
    private String category;
    @NotEmpty
    private String title;
    private String contents;
    @Builder.Default
    private Boolean isBooked = false;
    private String bookedTime; // 시간대는 직접 형변환하는 것을 추천 -> 원하는대로 형변환 되지 않을 때가 자주 있음

//    @NotNull // 숫자는 NotEmpty사용불가
//    private Long authorId;

    public Post toEntity(Author author) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime bookedTime = null;

        if (this.bookedTime != null)
            bookedTime = LocalDateTime.parse(this.bookedTime, dtf);

        return Post.builder()
                .category(this.category)
                .title(this.title)
                .contents(this.contents)
                .author(author)
                .isBooked(this.isBooked)
                .bookedTime(bookedTime)
                .build();
    }
}
