package com.issues.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.issues.genericdao.GenericDao;
import com.issues.genericdao.SessionManager;
import com.issues.model.Request;

public class RequestDao extends GenericDao<Request> {
	
	public static  class QUERRY{
//		public static final String findClosedList="From Request where employee.id =:employee and status ='closed'";
		public static final String findRequest ="FROM Request";
	}
	public static class QUERY_NAME{
//		public static final String findClosedList = "Request.findClosedList";
		public static final String findRequest = "Request.findRequest";
	}

	@SuppressWarnings("unchecked")
	public List<Request>customerRequest(String customer){
		Session s = SessionManager.getSession();
		s.beginTransaction();
		Query q = s.createQuery("from Request where customer.id =:customer");
		q.setString("customer", customer);
		List<Request>list = q.list();
		s.close();
		return list;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<Request>employeeRequestSVD(){
		Session s = SessionManager.getSession();
		s.beginTransaction();
		Query q = s.createQuery("from Request where status='New'");
		List<Request>list = q.list();
		s.close();
		return list;
	}
	public Request getOne(String id){
		Session session = SessionManager.getSession();
		session.beginTransaction();
		Request l = (Request)session.get(Request.class, id);
		session.close();
		return l;
	}
	
//	public List<Request>findClosedList(String employee){
//		QueryParameters query = new QueryParameters().add("employee", employee);
//		return this.executeNamedQueryMultiple(QUERY_NAME.findClosedList,query);
//	}
	public List<Request>findRequest(){
		return this.executeNamedQueryMultiple(QUERY_NAME.findRequest);
	}
	@SuppressWarnings("unchecked")
	public List<Request>findRequestByDate(Date from , Date to){
		
		Session s = SessionManager.getSession();
		Query q = s.createQuery("FROM Request where requestDate between ? and ?");
		q.setDate(0, from);
		q.setDate(1, to);
		List<Request>list = q.list();
		s.close();
		return list;
	}
	
}
