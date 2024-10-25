package com.nnzz.nnzz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("classpath:/application-key.properties")
public class NnzzApplication {

	public static void main(String[] args) {
		SpringApplication.run(NnzzApplication.class, args);
	}

}
