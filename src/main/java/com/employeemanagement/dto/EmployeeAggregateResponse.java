package com.employeemanagement.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EmployeeAggregateResponse {
	
	public EmployeeAggregateResponse(Object key, Double value) {
		this.key = key;
		this.value = value;
	}
	
	@JsonProperty("propertyValue")
	private Object key;
	
	@JsonProperty("aggregatedResponse")
	private Double value;
}
