package com.ivan.Flowers.Shop.configs;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Configuration
    public class ApplicationBeanConfiguration {

        @Bean
        ModelMapper modelMapper(){
            return  new ModelMapper();
        }

    }
}