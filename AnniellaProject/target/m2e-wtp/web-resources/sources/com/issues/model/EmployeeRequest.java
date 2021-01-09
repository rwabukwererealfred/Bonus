package com.issues.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Transient;

import com.issues.dao.EmployeeRequestDao;

@Entity
@NamedQueries({@NamedQuery(name=EmployeeRequestDao.QUERY_NAME.findEmployeeRequest, query = EmployeeRequestDao.QUERY.findEmployeeRequest),
	@NamedQuery(name=EmployeeRequestDao.QUERY_NAME.findByRequest, query= EmployeeRequestDao.QUERY.findByRequest)})
public class EmployeeRequest {

	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	private int id;
	@Enumerated(EnumType.STRING)
	private StatusType status ;
	private Date requestDate;
	
	@ManyToOne
	@JoinColumn(name="request")
	private Request request;
	
	@ManyToOne
	@JoinColumn(name="employee")
	private Employee employee;
	
	
	
	
	@Transient
	private List<Response>response;

	public int getId() {
		return id;
	}

	public StatusType getStatus() {
		return status;
	}

	public Date getRequestDate() {
		return requestDate;
	}

	public Request getRequest() {
		return request;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setStatus(StatusType status) {
		this.status = status;
	}

	public void setRequestDate(Date requestDate) {
		this.requestDate = requestDate;
	}

	public void setRequest(Request request) {
		this.request = request;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public List<Response> getResponse() {
		return response;
	}

	public void setResponse(List<Response> response) {
		this.response = response;
	}

	@Override
	public String toString() {
		return "EmployeeRequest [id=" + id + ", status=" + status + ", requestDate=" + requestDate + ", request="
				+ request + ", employee=" + employee + ", response=" + response + "]";
	}

	public EmployeeRequest(int id, StatusType status, Date requestDate, Request request, Employee employee,
			List<Response> response) {
		
		this.id = id;
		this.status = status;
		this.requestDate = requestDate;
		this.request = request;
		this.employee = employee;
		this.response = response;
	}

	public EmployeeRequest() {
		
	}

	

	
}
