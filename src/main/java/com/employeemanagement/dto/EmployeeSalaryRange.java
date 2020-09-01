package com.employeemanagement.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EmployeeSalaryRange {

	public EmployeeSalaryRange(Float minSalary, Float maxSalary) {
		this.minSalary = minSalary;
		this.maxSalary = maxSalary;
	}

	private Float minSalary;

	private Float maxSalary;
}
