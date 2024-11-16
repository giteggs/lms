package com.assesment.lms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class LMSApp {

	public static void main(String[] args) {
		SpringApplication.run(LMSApp.class, args);
	}

}
