package com.beyond.basic.b2_board.author.controller;

import com.beyond.basic.b2_board.author.dto.AuthorCreateDTO;
import com.beyond.basic.b2_board.author.dto.AuthorDetailDTO;
import com.beyond.basic.b2_board.author.dto.AuthorListDTO;
import com.beyond.basic.b2_board.author.dto.AuthorUpdatePwDTO;
import com.beyond.basic.b2_board.common.dto.CommonDTO;
import com.beyond.basic.b2_board.author.service.AuthorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // Controller + ResponseBody (화면 리턴은 불가능)
@RequiredArgsConstructor
@RequestMapping("/author")
public class AuthorController {
    private final AuthorService authorService;

    //    CRUD
//    회원가입
    @PostMapping("/create")
//    보통 사용자의 입력과 DB에 들어가는 엔티티와 다르기 때문에 DataTransferObject(DTO) class를 새로 만들어서 사용
//    dto에 있는 NotEmpty어노테이션과 controller의 @Valid가 한 쌍이 되어 동작한다.
    public ResponseEntity<?> save(@Valid @RequestBody AuthorCreateDTO authorCreateDTO) {
//        ResponseEntity<?> response = null;
//        try {
//            Author author = this.authorService.save(authorCreateDTO);
//            response = new ResponseEntity<>(author, HttpStatus.CREATED);
//        } catch (IllegalArgumentException e) {
//            e.printStackTrace();
//            // 일반적으로는 생성자 매개변수로 응답을 줄 바디부분의 객체와 해더의 상태코드를 넣어줌
//            response = new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
//        }
//        return response;

//        controller advice가 없었으면 위와 같이 개별적인 예외처리가 필요하나, 이제는 전역적인 예외 처리가 가능하다.
        AuthorDetailDTO dto = this.authorService.save(authorCreateDTO);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    //    회원 목록 조회 : /author/list
    @GetMapping("/list")
    public List<AuthorListDTO> findAll() {
        return this.authorService.findAll();
    }

    //    회원 상세 조회 : id로 조회 /author/detail/1
    @GetMapping("/detail/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
//        try {
//            String statusMsg = "Found Author id : " + id;
//            return new ResponseEntity<>(new CommonDTO(this.authorService.findById(id), HttpStatus.OK.value(), statusMsg), HttpStatus.OK);
//        } catch (NoSuchElementException e) {
//            e.printStackTrace();
//            return new ResponseEntity<>(new CommonErrorDTO(HttpStatus.NOT_FOUND.value(), e.getMessage()), HttpStatus.NOT_FOUND);
//        }

        return new ResponseEntity<>(new CommonDTO(this.authorService.findById(id), HttpStatus.OK.value(), "author is founded"), HttpStatus.OK);
    }

    //    비밀번호 수정(Patch) : /author/updatepw   email, password -> json
    @PatchMapping("/updatepw")
    public void updatePassword(@Valid @RequestBody AuthorUpdatePwDTO authorUpdatePwDTO) {
//        try {
//            return new ResponseEntity<>(this.authorService.updatePassword(authorUpdatePwDTO), HttpStatus.OK);
//        } catch (NoSuchElementException e) {
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
//        }
        this.authorService.updatePassword(authorUpdatePwDTO);
    }
//    회원 탈퇴 : /author/1

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        this.authorService.delete(id);
        return "OK";
    }
}
