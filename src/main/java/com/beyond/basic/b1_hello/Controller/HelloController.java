package com.beyond.basic.b1_hello.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

// Controller 어노테이션을 통해 쉽게 사용자의 http 요청을 분석하고, http 응답을 생성해줌
@Controller // <- Component 어노테이션을 통해 별도의 객체를 생성할 필요가 없는, 싱글톤 객체 생성
@RequestMapping("/hello") // 클래스 차원의 url매핑시에는 RequestMapping 사용
public class HelloController {


    //    get요청의 case들
//    Case 1) 서버가 사용자에게 단순 String 데이터 return - @ResponseBody 있을 때
    @GetMapping("") // 아래의 메서드에 대한 서버의 end point를 설정
//    ResponseBody가 없고, return 타입이 String인 경우에 서버는 templates 폴더 밑에 hello World!.html을 찾아 리턴해주려 함
//    @ResponseBody
    public String helloWorld() {
        return "HelloWorld!";
    }

    //    Case 2) 서버가 사용자에게 객체를 json으로 직렬화해서 return
    @GetMapping("/json")
    @ResponseBody
    public Hello helloJson() throws JsonProcessingException {
        Hello h1 = new Hello("hong", "hong@naver.com");
//        직접 json으로 직렬화 할 필요 없이, return 타입에 객체가 있으면 자동으로 직렬화
//        ObjectMapper mapper = new ObjectMapper();
//        String result = mapper.writeValueAsString(h1);

        return h1;
    }

    //    Case 3) parameter 방식을 통해 사용자로부터 값을 수신
//    parameter의 형식 : /memberParam?id=1, /memberParam?name=hong
    @GetMapping("/memberParam")
    @ResponseBody
    public Hello memberParam(@RequestParam(value = "name") String name) {
        return new Hello(name, "dummy@naver.com");
    }

    //    Case 4) Path Variable 방식
//    PathVariable의 형식 : /member/1
//    PathVariable방식은 url을 통해 자원의 구조를 명확하게 표현할때 사용(좀 더 restful함)
    @GetMapping("/memberPath/{id}")
    @ResponseBody
    public String member(@PathVariable Long id) {
//        별도의 형변환 없이도, 매개변수에 타입지정시 자동형변환 시켜줌.
        System.out.println(id);

        return "OK";
    }

    //    Case 5) 여러개의 parameter를 사용한 방식
//    /memberTwoParams?name=hong&email=hong@naver.com
    @GetMapping("/memberTwoParams")
    @ResponseBody
    public Hello memberTwoParams(@RequestParam(value = "name") String name, @RequestParam(value = "email") String email) {
        return new Hello(name, email);
    }

    //    Case 6) parameter가 많아질 경우, 데이터 바인딩을 통해 input값 처리
//    데이터 바인딩 : parameter들을 사용하요 객체로 생성해줌.
//    /memberManyParams?name=hong&email=hong@naver.com
    @GetMapping("/memberManyParams")
    @ResponseBody
//    @ModelAttribute를 써도 되고 안써도 되는데, 명시적으로 parameter 형식의 데이터를 받겠다는 뜻
    public Hello memberManyParams(@ModelAttribute Hello hello) {
        System.out.println(hello);

        return null;
    }

    //    Case 7) 서버에서 화면을 return, 사용자로 부터 넘어오는 input을 활용하여 동적인 화면 생성
//    서버에서 화면을 렌더링해주는 ssr방식(csr은 서버가 데이터만 제공)
//    MVC(model(변수에 있는 model?), view(html 파일), controller(현재 클래스))패턴 이라고도 함
    @GetMapping("/model-param")
    public String modelParam(@RequestParam(value = "id") Long id, Model model) {
//        model 객체는 데이터를 화면에 전달해주는 역할
//        name이라는 key에 hongildong이라는 value를 key:value형태로 화면에 전달
        if (id == 1) {
            model.addAttribute("name", "honggildong");
            model.addAttribute("email", "hong@naver.com");
        } else if (id == 2) {
            model.addAttribute("name", "honggildong2");
            model.addAttribute("email", "hong2@naver.com");
        }
        return "helloworld2";
    }

    //    post요청의 case들 : urlEnccoding, multipart-formdata, json
//    Case 1) 텍스트만 있는 formdata형식
//    형식 : body부에 name=xxx&email=xxx
    @GetMapping("/form-view")
    public String formView() {
        return "formView";
    }

