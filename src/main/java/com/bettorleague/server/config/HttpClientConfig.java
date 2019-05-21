package com.bettorleague.server.config;


import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class HttpClientConfig {


    @Autowired
    HttpInterceptor httpInterceptor;

    @Bean
    RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.additionalInterceptors(httpInterceptor).build();
    }

    @Bean
    ModelMapper mapper(){
        return new ModelMapper();
    }


}
