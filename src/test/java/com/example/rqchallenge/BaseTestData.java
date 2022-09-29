package com.example.rqchallenge;

import com.example.rqchallenge.dto.Employee;
import com.example.rqchallenge.dto.RequestDTO;
import com.example.rqchallenge.dto.ResponseDTO;
import com.example.rqchallenge.dto.ResponseSearchByIdDTO;

import java.util.List;

public abstract class BaseTestData {

    public static List<String> employeeList = List.of("Tiger", "Garrett", "Ashton", "Cedric", "Airi", "Brielle", "Herrod", "Rhona", "Colleen", "Sonya");

    public static ResponseDTO get10Employees() {
        return ResponseDTO.builder()
                .status("success")
                .data(List.of(getEmployee(12), getEmployee(23),
                        getEmployee(25), getEmployee(100),
                        getEmployee(90), getEmployee(41),
                        getEmployee(35), getEmployee(57),
                        getEmployee(10), getEmployee(63),
                        getEmployee(21), getEmployee(65)))
                .message("Successfully! Record has been fetched.")
                .build();
    }

    public static ResponseDTO getAllEmployees() {
        return ResponseDTO.builder()
                .status("success")
                .data(List.of(getEmployee()))
                .message("Successfully! Record has been fetched.")
                .build();
    }

    public static Employee getEmployee(int sal){
        return Employee.builder()
                .id(1)
                .employee_name("test emp")
                .employee_salary(sal)
                .employee_age(12)
                .profile_image("")
                .build();
    }

    public static Employee getEmployee() {
        return Employee.builder()
                .id(1)
                .employee_name("test emp")
                .employee_salary(12345)
                .employee_age(12)
                .profile_image("")
                .build();
    }

    public static ResponseSearchByIdDTO responseSearchByIdDTO() {
        return ResponseSearchByIdDTO.builder()
                .status("success")
                .message("Successfully! Record has been fetched.")
                .data(getEmployee())
                .build();
    }

    public static RequestDTO getRequestDTO() {
        return RequestDTO.builder()
                .name("test emp")
                .salary(12345)
                .age(25)
                .build();
    }

}
