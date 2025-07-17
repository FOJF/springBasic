//package com.beyond.basic.b2_board.controller;
//
//import com.beyond.basic.b2_board.domain.Author;
//import com.beyond.basic.b2_board.dto.CommonDTO;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseStatus;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/response/entity")
//public class ResponseEntityController {
//
//    //    case 1. 어노테이션 방식
//    @ResponseStatus
//    @GetMapping("/annotation")
//    public String annotation() {
//        return "OK";
//    }
//
//    //    case 2. 메서드 체이닝 방식
//    @GetMapping("/channing")
//    public ResponseEntity<?> channing() {
//        Author author = new Author("test", "test@naver.com", "1234");
//        return ResponseEntity.status(HttpStatus.OK).body(author);
//    }
//
//    //    case 3. ResponseEntity 객체를 직접 생성하는 방식(가장 많이 사용)
//    @GetMapping("/custom")
//    public ResponseEntity<?> custom() {
//        Author author = new Author("test", "test@naver.com", "1234");
//        return new ResponseEntity<>(author, HttpStatus.CREATED);
//    }
//
//    @GetMapping("/customDTO")
//    public ResponseEntity<?> customDTO() {
//        Author author = new Author("test", "test@naver.com", "1234");
//        return new ResponseEntity<>(new CommonDTO(author, HttpStatus.CREATED.value(), "Author is created successfully"), HttpStatus.CREATED);
//    }
//}
