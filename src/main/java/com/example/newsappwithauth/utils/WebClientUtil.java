package com.example.newsappwithauth.utils;

import org.springframework.beans.factory.annotation.Autowired;
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

    public Mono<String> externalGetRequestStreamAsString(String url) {
        return webClientBuilder.build()
                .get()
                .uri(url)
                .headers(headers -> headers.set("x-api-key", "01d95c8a53d4405daa4f6887379b4da7"))
                .retrieve()
                .bodyToFlux(DataBuffer.class)
                .map(dataBuffer -> {
                    // Convert each DataBuffer chunk to a string
                    return dataBuffer.toString(StandardCharsets.UTF_8);
                })
                .reduce(String::concat);  // Combine all chunks into a single string
    }

//    public Flux<DataBuffer> externalGetRequestStream(String url) {
//        return webClientBuilder.build()
//                .get()
//                .uri(url)
//                .headers(headers -> headers.set("x-api-key", "01d95c8a53d4405daa4f6887379b4da7"))
//                .retrieve()
//                .bodyToFlux(DataBuffer.class) // Streaming the response body
//                .doOnNext(dataBuffer -> {
//                    // Process each chunk of the response (dataBuffer) as it's received
//                    // Optionally, you can log or transform the data here
//                    System.out.println("Received chunk of size: " + dataBuffer.readableByteCount());
//                })
//                .doOnTerminate(() -> {
//                    // Any final logic to execute after the stream is completed
//                    System.out.println("Streaming completed.");
//                });
//    }

    // Method to retrieve the full response (using bodyToMono if the response is small enough)
    public String externalGetRequest(String url) {
        return webClientBuilder.build()
                .get()
                .uri(url)
                .headers(headers -> headers.set("x-api-key", "01d95c8a53d4405daa4f6887379b4da7"))
                .retrieve()
                .bodyToMono(String.class)
                .block(); // Blocking for now, but you can use reactive handling
    }

//    public String externalGetRequest(String url) {
//        return webClientBuilder.build()
//                .get()
//                .uri(url)
//                .headers(headers -> headers.set("x-api-key", "01d95c8a53d4405daa4f6887379b4da7"))
//                .retrieve()
////                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
////                        clientResponse -> {
////                            return Mono.error(new NotFoundException("Client Api error :NOTFOUND"));
////                        })
//                .bodyToMono(String.class)
//                .block();
//    }
}
