package com.employeemanagement;

import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import com.employeemanagement.models.Employee;
import com.employeemanagement.repositories.IEmployeeRepository;
import com.employeemanagement.services.IEmployeeService;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest
public class EmployeeSuperviseeTest {

	@Autowired
	private IEmployeeService employeeService;
	
	@MockBean
	private IEmployeeRepository employeeRepository;
	
	@Test
	public void employeeSuperviseeTest() {
		List<Employee> employees = getInputEmployeeList();
		Mockito.when(employeeRepository.findSuperviseeList(1L)).thenReturn(employees);
	
		Assert.assertEquals(employeeService.getEmployeeHierarcyDiagram(1L), "Vivek->Abhishek->Deepak->Pawan,Razi->");
	}
	
	@SuppressWarnings("serial")
	private List<Employee> getInputEmployeeList(){
		Employee vivek = new Employee() {{
			setId(1L);
			setEmployeeName("Vivek");
			setSupervisorId(0);
		}};
		
		Employee abhishek = new Employee() {{
			setId(2L);
			setEmployeeName("Abhishek");
			setSupervisorId(1);
		}};
		
		Employee deepak = new Employee() {{
			setId(3L);
			setEmployeeName("Deepak");
			setSupervisorId(2);
		}};
		
		Employee pawan = new Employee() {{
			setId(4L);
			setEmployeeName("Pawan");
			setSupervisorId(3);
		}};
		
		Employee razi = new Employee() {{
			setId(5L);
			setEmployeeName("Razi");
			setSupervisorId(3);
		}};
		
		return new ArrayList<Employee>() {{
			add(vivek);
			add(abhishek);
			add(deepak);
			add(pawan);
			add(razi);
		}};
	}
	
}
