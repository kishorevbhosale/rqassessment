package com.example.rqchallenge.controller

import com.example.rqchallenge.BaseTestData
import com.example.rqchallenge.dto.RequestDTO
import com.example.rqchallenge.service.EmployeeService
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import spock.lang.Specification

import java.nio.charset.Charset

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup

class EmployeeControllerSpec extends Specification {

    private static final String status = "success"
    private static final String message = "Successfully! Record has been fetched."

    EmployeeService employeeService = Mock()

    MockMvc mockMvc
    ObjectMapper objectMapper = new ObjectMapper()

    EmployeeController employeeController

    void setup() {
        employeeController = new EmployeeController(employeeService)
        mockMvc = standaloneSetup(employeeController).build()
    }

    def "should give all employee details"() {
        given:
        def responseDTO = BaseTestData.getAllEmployees()

        when:
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(MediaType.TEXT_PLAIN_VALUE))

        then:
        1 * employeeService.getAllEmployees() >> responseDTO
        resultActions
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath('$.status').value(status))
                .andExpect(MockMvcResultMatchers.jsonPath('$.message').value(message))
                .andExpect(MockMvcResultMatchers.jsonPath('$.data.size()').value(1))
    }

    def "should search employees by name"() {
        given:
        def employee = BaseTestData.getEmployee()

        when:
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/employee/search-by-name/{name}", "test")
                .contentType(MediaType.APPLICATION_JSON)
                .content(MediaType.TEXT_PLAIN_VALUE))

        then:
        1 * employeeService.getEmployeesByNameSearch("test") >> List.of(employee)
        resultActions
                .andExpect(MockMvcResultMatchers.status().isOk())
    }

    def "should search employees by id"() {
        given:
        def searchByIdDTO = BaseTestData.responseSearchByIdDTO()

        when:
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/employee/search-by-id/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(MediaType.TEXT_PLAIN_VALUE))

        then:
        1 * employeeService.getEmployeeById("1") >> searchByIdDTO
        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
        resultActions.andExpect(MockMvcResultMatchers.jsonPath('$.status').value(status))
        resultActions.andExpect(MockMvcResultMatchers.jsonPath('$.message').value(message))
        resultActions.andExpect(MockMvcResultMatchers.jsonPath('$.data.id').value(1))
        resultActions.andExpect(MockMvcResultMatchers.jsonPath('$.data.employee_name').value("test emp"))
    }

    def "should give the highest salary of employees"() {
        when:
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/employees/highestSalary")
                .contentType(MediaType.APPLICATION_JSON))

        then:
        1 * employeeService.getHighestSalaryOfEmployees() >> 12345
        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
        resultActions.andExpect(MockMvcResultMatchers.jsonPath('$').value(12345))
    }

    def "should give top ten highest earning employee name"() {
        given:
        def employeeList = BaseTestData.employeeList

        when:
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/employees/top-ten-highest-earning-employee-names")
                .contentType(MediaType.APPLICATION_JSON))
        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
        def contentAsString = resultActions.andReturn().getResponse().getContentAsString(Charset.defaultCharset())

        then:
        1 * employeeService.getTop10HighestEarningEmployeeNames() >> employeeList
        contentAsString.containsIgnoreCase("Tiger")
        !contentAsString.containsIgnoreCase("Tigershrof")
    }

    def "should create the employee"() {
        given:
        def requestDTO = BaseTestData.getRequestDTO()

        when:
        def resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/employee/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(requestDTO)))

        then:
        1 * employeeService.createEmployee(_ as RequestDTO) >> "success"
        resultActions.andExpect(MockMvcResultMatchers.status().isOk())

    }

    def "shoukd delete the employee"(){
        when:
        def resultActions = mockMvc.perform(MockMvcRequestBuilders.delete("/employee/delete/{id}", "1")
                .contentType(MediaType.APPLICATION_JSON))

        then:
        1 * employeeService.deleteEmployee("1") >> "test emp"
        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
        resultActions.andExpect(MockMvcResultMatchers.jsonPath('$').value("test emp"))
    }
}
