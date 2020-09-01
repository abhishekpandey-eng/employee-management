package com.employeemanagement.dao;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.employeemanagement.dto.EmployeeAggregateResponse;
import com.employeemanagement.dto.EmployeeSalaryRange;
import com.employeemanagement.models.Employee;

@Repository
public class EmployeeRepoDao {

	@Autowired
	EntityManager entityManager;

	public EmployeeAggregateResponse totalSalaryByProperty(String property, String propertyValue) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<EmployeeAggregateResponse> cq = cb.createQuery(EmployeeAggregateResponse.class);

		Root<Employee> employee = cq.from(Employee.class);
		
		Predicate propertyNamePredicate = cb.equal(employee.get(property), propertyValue);
		Predicate activePredicate = cb.equal(employee.get("active"), true);
		
		cq.where(propertyNamePredicate, activePredicate);
		cq.multiselect(employee.get(property), cb.sum(employee.get("salary"))).groupBy(employee.get(property));
		
		TypedQuery<EmployeeAggregateResponse> query = entityManager.createQuery(cq);
		
		return query.getSingleResult();	
	}
	
	public EmployeeSalaryRange salaryRangeByTitle(String title) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<EmployeeSalaryRange> cq = cb.createQuery(EmployeeSalaryRange.class);

		Root<Employee> employee = cq.from(Employee.class);
		
		Predicate propertyNamePredicate = cb.equal(employee.get("title"), title);
		Predicate activePredicate = cb.equal(employee.get("active"), true);
		
		cq.where(propertyNamePredicate, activePredicate);
		cq.multiselect(cb.min(employee.get("salary")), cb.max(employee.get("salary"))).groupBy(employee.get("title"));
		
		TypedQuery<EmployeeSalaryRange> query = entityManager.createQuery(cq);
		
		return query.getSingleResult();	
	}
}
