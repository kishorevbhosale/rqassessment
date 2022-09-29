package com.example.rqchallenge.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;

@Builder(toBuilder = true)
@Getter
@ToString
@Jacksonized
public class RequestDTO {
    private String name;
    private Integer salary;
    private Integer age;
}
