package com.beyond.basic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

// ComponentScan은 Application파일을 포함한 경로 하위의 요소들만 Scan 가능
@SpringBootApplication
@ServletComponentScan
@EnableScheduling // 스케쥴러 사용시 필요한 어노테이션
public class BasicApplication {

	public static void main(String[] args) {
		SpringApplication.run(BasicApplication.class, args);
	}

}
