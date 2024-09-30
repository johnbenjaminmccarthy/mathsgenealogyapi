package com.mathsgenealogyapi;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class MathsgenealogyapiApplication {

	private static final Logger logger = LoggerFactory.getLogger(MathsgenealogyapiApplication.class);

	public static void main(String[] args) throws Exception {

		SpringApplication.run(MathsgenealogyapiApplication.class, args);
		logger.info("Hello Logging!");
		logger.debug("Hello debugging!");
		logger.error("Hello erroring!");



	}

}
