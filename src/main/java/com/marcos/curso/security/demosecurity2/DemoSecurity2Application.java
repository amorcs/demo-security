package com.marcos.curso.security.demosecurity2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoSecurity2Application {

	public static void main(String[] args) {
//		System.out.println(new BCryptPasswordEncoder().encode("Javax321"));

		SpringApplication.run(DemoSecurity2Application.class, args);
	}

}
