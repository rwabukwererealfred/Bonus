package com.issues.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.issues.genericdao.GenericDao;
import com.issues.genericdao.SessionManager;
import com.issues.model.Groups;

public class GroupDao extends GenericDao<Groups> {

	public Groups getOne(String id){
		Session session = SessionManager.getSession();
		session.beginTransaction();
		Groups l = (Groups)session.get(Groups.class, id);
		session.close();
		return l;
	}
	
	@SuppressWarnings("unchecked")
	public List<Groups>findByDepartment(String department){
		Session session = SessionManager.getSession();
		session.beginTransaction();
		Query q = session.createQuery("from Groups where department.id =:department");
		q.setString("department", department);
		List<Groups>list = q.list();
		session.close();
		return list;
	}
	
}
