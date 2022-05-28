package com.example.demo.service;

import com.example.demo.model.Currency;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.math.BigDecimal;


@Slf4j
@Service
@RequiredArgsConstructor
public class CurrencyCalculator {

    private final NewShinyCurrencyRateProvider newShinyCurrencyRateProvider;
    private final OldDullCurrencyRateProvider oldDullCurrencyRateProvider;
    private final Sinks.Many<Request> sinks = Sinks.many().multicast().onBackpressureBuffer();


    public BigDecimal getRates(Currency currency, BigDecimal rub) {
        return oldDullCurrencyRateProvider.getRate(currency).multiply(rub);
    }

    public Mono<BigDecimal> getRatesMono(Currency currency, BigDecimal rub) {
        return newShinyCurrencyRateProvider.getRateMono(currency)
                .map(rate -> rate.multiply(rub));
    }

    public void complete() {
        sinks.tryEmitComplete();
    }

    public void requestForRates(Currency currency, BigDecimal rub) {
        sinks.tryEmitNext(new Request(currency, rub));
    }

    public Flux<BigDecimal> getRatesFlux() {
        return sinks.asFlux()
                .flatMap(req -> newShinyCurrencyRateProvider.getRateMono(req.currency()).map(rate -> rate.multiply(req.rub())));
    }

    private record Request(Currency currency, BigDecimal rub) {
    }
}
