package com.example.rqchallenge.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Builder(toBuilder = true)
@Getter
@ToString
@Jacksonized
public class ResponseDTO {
    private final String status;
    private final List<Employee> data;
    private final String message;
}
