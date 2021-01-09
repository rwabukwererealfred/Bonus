package com.issues.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Transient;

import com.issues.dao.RequestDao;

@Entity
@NamedQueries({@NamedQuery(name=RequestDao.QUERY_NAME.findRequest,query = RequestDao.QUERRY.findRequest)})
public class Request {

	@Id
	private String id;
	private String subject;
	private String description;
	private Date requestDate;
	
	@Enumerated(EnumType.STRING)
	private StatusType status;
	
	@Transient
	private List<EmployeeRequest>employeeRequest;
	
	@ManyToOne
	@JoinColumn(name="customer")
	private Customer customer;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Date getRequestDate() {
		return requestDate;
	}
	public void setRequestDate(Date requestDate) {
		this.requestDate = requestDate;
	}
	
	public StatusType getStatus() {
		return status;
	}
	public void setStatus(StatusType status) {
		this.status = status;
	}
	
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
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
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Request [id=" + id + ", subject=" + subject + ", description=" + description + ", requestDate="
				+ requestDate + ", status=" + status + ", customer=" + customer + "]";
	}
	
	
}
