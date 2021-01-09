package com.issues.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

@Entity
public class Department {

	@Id
	private String id;
	private String name;
	
	@Transient
	private List<Groups>group;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Groups> getGroup() {
		return group;
	}
	public void setGroup(List<Groups> group) {
		this.group = group;
	}
	@Override
	public String toString() {
		return "Department [id=" + id + ", name=" + name + ", group=" + group + "]";
	}
	
	
	
}
