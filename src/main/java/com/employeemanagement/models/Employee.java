package com.employeemanagement.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "tbl_employee")
public class Employee extends AbstractBaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "name", nullable = false)
	private String employeeName;

	@Column(name = "title")
	private String title;

	@Column(name = "business_unit")
	private String businessUnit;

	@Column(name = "place")
	private String place;

	@Column(name = "supervisor_id")
	private long supervisorId;

	@Column(name = "competencies")
	private String competencies;

	@Column(name = "salary", nullable = false)
	private float salary;
}
