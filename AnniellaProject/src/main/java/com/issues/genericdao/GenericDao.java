package com.issues.genericdao;



import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;

public class GenericDao<T> {

	public void record(T t)
	{
		Session s  = SessionManager.getSession();
		s.beginTransaction();
		s.save(t);
		s.getTransaction().commit();
        s.close();
	}
	
	public void update(T t)
	{
		Session s = SessionManager.getSession();
		s.beginTransaction();
		s.update(t);
		s.getTransaction().commit();
		s.close();
	}
	
	public void delete(T t)
	{
		Session s = SessionManager.getSession();
		s.beginTransaction();
		s.delete(t);
		s.getTransaction().commit();
		s.close();
	}
	
	@SuppressWarnings("unchecked")
	public List<T> getAll(String query)
	{
		Session s =SessionManager.getSession();
		List<T> list = new ArrayList<T>();
		list = s.createQuery(query).list();
		s.close();
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public List<T> executeNamedQueryMultiple(final String name, final QueryParameters props) {
		return getMappedQueryWithArgs(name, props).list();
	}
	@SuppressWarnings("unchecked")
	public List<T> executeNamedQueryMultiple(final String name) {
		return SessionManager.getSession().getNamedQuery(name).list();
	}
	
	private Query getMappedQueryWithArgs(final String name, final QueryParameters props) {
		
		Query query = SessionManager.getSession().getNamedQuery(name);
		Map<String, Object> args = props.getArgs();
		for (String key : args.keySet()) {
			query.setParameter(key, args.get(key));
		}
		// Now assign the lists if there are any
		Map<String, Collection<?>> lists = props.getLists();
		for (String key : lists.keySet()) {
			query.setParameterList(key, (Collection<?>) lists.get(key));
		}
		return query;
	}
	
	
}
