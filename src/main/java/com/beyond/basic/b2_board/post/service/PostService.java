package com.beyond.basic.b2_board.post.service;

import com.beyond.basic.b2_board.author.domain.Author;
import com.beyond.basic.b2_board.author.repository.AuthorRepository;
import com.beyond.basic.b2_board.author.service.AuthorService;
import com.beyond.basic.b2_board.post.domain.Post;
import com.beyond.basic.b2_board.post.dto.PostCreateDto;
import com.beyond.basic.b2_board.post.dto.PostDetailDto;
import com.beyond.basic.b2_board.post.dto.PostListDto;
import com.beyond.basic.b2_board.post.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
//    private final AuthorService authorService; // 의도치 않게 순환참조가 발생할 가능성이 있어서 별로임. 아래와 같이 Repository를 사용하는 것이 어지간하면 더 좋아보임
    private final AuthorRepository authorRepository;

    public void save(PostCreateDto dto) {
        Author author = this.authorRepository.findById(dto.getAuthorId()).orElseThrow(() -> new EntityNotFoundException("없는 ID 입니다."));
        this.postRepository.save(dto.toEntity(author));
    }

    public PostDetailDto findById(Long id) {
        Post post = this.postRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("없는 ID 입니다."));
// 엔티티간의 관계성 설정을 하지 않았을 경우
//        String email = this.authorRepository.findById(post.getAuthorId()).orElseThrow(() -> new EntityNotFoundException("없는 Author ID 입니다.")).getEmail();
//        return PostDetailDto.fromEntity(post, email);

//        엔티티간의 관계성이 설정되어 있는 경우
        return PostDetailDto.fromEntity(post);
    }

    public List<PostListDto> findAll() {
        return this.postRepository.findAll().stream().map(PostListDto::fromEntity).toList();
    }
}
