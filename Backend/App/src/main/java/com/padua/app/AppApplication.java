package com.padua.app;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AppApplication {

	@Bean
	public ModelMapper ModelMapper(){
		ModelMapper modelMapper = new ModelMapper();

		return modelMapper;
	}

	public static void main(String[] args) {
		SpringApplication.run(AppApplication.class, args);
	}

}
