package com.issues.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;

import com.issues.dao.ResponseDao;

@Entity
@NamedQueries({@NamedQuery(name = ResponseDao.QUERY_NAME.findEmployeeResponse, query = ResponseDao.QUERY.findEmployeeResponse) })
public class Response {

	@Id
	private String id;
	private String subject;
	private Date responseDate;
	
	private String description;
	
	@ManyToOne
	@JoinColumn(name="requestFrom")
	private Employee requestFrom;
	
	@OneToOne
	@JoinColumn(name="employeeRequest")
	private EmployeeRequest employeeRequest;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public Date getResponseDate() {
		return responseDate;
	}

	public void setResponseDate(Date responseDate) {
		this.responseDate = responseDate;
	}


	public EmployeeRequest getEmployeeRequest() {
		return employeeRequest;
	}

	public void setEmployeeRequest(EmployeeRequest employeeRequest) {
		this.employeeRequest = employeeRequest;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Employee getRequestFrom() {
		return requestFrom;
	}

	public void setRequestFrom(Employee requestFrom) {
		this.requestFrom = requestFrom;
	}
	
	
}
