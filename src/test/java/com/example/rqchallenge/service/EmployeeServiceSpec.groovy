package com.example.rqchallenge.service

import com.example.rqchallenge.BaseTestData
import com.example.rqchallenge.dto.ResponseDTO
import com.example.rqchallenge.dto.ResponseSearchByIdDTO
import com.example.rqchallenge.exception.EmployeeException
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate
import spock.lang.Specification

class EmployeeServiceSpec extends Specification {
    RestTemplate restTemplate = Mock()
    ObjectMapper objectMapper = new ObjectMapper()
    String baseUrl = "https://dummy.restapiexample.com/api/v1"

    EmployeeService employeeService

    void setup(){
        employeeService = new EmployeeService(objectMapper, restTemplate, baseUrl)
    }

    def "should give all employee details"(){
        given:
        def responseDto = BaseTestData.getAllEmployees()

        when:
        def result = employeeService.getAllEmployees()

        then:
        1 * restTemplate.getForEntity(baseUrl + "/employees", ResponseDTO.class) >> ResponseEntity.ok(responseDto)
        result == responseDto
    }

    def "should search employees by name"(){
        given:
        def responseDto = BaseTestData.getAllEmployees()

        when:
        def result = employeeService.getEmployeesByNameSearch("test")

        then:
        1 * restTemplate.getForEntity(baseUrl + "/employees", ResponseDTO.class) >> ResponseEntity.ok(responseDto)
        result == responseDto.getData()
    }

    def "should give employee by Id"(){
        given:
        def responseDto = BaseTestData.responseSearchByIdDTO()

        when:
        def result = employeeService.getEmployeeById("1")

        then:
        1 * restTemplate.getForEntity(baseUrl + "/employee/1", ResponseSearchByIdDTO.class) >> ResponseEntity.ok(responseDto)
        result == responseDto
    }

    def "should give highest salary of employee"(){
        given:
        def responseDto = BaseTestData.getAllEmployees()

        when:
        def result = employeeService.getHighestSalaryOfEmployees()

        then:
        1 * restTemplate.getForEntity(baseUrl + "/employees", ResponseDTO.class) >> ResponseEntity.ok(responseDto)
        result == 12345
    }

    def "should give top 10 hig salaried employee name"(){
        given:
        def responseDto = BaseTestData.get10Employees()

        when:
        def result = employeeService.getTop10HighestEarningEmployeeNames()

        then:
        1 * restTemplate.getForEntity(baseUrl + "/employees", ResponseDTO.class) >> ResponseEntity.ok(responseDto)
        result.size()== 10
    }

    def "should create the employee"(){
        given:
        def requestDTO = BaseTestData.getRequestDTO()
        def responseDTO = BaseTestData.responseSearchByIdDTO()

        when:
        def result = employeeService.createEmployee(requestDTO)

        then:
        1 * restTemplate.postForEntity(baseUrl + "/create", requestDTO, ResponseSearchByIdDTO.class) >> ResponseEntity.ok(responseDTO)
        result == "success"
    }

    def "should delete the employee"(){
        given:
        def responseDto = BaseTestData.responseSearchByIdDTO()

        when:
        def result = employeeService.deleteEmployee("1")

        then:
        1 * restTemplate.getForEntity(baseUrl + "/employee/1", ResponseSearchByIdDTO.class) >> ResponseEntity.ok(responseDto)
        1 * restTemplate.delete(baseUrl + "/delete/1");
        result == "test emp"
    }

    def "should throw exception while fetching all employee details"(){
        when:
        employeeService.getAllEmployees()

        then:
        1 * restTemplate.getForEntity(baseUrl + "/employees", ResponseDTO.class) >>
                {
                 throw  new HttpClientErrorException(HttpStatus.BAD_REQUEST)
                }
        def e = thrown(EmployeeException)
        e.getMessage() == "400 BAD_REQUEST"
    }
}