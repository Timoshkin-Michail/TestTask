package com.test.task.cameras.client;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.HttpClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Configuration
public class RestTemplateConfiguration {
    @Bean
    @Primary
    @Scope("prototype")
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder,
                                     @Qualifier("logInterceptor") ClientHttpRequestInterceptor logInterceptor,
                                     HttpClient httpClient) {
        RestTemplate restTemplate = restTemplateBuilder.build();
        restTemplate.setRequestFactory(new BufferingClientHttpRequestFactory(new HttpComponentsClientHttpRequestFactory(httpClient)));
        restTemplate.getInterceptors().add(logInterceptor);
        return restTemplate;
    }
}