package com.joinmeds;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@ComponentScan({"com.joinmeds"})
@EnableAsync
public class JoinmedsApplication {

	public static void main(String[] args) {
		SpringApplication.run(JoinmedsApplication.class, args);
	}

}
