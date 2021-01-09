package com.issues.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

@Entity
public class Location {

	@Id
	private String id;
	private String locationName;
	
	@Transient
	private List<Customer>customer;
	
	@ManyToOne
	@JoinColumn(name="location")
	private Location location;

	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public List<Customer> getCustomer() {
		return customer;
	}

	public void setCustomer(List<Customer> customer) {
		this.customer = customer;
	}

	@Override
	public String toString() {
		return "Location [id=" + id + ", locationName=" + locationName + ", customer=" + customer + ", location="
				+ location + "]";
	}
	
	
}
