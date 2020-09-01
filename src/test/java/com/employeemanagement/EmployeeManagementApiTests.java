package com.employeemanagement;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import com.employeemanagement.services.IEmployeeService;

@RunWith(SpringRunner.class)
@WebMvcTest
@ActiveProfiles("test")
class EmployeeManagementApiTests {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private IEmployeeService employeeService;

	@Test
	public void totalSalaryByPropertyFor404() throws Exception {
		// finding total salary with given business unit
		// test for bu which not exist
		mvc.perform(MockMvcRequestBuilders.get("employee/salary?property=bu&propertyValue=RnD")
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
	}
	
	@Test
	public void totalSalaryByPropertyFor400() throws Exception {
		// test for salary which is not supported currently (only property: place, bu and supervisor is supported)
		mvc.perform(MockMvcRequestBuilders.get("/employee/salary?property=salary&propertyValue=500")
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
	}
	
	@Test
	public void totalSalaryByProperty() throws Exception {
		// finding total salary with given business unit
		// test for bu RND
		mvc.perform(MockMvcRequestBuilders.get("/employee/salary?property=place&propertyValue=Lko")
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
	}
	
	@Test
	public void salaryRangeByTitle() throws Exception {
		// checking for SDE2
		mvc.perform(MockMvcRequestBuilders.get("/employee/title/SDE2/salary-range")
				.accept(MediaType.APPLICATION_JSON))
			    .andExpect(status().isOk());
	}
}
