package com.example.demo.service;

import com.example.demo.model.Currency;

import java.math.BigDecimal;

public interface OldDullCurrencyRateProvider {
    BigDecimal getRate(Currency currency);
}
