package com.issues.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.issues.genericdao.GenericDao;
import com.issues.genericdao.SessionManager;
import com.issues.model.Location;

public class LocationDao extends GenericDao<Location>{

	public Location getOne(String id){
		Session session = SessionManager.getSession();
		session.beginTransaction();
		Location l = (Location)session.get(Location.class, id);
		session.close();
		return l;
	}
	@SuppressWarnings("unchecked")
	public List<Location>districtList(String location){
		Session s = SessionManager.getSession();
		s.beginTransaction();
		Query q = s.createQuery("from Location where location.id =:location");
		q.setString("location", location);
		List<Location>list = q.list();
		s.close();
		return list;
	}
	
}
