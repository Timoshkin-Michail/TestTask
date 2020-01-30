package com.test.task.cameras.client;

import org.apache.http.HttpHost;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.NoConnectionReuseStrategy;
import org.apache.http.impl.client.DefaultClientConnectionReuseStrategy;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class HttpClientConfiguration {
    @Bean
    public HttpClient httpClient(@Value("${http.timeout:60000}") int timeout,
                                 @Value("${http.keepAlive:true}") boolean keepAlive,
                                 @Value("${http.connectionTtl:60000}") int connectionTtl,
                                 @Value("${http.proxy:}") String proxy,
                                 @Value("${http.maxConnections:5}") int maxConnections) {
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(timeout)
                .setConnectionRequestTimeout(timeout)
                .setSocketTimeout(timeout)
                .build();
        if (!proxy.isEmpty()) {
            requestConfig = RequestConfig.copy(requestConfig)
                    .setProxy(HttpHost.create(proxy))
                    .build();
        }
        return HttpClients.custom()
                .useSystemProperties()
                .setConnectionReuseStrategy(
                        keepAlive ? DefaultClientConnectionReuseStrategy.INSTANCE : NoConnectionReuseStrategy.INSTANCE
                )
                .setDefaultRequestConfig(requestConfig)
                .setMaxConnPerRoute(maxConnections)
                .setMaxConnTotal(3 * maxConnections)
                .setConnectionTimeToLive(connectionTtl, TimeUnit.MILLISECONDS)
                .build();
    }
}