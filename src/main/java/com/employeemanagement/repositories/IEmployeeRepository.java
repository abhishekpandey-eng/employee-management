package com.employeemanagement.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.employeemanagement.models.Employee;

@Repository
public interface IEmployeeRepository extends JpaRepository<Employee, Long>{

	List<Employee> findByPlaceAndActive(String place, boolean active);
	
	@Query(value="WITH RECURSIVE subordinates AS ("  
			+"	SELECT"  
			+"		*" 
			+"	FROM"  
			+"		tbl_employee" 
			+"	WHERE"  
			+"		id = :employeeId"  
			+"	UNION" 
			+"		SELECT" 
			+"			e.*" 
			+"		FROM"  
			+"			tbl_employee e"  
			+"		INNER JOIN subordinates s ON s.id = e.supervisor_id"  
			+") SELECT * FROM subordinates",nativeQuery=true) 
	List<Employee> findSuperviseeList(@Param("employeeId") Long employeeId);
}
