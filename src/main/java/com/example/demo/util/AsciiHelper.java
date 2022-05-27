package com.example.demo.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AsciiHelper {
    GREEN("\u001b[32m"),
    RED("\u001b[31m"),
    RESET("\u001b[0m");

    private final String code;
}
