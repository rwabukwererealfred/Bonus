package com.issues.dao;

import org.hibernate.Session;

import com.issues.genericdao.GenericDao;
import com.issues.genericdao.SessionManager;
import com.issues.model.Department;

public class DepartmentDao extends GenericDao<Department>{

	public Department getOne(String id){
		Session session = SessionManager.getSession();
		session.beginTransaction();
		Department l = (Department)session.get(Department.class, id);
		session.close();
		return l;
	}
}
