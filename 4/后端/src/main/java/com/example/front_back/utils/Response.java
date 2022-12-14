package com.example.front_back.utils;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Response {
    private Integer statusCode;
    private String message;
}
