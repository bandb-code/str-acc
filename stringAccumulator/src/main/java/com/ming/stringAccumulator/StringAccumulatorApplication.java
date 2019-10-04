package com.ming.stringAccumulator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StringAccumulatorApplication {

	@Autowired
	public StringAccumulator stringAccumulator;

	public static void main(String[] args) {
		SpringApplication.run(StringAccumulatorApplication.class, args);
    }
}
