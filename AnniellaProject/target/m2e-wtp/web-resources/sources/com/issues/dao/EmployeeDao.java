package com.issues.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.issues.genericdao.GenericDao;
import com.issues.genericdao.SessionManager;
import com.issues.model.Employee;

public class EmployeeDao extends GenericDao<Employee> {

	public Employee getOne(String id){
		Session session = SessionManager.getSession();
		session.beginTransaction();
		Employee l = (Employee)session.get(Employee.class, id);
		session.close();
		return l;
	}
	
	public Employee getPhoneNumber(String phoneNumber) {
		Session session = SessionManager.getSession();
		session.beginTransaction();
		Query q = session.createQuery("from Employee where phoneNumber =:phoneNumber");
		q.setString("phoneNumber", phoneNumber);
		Employee emp = (Employee)q.uniqueResult();
		session.close();
		return emp;
	}
	public Employee getUsername(String username) {
		Session session = SessionManager.getSession();
		session.beginTransaction();
		Query q = session.createQuery("from Employee where username =:username");
		q.setString("username",username);
		Employee emp = (Employee)q.uniqueResult();
		session.close();
		return emp;
	}
	@SuppressWarnings("unchecked")
	public List<Employee>findByGroups(String groups){
		Session session = SessionManager.getSession();
		session.beginTransaction();
		Query q = session.createQuery("from Employee where groups.id =:groups");
		q.setString("groups", groups);
		List<Employee>list = q.list();
		session.close();
		return list;
	}
}
