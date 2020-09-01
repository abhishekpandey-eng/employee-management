package com.employeemanagement.models;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FailureResponse {

	private String employeeId;
	
	private String employeeName;
	
	private String exceptionMessage;
}
