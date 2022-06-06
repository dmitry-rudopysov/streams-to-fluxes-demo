package com.example.demo.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AsciiHelper {
    GREEN("\u001b[32m"),
    RED("\u001b[31m"),
    BLUE("\u001b[34m"),
    YELLOW("\u001b[33m"),
    MAGENTA("\u001b[35m"),
    CYAN(" \u001b[36m"),
    WHITE(" \u001b[37m"),
    RESET("\u001b[0m");

    private final String code;
}
