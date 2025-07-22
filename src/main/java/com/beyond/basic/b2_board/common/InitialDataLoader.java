package com.beyond.basic.b2_board.common;


import com.beyond.basic.b2_board.author.domain.Author;
import com.beyond.basic.b2_board.author.domain.Role;
import com.beyond.basic.b2_board.author.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

//CommandLineRunner를 implements 해서 해당 컴포넌트가 스프링빈으로 등록되는 시점에 run 메서드 자동 실행
@Component
@RequiredArgsConstructor
public class InitialDataLoader implements CommandLineRunner {
    private final AuthorRepository authorRepository;
    private final PasswordEncoder passwordEncoder;
    @Value("${jwt.adminEmail}")
    private String email;

    @Value("${jwt.adminPassword}")
    private String password;

    @Override
    public void run(String... args) throws Exception {
        if (authorRepository.findByEmail(email).isPresent()) return;

        Author author = Author.builder()
                .email(email)
                .role(Role.ADMIN)
                .password(passwordEncoder.encode(password))
                .build();

        authorRepository.save(author);
    }
}
