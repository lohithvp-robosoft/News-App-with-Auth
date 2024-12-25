package com.example.newsappwithauth.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.core.io.buffer.DataBuffer;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;


@Component
public class WebClientUtil {

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Value("${API_Key}")
    private String apiKey;

    public Mono<String> externalGetRequestStreamAsString(String url) {
        return webClientBuilder.build()
                .get()
                .uri(url)
                .headers(headers -> headers.set("x-api-key",apiKey))
                .retrieve()
                .bodyToFlux(DataBuffer.class)
                .map(dataBuffer -> {
                    return dataBuffer.toString(StandardCharsets.UTF_8);
                })
                .reduce(String::concat);
    }

//    public String externalGetRequest(String url) {
//        return webClientBuilder.build()
//                .get()
//                .uri(url)
//                .headers(headers -> headers.set("x-api-key", apikey))
//                .retrieve()
//                .bodyToMono(String.class)
//                .block();
//    }

}
