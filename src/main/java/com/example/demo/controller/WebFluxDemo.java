package com.example.demo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.stream.IntStream;

@Slf4j
@RestController
public class WebFluxDemo {

    @GetMapping("/count")
    Flux<String> giveMeOK() {
        return Flux.fromStream(IntStream.range(1, 6).boxed()
                        .map(String::valueOf)
                        .map(s -> s + System.lineSeparator())).concatWithValues("That's all Folks!")
                .delayElements(Duration.ofSeconds(3))
                ;
    }
}
