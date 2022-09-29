package com.example.rqchallenge.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;

@Builder(toBuilder = true)
@Getter
@ToString
@Jacksonized
public class ResponseSearchByIdDTO {
    private final String status;
    private final Employee data;
    private final String message;
}