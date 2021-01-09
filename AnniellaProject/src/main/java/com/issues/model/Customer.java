package com.issues.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

@Entity
public class Customer extends Person {

	@Id
	private String id;
	
	@ManyToOne
	@JoinColumn(name="location")
	private Location location;
	
	@Transient
	private List<Request>request;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<Request> getRequest() {
		return request;
	}

	public void setRequest(List<Request> request) {
		this.request = request;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	@Override
	public String toString() {
		return "Customer [id=" + id + ", location=" + location + ", request=" + request + "]";
	}
	
	
	
}
