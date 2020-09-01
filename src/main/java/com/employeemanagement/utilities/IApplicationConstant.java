package com.employeemanagement.utilities;

import java.util.Map;
import com.google.common.collect.ImmutableMap;

public interface IApplicationConstant {

	public static Map<String, String> searchParams = ImmutableMap.of(
		    "BU", "businessUnit",
		    "SUPERVISOR", "supervisorId",
		    "PLACE", "place"
		);
}
