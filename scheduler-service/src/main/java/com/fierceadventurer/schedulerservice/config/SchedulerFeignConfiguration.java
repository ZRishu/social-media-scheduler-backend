package com.fierceadventurer.schedulerservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import feign.Client;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.hc5.ApacheHttp5Client;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import org.springframework.context.annotation.Bean;


public class SchedulerFeignConfiguration {

    @Bean
    public Client client() {
        return new ApacheHttp5Client();
    }

    @Bean
    public ObjectMapper customObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new Jdk8Module());
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }

    @Bean
    public Encoder feignEncoder(ObjectMapper customObjectMapper) {
        return new JacksonEncoder(customObjectMapper);
    }

    @Bean
    public Decoder feignDecoder(ObjectMapper customObjectMapper) {
        return new JacksonDecoder(customObjectMapper);
    }

    @Bean
    feign.Logger.Level feignLoggerLevel() {
        return feign.Logger.Level.FULL;
    }

    @Bean
    public feign.RequestInterceptor contentLengthRemover() {
        return template -> {
            template.removeHeader("Content-Length");
        };
    }
}