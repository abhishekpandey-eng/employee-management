package com.employeemanagement.loaders;

import java.io.IOException;
import java.time.LocalDateTime;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import com.employeemanagement.services.IEmployeeService;

@Component
public class EmployeeLoader {

	@Autowired
	private IEmployeeService employeeService;
	
	private Logger logger = LogManager.getLogger(getClass());

	@EventListener(ApplicationReadyEvent.class)
	public void loadEmployeeList() {
		try {
			employeeService.loadEmployeeList();
		} catch (IOException e) {
			logger.error("Exception occured while loading CSV at: {}", LocalDateTime.now());
		}
	}
}
