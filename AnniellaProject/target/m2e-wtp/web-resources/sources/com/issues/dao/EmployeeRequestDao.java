package com.issues.dao;

import java.util.List;


import com.issues.genericdao.GenericDao;
import com.issues.genericdao.QueryParameters;
import com.issues.model.EmployeeRequest;

public class EmployeeRequestDao extends GenericDao<EmployeeRequest> {

	public static class QUERY{
		public static final String findEmployeeRequest ="FROM EmployeeRequest where employee.id =:employee";
		public static final String findByRequest ="From EmployeeRequest where request.id =:request";
		
	}
	public static class QUERY_NAME{
		public static final String findEmployeeRequest ="EmployeeRequest.findEmployeeRequestt";
		public static final String findByRequest = "EmployeeRequest.findByRequest";
	}
	
	public List<EmployeeRequest>findEmployeeRequest(String employee){
		QueryParameters q = new QueryParameters().add("employee", employee);
		return this.executeNamedQueryMultiple(QUERY_NAME.findEmployeeRequest, q);
	}
	public List<EmployeeRequest>findByRequest(String request){
		QueryParameters q = new QueryParameters().add("request", request);
		return this.executeNamedQueryMultiple(QUERY_NAME.findByRequest, q);
	}
	
	
	
}
