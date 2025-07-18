package com.beyond.basic.b2_board.post.controller;

import com.beyond.basic.b2_board.common.dto.CommonDTO;
import com.beyond.basic.b2_board.post.dto.PostCreateDto;
import com.beyond.basic.b2_board.post.dto.PostDetailDto;
import com.beyond.basic.b2_board.post.dto.PostListDto;
import com.beyond.basic.b2_board.post.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
    // 데이터 요청 예시 : 8080/post/list?page=0&size=20&sort=title&direction=asc
    public ResponseEntity<?> findAll() {
        List<PostListDto> dtos = this.postService.findAll();
        return new ResponseEntity<>(new CommonDTO(dtos, HttpStatus.OK.value(), "posts are found"), HttpStatus.OK);
    }

    @GetMapping("/list/pagination")
    // 데이터 요청 예시 : 8080/post/list?page=0&size=20&sort=title,asc
    public ResponseEntity<?> findAll(@PageableDefault(size = 2, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<PostListDto> dtos = this.postService.findAllByDelYN(pageable);
        return new ResponseEntity<>(new CommonDTO(dtos, HttpStatus.OK.value(), "posts are found"), HttpStatus.OK);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        PostDetailDto dto = postService.findById(id);
        return new ResponseEntity<>(new CommonDTO(dto, HttpStatus.OK.value(), "post is found"), HttpStatus.OK);
    }
}
