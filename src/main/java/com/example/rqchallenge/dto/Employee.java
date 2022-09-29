package com.example.rqchallenge.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;

@Builder(toBuilder = true)
@Getter
@ToString
@Jacksonized
public class Employee {
    private Integer id;
    private String employee_name;
    private Integer employee_salary;
    private Integer employee_age;
    private String profile_image;
}
