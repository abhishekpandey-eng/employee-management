package com.employeemanagement.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.employeemanagement.dto.EmployeeAggregateResponse;
import com.employeemanagement.dto.EmployeeSalaryRange;
import com.employeemanagement.exceptions.InvalidInputException;
import com.employeemanagement.models.Employee;
import com.employeemanagement.services.IEmployeeService;
import com.employeemanagement.utilities.IApplicationConstant;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
public class EmployeeController {

	@Autowired
	private IEmployeeService employeeService;

	/**
	 * 
	 * @param place
	 * @param percentage in float format, for ex: want to give 20 percent then pass 20 as it is
	 * @return
	 */
	@PutMapping("/employee/place/{place}/salary/{percentage}")
	@ApiOperation(value = "increment the salary of employee of given place with given percentage", response = List.class)
	public ResponseEntity<List<Employee>> incrementSalary(@PathVariable @ApiParam(value = "location of an employee", required = true, type = "String", example = "Delhi") String place, @PathVariable @ApiParam(value = "percentage for salary increment", required = true, example = "40", type = "float") float percentage) {
		if(percentage <= 0.0F)
			throw new InvalidInputException("percentage must be greater than 0");
		
		List<Employee> employees = employeeService.incrementEmployeeSalary(place, percentage);

		if (CollectionUtils.isEmpty(employees))
			return new ResponseEntity<List<Employee>>(employees, HttpStatus.NO_CONTENT);

		return new ResponseEntity<List<Employee>>(employees, HttpStatus.OK);
	}

	@GetMapping("/employee/place/{place}")
	@ApiOperation(value = "return the list of employee by given place", response = List.class)
	public ResponseEntity<List<Employee>> getEmployeesByPlace(@PathVariable @ApiParam(value = "location of an employee", required = true, type = "String", example = "Lko") String place) {
		List<Employee> employees = employeeService.getEmployees(place);

		if (CollectionUtils.isEmpty(employees))
			return new ResponseEntity<List<Employee>>(employees, HttpStatus.NO_CONTENT);

		return new ResponseEntity<List<Employee>>(employees, HttpStatus.OK);
	}
	
	/**
	 * 
	 * @param employeeId
	 * @return hierarchical diagram from parent to childs 
	 */
	@GetMapping("/employee/supervisee/{employeeId}")
	@ApiOperation(value = "return the string representing hierarchy diagram of given employee id", response = String.class)
	public ResponseEntity<String> getEmployeeSupervisee(@PathVariable @ApiParam(value = "id of an employee", required = true, type = "Long", example = "1") Long employeeId) {
		String supervisee = employeeService.getEmployeeHierarcyDiagram(employeeId);

		if (StringUtils.isEmpty(supervisee))
			return new ResponseEntity<String>(supervisee, HttpStatus.NO_CONTENT);

		return new ResponseEntity<String>(supervisee, HttpStatus.OK);
	}
	
	/**
	 * 
	 * @param property: name of the employee property like bu, place, supervisor
	 * @param propertyValue: value of the property like RND for bu property
	 * @return
	 */
	@GetMapping("/employee/salary")
	@ApiOperation(value = "return the property value and total salary based on the property", response = EmployeeAggregateResponse.class)
	public ResponseEntity<EmployeeAggregateResponse> getEmployeesSalaryByProperty(@RequestParam @ApiParam(value = "property by which we need total salary", required = true, type = "String", example = "bu") String property, @ApiParam(value = "value of given property", required = true, type = "String", example = "RND") @RequestParam String propertyValue) {
		
		if(!IApplicationConstant.searchParams.containsKey(property.toUpperCase()))
			throw new InvalidInputException("given field not supported !!");
		
		EmployeeAggregateResponse response = employeeService.salaryByProperty(IApplicationConstant.searchParams.get(property.toUpperCase()), propertyValue);
		return new ResponseEntity<EmployeeAggregateResponse>(response, HttpStatus.OK);
	}
	
	@GetMapping("/employee/title/{title}/salary-range")
	@ApiOperation(value = "return the minimum and maximum salary of given title", response = EmployeeSalaryRange.class)
	public ResponseEntity<EmployeeSalaryRange> getEmployeesSalaryRange(@PathVariable @ApiParam(value = "title of an employees", required = true, type = "String", example = "SDE2") String title) {		
		EmployeeSalaryRange response = employeeService.salaryRangeByTitle(title);
		return new ResponseEntity<EmployeeSalaryRange>(response, HttpStatus.OK);
	}

}
