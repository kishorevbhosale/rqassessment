package com.example.rqchallenge.controller;

import com.example.rqchallenge.dto.Employee;
import com.example.rqchallenge.dto.RequestDTO;
import com.example.rqchallenge.dto.ResponseDTO;
import com.example.rqchallenge.dto.ResponseSearchByIdDTO;
import com.example.rqchallenge.exception.ApiError;
import com.example.rqchallenge.exception.EmployeeException;
import com.example.rqchallenge.exception.ErrorType;
import com.example.rqchallenge.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping("/employees")
    public ResponseEntity<ResponseDTO> getAllEmployee() {
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }

    @GetMapping(value = "/employee/search-by-name/{name}")
    public ResponseEntity<List<Employee>> getEmployeesByNameSearch(@PathVariable("name") String name) {
        return ResponseEntity.ok(employeeService.getEmployeesByNameSearch(name));
    }

    @GetMapping(value = "/employee/search-by-id/{id}")
    public ResponseEntity<ResponseSearchByIdDTO> getEmployeeById(@PathVariable("id") String id) {
        return ResponseEntity.ok(employeeService.getEmployeeById(id));
    }

    @GetMapping(value = "/employees/highestSalary")
    ResponseEntity<Integer> getHighestSalaryOfEmployees() {
        return ResponseEntity.ok(employeeService.getHighestSalaryOfEmployees());
    }

    @GetMapping(value = "/employees/top-ten-highest-earning-employee-names")
    ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames() {
        return ResponseEntity.ok(employeeService.getTop10HighestEarningEmployeeNames());
    }

    @PostMapping(value = "/employee/create")
    ResponseEntity<String> createEmployee(@RequestBody RequestDTO requestDTO) {
        return ResponseEntity.ok(employeeService.createEmployee(requestDTO));
    }

    @DeleteMapping("/employee/delete/{id}")
    ResponseEntity<String> deleteEmployeeById(@PathVariable String id) {
        return ResponseEntity.ok(employeeService.deleteEmployee(id));
    }

    @ExceptionHandler(value = {IllegalStateException.class, EmployeeException.class})
    public ResponseEntity<ApiError> handleRunTimeException(RuntimeException exception) {
        log.warn("Runtime exception occured during Employee service api call.", exception);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiError(HttpStatus.BAD_REQUEST, exception.getMessage(), ErrorType.EMPLOYEE_SERVICE_EXCEPTION, exception));
    }
}
