package com.example.demo;

import com.example.demo.service.CurrencyCalculator;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.example.demo.model.Currency.getRandomCurrency;
import static com.example.demo.util.AsciiHelper.*;

@Slf4j
@SpringBootTest(classes = StreamsToFluxesDemoApplication.class)
public class StreamsToFluxesDemo {

    private final Random random = new Random();
    @Autowired
    CurrencyCalculator currencyCalculator;

    @Test
    void SimpleStreamTest() {

        IntStream.range(1, 300)
                .parallel()
                .map(__ -> random.nextInt(100, 1000))
                .mapToObj(BigDecimal::new)
                .map(rub -> currencyCalculator.getRates(getRandomCurrency(), rub))
                .peek(result -> log.info(GREEN.getCode() + "Conversion result {}" + RESET.getCode(), result))
                .toList();
    }

    @Test
    void TransformToFlux() throws Exception {
        IntStream.range(1, 1000)
                .map(__ -> random.nextInt(100, 1000))
                .mapToObj(BigDecimal::new)
                .forEach(rub -> currencyCalculator.requestForRates(getRandomCurrency(), rub));

        currencyCalculator.getRatesFlux()
                .subscribe(result -> log.info(GREEN.getCode() + "Conversion result {}" + RESET.getCode(), result));

        Thread.sleep(15000); // Necessary for test only
    }


    @Test
    void AdvancedSubscribe() throws Exception {
        IntStream.range(1, 10)
                .map(__ -> random.nextInt(100, 1000))
                .mapToObj(BigDecimal::new)
                .forEach(rub -> currencyCalculator.requestForRates(getRandomCurrency(), rub));

        currencyCalculator.complete();

        currencyCalculator.getRatesFlux()
                .subscribe(result -> log.info(GREEN.getCode() + "Conversion result {}" + RESET.getCode(), result),
                        err -> log.error(RED.getCode() + "Some Error" + RESET.getCode(), err),
                        () -> log.info("Completed!"));

        Thread.sleep(1000); // Necessary for test only
    }

    @Test
    void ReadFileAsStreamDemo() throws Exception {
        Files.lines(Path.of("in.txt"))
                .parallel()
                .map(rub -> currencyCalculator.getRates(getRandomCurrency(), new BigDecimal(rub)))
                .peek(result -> log.info(GREEN.getCode() + "Conversion result {}" + RESET.getCode(), result))
                .toList();
    }

    @Test
    void ReadFileAsFluxDemo() throws Exception {
        Flux.using(() -> Files.lines(Path.of("in.txt")), Flux::fromStream, Stream::close)
                .subscribeOn(Schedulers.newParallel("file-copy", 3))
                .flatMap(rub -> currencyCalculator.getRatesMono(getRandomCurrency(), new BigDecimal(rub)))
                .subscribe(result -> log.info(GREEN.getCode() + "Conversion result {}" + RESET.getCode(), result),
                        err -> log.error(RED.getCode() + "Some Error" + RESET.getCode(), err),
                        () -> log.info("Completed!"));


        Thread.sleep(15000); // Necessary for test only
    }

    @Test
    void onErrorContinueDemo() throws Exception {

        Flux.fromStream(IntStream.range(1, 100).boxed())
                .delayElements(Duration.ofMillis(500))
                .map(__ -> {
                    // Sometimes we got error...
                    if (Instant.now().getEpochSecond() % 3 == 0) {
                        log.info(RED.getCode() + "It's time to fall! {}" + RESET.getCode(), __);
                        throw new RuntimeException("It't time to fall!");
                    }
                    return __;
                })
                .onErrorContinue((t, n) -> log.info(YELLOW.getCode() + "Next number continue {}" + RESET.getCode(), n))
                .subscribe(result -> log.info(GREEN.getCode() + "Next number is {}" + RESET.getCode(), result),
                        err -> log.error(RED.getCode() + "Some Error" + RESET.getCode(), err),
                        () -> log.info("Completed!"));

        Thread.sleep(10000); // Necessary for test only
    }
}
