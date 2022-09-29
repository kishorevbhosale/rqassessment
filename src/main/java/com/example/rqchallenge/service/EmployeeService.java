package com.example.rqchallenge.service;

import com.example.rqchallenge.dto.Employee;
import com.example.rqchallenge.dto.RequestDTO;
import com.example.rqchallenge.dto.ResponseDTO;
import com.example.rqchallenge.dto.ResponseSearchByIdDTO;
import com.example.rqchallenge.exception.EmployeeException;
import com.example.rqchallenge.exception.ErrorType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class EmployeeService {

    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;
    private final String baseUrl;

    public EmployeeService(ObjectMapper objectMapper,
                           RestTemplate restTemplate,
                           @Value("${service.url}") String baseUrl) {
        this.objectMapper = objectMapper;
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    public ResponseDTO getAllEmployees() {
        try {
            ResponseEntity<ResponseDTO> responseEntity = restTemplate.getForEntity(baseUrl + "/employees", ResponseDTO.class);
            ResponseDTO responseDTO = responseEntity.getBody();
            if (responseDTO == null) {
                throw new IllegalStateException("Get all employee response should not be null.");
            }
            log.debug("response received for all employees: {}", objectMapper.writeValueAsString(responseDTO));
            return responseEntity.getBody();
        } catch (HttpClientErrorException | JsonProcessingException e) {
            log.error("Error while fetching all employee details : {}", e.getMessage());
            throw new EmployeeException(e.getMessage());
        }
    }

    public List<Employee> getEmployeesByNameSearch(String name) {
        try {
            ResponseEntity<ResponseDTO> responseEntity = restTemplate.getForEntity(baseUrl + "/employees", ResponseDTO.class);
            ResponseDTO responseDTO = responseEntity.getBody();
            log.debug("response received for search name: {}", objectMapper.writeValueAsString(responseDTO));
            return Objects.requireNonNull(responseDTO).getData().stream()
                    .filter(employee -> employee.getEmployee_name().contains(name))
                    .collect(Collectors.toList());
        } catch (HttpClientErrorException | JsonProcessingException e) {
            log.error("Error while fetching employees details by name: {}", e.getMessage());
            throw new EmployeeException(e.getMessage());
        }
    }

    public ResponseSearchByIdDTO getEmployeeById(String id) {
        try {
            ResponseEntity<ResponseSearchByIdDTO> responseEntity = restTemplate.getForEntity(baseUrl + "/employee/" + id, ResponseSearchByIdDTO.class);
            ResponseSearchByIdDTO responseDTO = responseEntity.getBody();
            log.debug("response received for search name: {}", objectMapper.writeValueAsString(responseDTO));
            return responseDTO;
        } catch (HttpClientErrorException | JsonProcessingException e) {
            log.error("Error while fetching employees details by id: {}", e.getMessage());
            throw new EmployeeException(e.getMessage());
        }
    }

    public Integer getHighestSalaryOfEmployees() {
        List<Employee> employees = getAllEmployees().getData();
        Optional<Employee> employee = employees.stream().max(Comparator.comparingInt(Employee::getEmployee_salary));
        return employee.map(Employee::getEmployee_salary).orElseThrow(() -> new EmployeeException(ErrorType.BUSINESS_EXCEPTION.name()));
    }

    public List<String> getTop10HighestEarningEmployeeNames() {
        List<Employee> employees = getAllEmployees().getData();
        log.debug("Total employees  :{}", employees.size());
        Map<Integer, String> empMap = employees.stream()
                .collect(Collectors.groupingBy(Employee::getEmployee_salary,
                        Collectors.collectingAndThen(Collectors.maxBy(Comparator.comparingDouble(Employee::getEmployee_salary)), value -> value.get().getEmployee_name())));
        return new ArrayList<>(new TreeMap<>(empMap).descendingMap().values()).subList(0, 10);
    }

    public String createEmployee(RequestDTO requestDTO) {
        try {
            ResponseEntity<ResponseSearchByIdDTO> responseEntity = restTemplate.postForEntity(baseUrl + "/create", requestDTO, ResponseSearchByIdDTO.class);
            log.debug("response code after creating employee: {}", objectMapper.writeValueAsString(responseEntity.getStatusCodeValue()));
            return Objects.requireNonNull(responseEntity.getBody()).getStatus();
        } catch (HttpClientErrorException | JsonProcessingException e) {
            log.error("Error while creating new employee : {}", e.getMessage());
            throw new EmployeeException(e.getMessage());
        }
    }

    public String deleteEmployee(String id) {
        String name = getEmployeeById(id).getData().getEmployee_name();
        try {
            restTemplate.delete(baseUrl + "/delete/" + id);
            log.debug("employee id : {} name : {} is deleted", id, name);
            return name;
        } catch (HttpClientErrorException e) {
            log.error("Error while deleting employee id : {}, error : {}", id, e.getMessage());
            throw new EmployeeException(e.getMessage());
        }
    }
}
