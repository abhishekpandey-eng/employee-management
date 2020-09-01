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
	public ResponseEntity<List<Employee>> incrementSalary(@PathVariable String place, @PathVariable float percentage) {
		List<Employee> employees = employeeService.incrementEmployeeSalary(place, percentage);

		if (CollectionUtils.isEmpty(employees))
			return new ResponseEntity<List<Employee>>(employees, HttpStatus.NO_CONTENT);

		return new ResponseEntity<List<Employee>>(employees, HttpStatus.OK);
	}

	@GetMapping("/employee/place/{place}")
	public ResponseEntity<List<Employee>> getEmployeesByPlace(@PathVariable String place) {
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
	public ResponseEntity<String> getEmployeeSupervisee(@PathVariable Long employeeId) {
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
	public ResponseEntity<EmployeeAggregateResponse> getEmployeesSalaryByProperty(@RequestParam String property, @RequestParam String propertyValue) {
		
		if(!IApplicationConstant.searchParams.containsKey(property.toUpperCase()))
			throw new InvalidInputException("given field not supported !!");
		
		EmployeeAggregateResponse response = employeeService.salaryByProperty(IApplicationConstant.searchParams.get(property.toUpperCase()), propertyValue);
		return new ResponseEntity<EmployeeAggregateResponse>(response, HttpStatus.OK);
	}
	
	@GetMapping("/employee/title/{title}/salary-range")
	public ResponseEntity<EmployeeSalaryRange> getEmployeesSalaryRange(@PathVariable String title) {		
		EmployeeSalaryRange response = employeeService.salaryRangeByTitle(title);
		return new ResponseEntity<EmployeeSalaryRange>(response, HttpStatus.OK);
	}

}
