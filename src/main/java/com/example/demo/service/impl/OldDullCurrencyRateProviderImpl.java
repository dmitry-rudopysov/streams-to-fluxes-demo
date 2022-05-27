package com.example.demo.service.impl;

import com.example.demo.model.Currency;
import com.example.demo.service.OldDullCurrencyRateProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
@RequiredArgsConstructor
public class OldDullCurrencyRateProviderImpl implements OldDullCurrencyRateProvider {

    private final Map<Currency, Integer> currencyMap;
    private final AtomicInteger counter = new AtomicInteger();


    @Override
    public BigDecimal getRate(Currency currency) {
        log.info("{} Request for currency {}", counter.incrementAndGet(), currency);
        sleep();
        var fractionalPart = new Random().nextInt(0, 100);
        var integralPart = currencyMap.get(currency);
        return new BigDecimal(integralPart * 100 + fractionalPart).movePointLeft(2);
    }

    private void sleep() {
        try {
            Thread.sleep(1000);
        } catch (Exception ignore) {/*NoP*/}
    }
}
