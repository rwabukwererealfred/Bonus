package com.issues.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

@Entity
public class Employee extends Person {

	@Id
	private String id;
	
	
	@ManyToOne
	@JoinColumn(name="groups")
	private Groups groups;
	
	@Transient
	private List<EmployeeRequest>employeeRequest;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	

	public Groups getGroups() {
		return groups;
	}

	public void setGroups(Groups groups) {
		this.groups = groups;
	}

	/**
	 * @return the employeeRequest
	 */
	public List<EmployeeRequest> getEmployeeRequest() {
		return employeeRequest;
	}

	/**
	 * @param employeeRequest the employeeRequest to set
	 */
	public void setEmployeeRequest(List<EmployeeRequest> employeeRequest) {
		this.employeeRequest = employeeRequest;
	}

	@Override
	public String toString() {
		return "Employee [id=" + id + ", groups=" + groups + ", employeeRequest=" + employeeRequest + "]";
	}

	
	
	
}