    @PostMapping("/form-view")
    @ResponseBody
    public String formView(@ModelAttribute Hello hello) {
//        Get요청의 url에 파라미터 방식과 동일한 데이터 형식이므로, RequestParam 또는 데이터 바인딩 방식 가능
        System.out.println(hello);
        return "OK";
    }

    //    Case 2-1) 텍스트와 파일이 있는 formdata형식(순수 html로 제출)
    @GetMapping("/form-file-view")
    public String formFileView() {
        return "formFileView";
    }

    @PostMapping("/form-file-view")
    @ResponseBody
    public String formFileView(@ModelAttribute Hello hello, @RequestParam(value = "photo") MultipartFile photo) {
        System.out.println(hello + ", " + photo.getOriginalFilename() + "(" + photo.getSize() + "bytes)");
        return "OK";
    }

    /// /        Case 2-2) 텍스트와 파일이 있는 formdata형식(js 활용)
    @GetMapping("/axios-file-view")
    public String axiosFileView() {
        return "axiosFileView";
    }

    @PostMapping("/axios-file-view")
    @ResponseBody
    // 위의 메서드 그대로 사용가능하긴 함
    public String axiosFileView(@ModelAttribute Hello hello, @RequestParam(value = "photo") MultipartFile photo) {
        System.out.println(hello + ", " + photo.getOriginalFilename() + "(" + photo.getSize() + "bytes)");
        return "OK";
    }

    //    Case 3) 텍스트와 여러개의 파일이 있는 formdata형식(js로 제출)
    @GetMapping("/axios-multi-file-view")
    public String axiosMultiFileView() {
        return "axiosMultiFileView";
    }

    @PostMapping("/axios-multi-file-view")
    @ResponseBody
    public String axiosMultiFileView(@ModelAttribute Hello hello, @RequestParam(value = "photos") List<MultipartFile> photos) {
        System.out.println(hello);
        photos.forEach((photo) -> System.out.println(photo.getOriginalFilename()));
        return "OK";
    }

    //    Case 4) json데이터 처리
    @GetMapping("/axios-json-view")
    public String axiosJsonView() {
        return "axiosJsonView";
    }

    @PostMapping("/axios-json-view")
    @ResponseBody
//    RequestBody : json형식으로 데이터가 들어올 때 객체로 자동파싱
    public String axiosJsonView(@RequestBody Hello hello) {
        System.out.println(hello);
        return "OK";
    }

    //    Case 5) 중첩된 json데이터 처리
    @GetMapping("/axios-nested-json-view")
    public String axiosNestedJsonView() {
        return "axiosNestedJsonView";
    }

    @PostMapping("/axios-nested-json-view")
    @ResponseBody
    public String axiosNestedJsonView(@RequestBody Hello hello) {
        System.out.println(hello);
        return "OK";
    }

    //    Case 6) json과 파일을 같이 처리 : text구조가 복잡(객체안의 객체 같은 경우)하여 피치 못하게 json을 써야하는 경우
//    데이터형식 : hello={name:"hong", email:"hong@naver.com"}&photo=image.png
//    단순 json 구조가 아닌 multipart-formdata 안에 json을 넣는 구조로 진행
    @GetMapping("/axios-json-file-view")
    public String axiosJsonFileView() {
        return "axiosJsonFileView";
    }

//    @PostMapping("/axios-json-file-view")
//    @ResponseBody
//    public String axiosJsonFileView(@RequestParam(value = "hello") String hello, @RequestParam(value = "photo") MultipartFile photo) {
//        ObjectMapper objectMapper = new ObjectMapper();
//        try {
//            Hello hello1 = objectMapper.readValue(hello, Hello.class);
//            System.out.println(hello1);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//
//        System.out.println(photo.getOriginalFilename());
//        return "ok";
//    }

    @PostMapping("/axios-json-file-view")
    @ResponseBody
    public String axiosJsonFileView(@RequestPart("hello") Hello hello, @RequestPart("photo") MultipartFile photo) {
//        json과 파일을 함께 처리해야할 때 RequestPart를 일반적으로 활용
        System.out.println(hello);
        System.out.println(photo.getOriginalFilename());
        return "ok";
    }

    // PathVariable
    // formdata 방식 => RequestParam => ModelAttribute
    // json 방식 => RequestBody
    // formdata 안에 json이 포함되어 있는 경우 => RequestPart로 분리해서 받아줌
}
