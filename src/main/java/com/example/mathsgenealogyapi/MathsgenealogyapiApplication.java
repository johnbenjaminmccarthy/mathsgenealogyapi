package com.example.mathsgenealogyapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

@SpringBootApplication
public class MathsgenealogyapiApplication {

	private static final Logger logger = LogManager.getLogger(MathsgenealogyapiApplication.class);

	public static void main(String[] args) {
		logger.info("Hello Logging!");
		System.out.println("Hello World!");
		SpringApplication.run(MathsgenealogyapiApplication.class, args);


	}

}
