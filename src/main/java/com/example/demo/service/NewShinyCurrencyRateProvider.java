package com.example.demo.service;

import com.example.demo.model.Currency;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public interface NewShinyCurrencyRateProvider {

    Mono<BigDecimal> getRateMono(Currency currency);
}
