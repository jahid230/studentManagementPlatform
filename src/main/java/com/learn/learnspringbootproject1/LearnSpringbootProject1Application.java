package com.learn.learnspringbootproject1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@SpringBootApplication
@ComponentScan("com.learn.*")
@EntityScan("com.learn.entity")
@EnableJpaRepositories("com.learn.repository")
@EnableSwagger2
public class LearnSpringbootProject1Application {

	public static void main(String[] args) {
		SpringApplication.run(LearnSpringbootProject1Application.class, args);
	}

}

