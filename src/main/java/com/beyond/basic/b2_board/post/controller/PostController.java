package com.beyond.basic.b2_board.post.controller;

import com.beyond.basic.b2_board.author.dto.CommonDTO;
import com.beyond.basic.b2_board.post.domain.Post;
import com.beyond.basic.b2_board.post.dto.PostCreateDto;
import com.beyond.basic.b2_board.post.dto.PostDetailDto;
import com.beyond.basic.b2_board.post.dto.PostListDto;
import com.beyond.basic.b2_board.post.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping("/create")
    public ResponseEntity<?> save(@Valid @RequestBody PostCreateDto postCreateDto) {
        this.postService.save(postCreateDto);
        return new ResponseEntity<>(new CommonDTO("success", HttpStatus.CREATED.value(), "post is created"), HttpStatus.CREATED);
    }

    @GetMapping("/list")
    public ResponseEntity<?> findAll() {
        List<PostListDto> postListDtos = this.postService.findAll();
        return new ResponseEntity<>(new CommonDTO(postListDtos, HttpStatus.OK.value(), "posts are found"), HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        PostDetailDto dto = postService.findById(id);
        return new ResponseEntity<>(new CommonDTO(dto, HttpStatus.OK.value(), "post is found"), HttpStatus.OK);
    }
}
