package com.example.demo.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Getter
public enum Currency {

    USD(1),
    EUR(2),
    CHF(3),
    JPY(4);

    private static final Random random = new Random();
    private static final Map<Integer, Currency> map = Arrays.stream(Currency.values())
            .collect(Collectors.toMap(Currency::getNumber, Function.identity()));
    private final int number;

    public static Currency getRandomCurrency() {
        return map.get(random.nextInt(1, 4));
    }
}
