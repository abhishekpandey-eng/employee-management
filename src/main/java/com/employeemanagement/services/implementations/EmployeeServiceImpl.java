package com.employeemanagement.services.implementations;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.csv.CSVRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import com.employeemanagement.dao.EmployeeRepoDao;
import com.employeemanagement.dto.EmployeeAggregateResponse;
import com.employeemanagement.dto.EmployeeSalaryRange;
import com.employeemanagement.exceptions.NotFoundException;
import com.employeemanagement.models.Employee;
import com.employeemanagement.models.FailureResponse;
import com.employeemanagement.repositories.IEmployeeRepository;
import com.employeemanagement.services.IEmployeeService;
import com.employeemanagement.utilities.CSVUtils;

@Service
public class EmployeeServiceImpl implements IEmployeeService {

	@Autowired
	private IEmployeeRepository employeeRepository;

	@Autowired
	private EmployeeRepoDao employeeDao;

	@Value("${spring.app.sourceFile}")
	private String employeeSourceFile;

	@Value("${spring.app.outputFailureResponse}")
	private String faluireResponseLocation;

	private Logger logger = LogManager.getLogger(getClass());

	@Override
	public void loadEmployeeList() throws IOException {
		Iterable<CSVRecord> stats = CSVUtils.readCSVFile(employeeSourceFile);

		List<Employee> successList = new LinkedList<>();
		List<FailureResponse> failureList = new LinkedList<>();

		mapRecordsToList(stats, successList, failureList);

		if (failureList.size() > 0) {
			CSVUtils.saveCSVFile(faluireResponseLocation, failureList, "Employee Id", "Employee Name",
					"Exception Message");
			logger.debug("saving failure list with count: {} in: {}", failureList.size(), faluireResponseLocation);
		}

		if (successList.size() > 0) {
			List<Employee> savedEmployees = saveEmployees(successList);
			logger.debug("saving employee list from CSV count: {} values: {}", savedEmployees.size(), savedEmployees);
		}
	}

	@Override
	public List<Employee> saveEmployees(List<Employee> employees) {
		return employeeRepository.saveAll(employees);
	}

	@Override
	@CachePut(cacheResolver = "employeeCacheResolver", key = "#place", unless = "#result==null or #result.size()==0")
	public List<Employee> incrementEmployeeSalary(String place, float percentage) {
		List<Employee> employees = getEmployees(place);
		if (CollectionUtils.isEmpty(employees)) {
			logger.debug("empty employee list for place: {}", place);
			return Collections.emptyList();
		}

		List<Employee> transformedEmployees = incrementSalary(employees, percentage/100);
		return saveEmployees(transformedEmployees);
	}

	@Override
	@Cacheable(cacheResolver = "employeeCacheResolver", key = "#place", unless = "#result==null or #result.size()==0")
	public List<Employee> getEmployees(String place) {
		return employeeRepository.findByPlaceAndActive(place, true);
	}

	@Override
	public EmployeeAggregateResponse salaryByProperty(String property, String propertyValue) {
		try {
			EmployeeAggregateResponse response = employeeDao.totalSalaryByProperty(property, propertyValue);
			return response;
		} catch (Exception ex) {
			logger.error("Exception when fetching result for property: {} and value: {} with exception: {}", property,
					propertyValue, ex.getMessage());
			throw new NotFoundException("no result found !!");
		}
	}

	@Override
	public EmployeeSalaryRange salaryRangeByTitle(String title) {
		try {
			EmployeeSalaryRange response = employeeDao.salaryRangeByTitle(title);
			return response;
		} catch (Exception ex) {
			logger.error("Exception when fetching salary range for title: {} with exception: {}", title,
					ex.getMessage());
			throw new NotFoundException("no result found !!");
		}
	}

	@Override
	public String getEmployeeHierarcyDiagram(Long employeeId) {
		List<Employee> employeeHierarchy = employeeRepository.findSuperviseeList(employeeId);
		
		if(CollectionUtils.isEmpty(employeeHierarchy))
			throw new NotFoundException("employee not found for given id !!"); 
		
		Map<Long, String> idNamemap = employeeHierarchy.stream()
				.collect(Collectors.toMap(Employee::getId, Employee::getEmployeeName));
		Map<String, List<String>> mangerEmployeeMap = new LinkedHashMap<>();
		
		for (int i = 0; i < employeeHierarchy.size(); i++) {
			if (mangerEmployeeMap.containsKey(idNamemap.get(employeeHierarchy.get(i).getSupervisorId()))) {
				mangerEmployeeMap.get(idNamemap.get(employeeHierarchy.get(i).getSupervisorId()))
						.add(employeeHierarchy.get(i).getEmployeeName());
				continue;
			}

			List<String> subordinateList = new ArrayList<>();
			subordinateList.add(employeeHierarchy.get(i).getEmployeeName());
			mangerEmployeeMap.put(idNamemap.get(employeeHierarchy.get(i).getSupervisorId()), subordinateList);
		}
		
		return drawHierarchy(mangerEmployeeMap.values());
	}

	/**
	 * private methods
	 */

	private void mapRecordsToList(Iterable<CSVRecord> stats, List<Employee> successList,
			List<FailureResponse> failureList) {
		stats.forEach(csvRecord -> {
			Employee employee = new Employee();
			try {
				employee.setId(Long.valueOf(csvRecord.get("EmployeeID")));
				employee.setEmployeeName(csvRecord.get("EmployeeName"));
				employee.setTitle(csvRecord.get("Title"));
				employee.setPlace(csvRecord.get("Place"));
				employee.setBusinessUnit(csvRecord.get("BusinessUnit"));
				employee.setSupervisorId(Long.valueOf(csvRecord.get("SupervisorID")));
				employee.setCompetencies(csvRecord.get("Competencies"));
				employee.setSalary(Float.valueOf(csvRecord.get("Salary")));
				successList.add(employee);
			} catch (Exception ex) {
				FailureResponse response = new FailureResponse();
				response.setEmployeeId(csvRecord.get("EmployeeID"));
				response.setEmployeeName(csvRecord.get("EmployeeName"));
				response.setExceptionMessage(ex.getMessage());
				failureList.add(response);
			}
		});
	}

	private List<Employee> incrementSalary(List<Employee> employees, float salary) {
		return employees.stream()
				.peek(employee -> employee.setSalary(employee.getSalary() + (employee.getSalary() * salary)))
				.collect(Collectors.toList());
	}
	
	private String drawHierarchy(Collection<List<String>> employees) {
		StringBuilder hierarchy = new StringBuilder();
		for (List<String> employee : employees) {
			String subordinateCommaSeparated = employee.stream().collect(Collectors.joining(","));
			hierarchy.append(subordinateCommaSeparated);
			hierarchy.append("->");
		}
		return hierarchy.toString();
	}
}
