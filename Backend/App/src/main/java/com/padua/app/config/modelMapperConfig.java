package com.padua.app.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class modelMapperConfig {

    @Bean
    public ModelMapper ModelMapper(){
        ModelMapper modelMapper = new ModelMapper();

        return modelMapper;
    }
}
