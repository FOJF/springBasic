package com.beyond.basic.b2_board.controller;

import com.beyond.basic.b2_board.domain.Author;
import com.beyond.basic.b2_board.dto.AuthorCreateDTO;
import com.beyond.basic.b2_board.dto.AuthorDetailDTO;
import com.beyond.basic.b2_board.dto.AuthorListDTO;
import com.beyond.basic.b2_board.dto.AuthorUpdatePwDTO;
import com.beyond.basic.b2_board.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController // Controller + ResponseBody (화면 리턴은 불가능)
@RequiredArgsConstructor
@RequestMapping("/author")
public class AuthorController {
    private final AuthorService authorService;
//    CRUD
//    회원가입
    @PostMapping("/create")
    @ResponseBody
//    보통 사용자의 입력과 DB에 들어가는 엔티티와 다르기 때문에 DataTransferObject(DTO) class를 새로 만들어서 사용
    public String save(@RequestBody AuthorCreateDTO authorCreateDTO) {
        try {
            this.authorService.save(authorCreateDTO);
            return "ok";
        } catch (Exception e) {
            return "이미 존재하는 이메일입니다.";
        }
    }
//    회원 목록 조회 : /author/list
    @GetMapping("/list")
    @ResponseBody
    public List<AuthorListDTO> findAll() {
        return this.authorService.findAll();
    }

//    회원 상세 조회 : id로 조회 /author/detail/1
    @GetMapping("/detail/{id}")
    @ResponseBody
    public AuthorDetailDTO findById(@PathVariable Long id) {
        try {
            return this.authorService.findById(id);
        } catch (NoSuchElementException e) {
            e.printStackTrace();
        }
        return null;
    }

//    비밀번호 수정(Patch) : /author/updatepw   email, password -> json
    @PatchMapping("/updatepw")
    @ResponseBody
    public String updatePassword(@RequestBody AuthorUpdatePwDTO authorUpdatePwDTO) {
        this.authorService.updatePassword(authorUpdatePwDTO);
        return "OK";
    }
//    회원 탈퇴 : /author/1

    @DeleteMapping("/{id}")
    @ResponseBody
    public String delete(@PathVariable Long id) {
        System.out.println(id);
        this.authorService.delete(id);
        return "OK";
    }
}
