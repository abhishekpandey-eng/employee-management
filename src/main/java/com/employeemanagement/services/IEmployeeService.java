package com.employeemanagement.services;

import java.io.IOException;
import java.util.List;
import com.employeemanagement.dto.EmployeeAggregateResponse;
import com.employeemanagement.dto.EmployeeSalaryRange;
import com.employeemanagement.models.Employee;

public interface IEmployeeService {

	void loadEmployeeList() throws IOException;

	List<Employee> saveEmployees(List<Employee> employees);

	List<Employee> incrementEmployeeSalary(String place, float percentage);
	
	List<Employee> getEmployees(String place);
	
	EmployeeAggregateResponse salaryByProperty(String property, String propertyValue);
	
	EmployeeSalaryRange salaryRangeByTitle(String title);
	
	String getEmployeeHierarcyDiagram(Long employeeId);
}
