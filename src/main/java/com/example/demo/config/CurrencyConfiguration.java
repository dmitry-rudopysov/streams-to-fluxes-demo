package com.example.demo.config;

import com.example.demo.model.Currency;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

import static com.example.demo.model.Currency.*;

@Configuration
public class CurrencyConfiguration {

    @Bean
    Map<Currency, Integer> currencyMap() {
        return Map.of(USD, 63,
                EUR, 66,
                JPY, 49,
                CHF, 63);
    }
}
