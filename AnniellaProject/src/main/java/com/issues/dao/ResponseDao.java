package com.issues.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.issues.genericdao.GenericDao;
import com.issues.genericdao.QueryParameters;
import com.issues.genericdao.SessionManager;
import com.issues.model.Employee;
import com.issues.model.Response;

public class ResponseDao extends GenericDao<Response> {

	public static class QUERY{
		public static final String findEmployeeResponse ="From Response where employeeRequest.id =:employeeRequest";
	
	}
	public static class QUERY_NAME {
		public static final String findEmployeeResponse = "Response.findEmployeeResponse";
	}
	
	public List<Response>employeeResponse(Integer employeeRequest){
		QueryParameters q = new QueryParameters().add("employeeRequest", employeeRequest);
		return this.executeNamedQueryMultiple(QUERY_NAME.findEmployeeResponse, q);
	}
	
	public Response findByEmployee(String employee, int employeeRequest) {
		Session s = SessionManager.getSession();
		Query q = s.createQuery("from Response where requestFrom.id =:employee  and employeeRequest.id =:employeeRequest");
		q.setString("employee", employee);
		q.setInteger("employeeRequest", employeeRequest);
		Response em = (Response)q.uniqueResult();
		s.close();
		return em;
	}
	
}
