package com.nnzz.nnzz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@PropertySource("classpath:/application-key.properties")
@EnableScheduling
public class NnzzApplication {

	public static void main(String[] args) {
		SpringApplication.run(NnzzApplication.class, args);
	}
}
