package com.joinmeds;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.joinmeds"})
public class JoinmedsApplication {

	public static void main(String[] args) {
		SpringApplication.run(JoinmedsApplication.class, args);
	}

}
