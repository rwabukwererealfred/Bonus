package com.issues.dao;

import org.hibernate.Query;
import org.hibernate.Session;

import com.issues.genericdao.GenericDao;
import com.issues.genericdao.SessionManager;
import com.issues.model.Customer;

public class CustomerDao extends GenericDao<Customer> {

	public Customer getOne(String id){
		Session session = SessionManager.getSession();
		session.beginTransaction();
		Customer l = (Customer)session.get(Customer.class, id);
		session.close();
		return l;
	}
	
	public Customer getUsername(String username) {
		Session session = SessionManager.getSession();
		session.beginTransaction();
		Query q = session.createQuery("from Customer where username =:username");
		q.setString("username",username);
		Customer emp = (Customer)q.uniqueResult();
		session.close();
		return emp;
	}
}
