package com.issues.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.context.RequestContext;

import com.issues.dao.CustomerDao;
import com.issues.dao.DepartmentDao;
import com.issues.dao.EmployeeDao;
import com.issues.dao.EmployeeRequestDao;
import com.issues.dao.GroupDao;
import com.issues.dao.LocationDao;
import com.issues.dao.RequestDao;
import com.issues.dao.ResponseDao;
import com.issues.model.Customer;
import com.issues.model.Department;
import com.issues.model.Employee;
import com.issues.model.EmployeeRequest;
import com.issues.model.Groups;
import com.issues.model.Location;
import com.issues.model.Request;
import com.issues.model.Response;
import com.issues.model.StatusType;

/**
 * @author Hp
 *
 */
@ManagedBean(eager = true)
@ViewScoped
public class Registration extends Message1 implements Serializable {

	private static final long serialVersionUID = -8279962048979300450L;

	private Employee employee;
	private Customer customer;
	private Department department;
	private Groups group;
	private String locationId;
	private List<Department> departmentList;
	private List<Groups> groupList;
	private List<Location> locationList, districtList;
	private List<Employee> employeeList;
	private String confirm = "";
	private String district1;
	private List<Request> requestList;
	private List<Request> selectedRequestList;
	private String selectedEmployee;
	private Response response;
	private List<Groups> groupListByDepartment;
	private List<Employee> employeesByGroups;
	private String description;
	private String subject;
	private List<Request> allRequest;
	private List<EmployeeRequest> findByEmployeeRequest;
	private String key;

	public Registration() {
		this.key = "";
		this.findByEmployeeRequest = new ArrayList<EmployeeRequest>();
		this.allRequest = new RequestDao().findRequest();
		this.response = new Response();
		this.employeesByGroups = new ArrayList<Employee>();
		this.selectedRequestList = new ArrayList<Request>();
		this.requestList = new RequestDao().employeeRequestSVD();
		this.districtList = new ArrayList<Location>();
		this.employeeList = new EmployeeDao().getAll("from Employee");
		this.employee = new Employee();
		this.customer = new Customer();
		this.group = new Groups();
		this.groupListByDepartment = new ArrayList<Groups>();
		this.department = new Department();
		this.groupList = new GroupDao().getAll("from Groups");
		this.departmentList = new DepartmentDao().getAll("from Department");
		this.locationList = new LocationDao().getAll("from Location where location is null");
	}

	public void districtListForm() {

		districtList = new LocationDao().districtList(locationId);

	}

	public void registerCustomer() {
		try {
			if (validatePhone(this.customer.getPhoneNumber())) {
				if (customer.getPassword().matches(confirm)) {
					Location loc = new LocationDao().getOne(this.district1);
					this.customer.setLocation(loc);
					this.customer.setId(UUID.randomUUID().toString());
					new CustomerDao().record(this.customer);
					this.customer = new Customer();
					this.confirm = "";
					successMessage("success", "account is well successfull saved!!");
					this.district1 = "";
					RequestContext.getCurrentInstance().execute("PF('created').show(), PF('new').hide()");
				} else {
					errorMessage("error", "wrong confirmation password!!");
					RequestContext.getCurrentInstance().execute("PF('new').show()");
				}
			} else {
				errorMessage("error", "please provide right number!!");

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void registerGroup() {
		try {
			this.group.setId(UUID.randomUUID().toString());
			Department d = new DepartmentDao().getOne(this.department.getId());
			this.group.setDepartment(d);
			this.group.setStatus(false);
			new GroupDao().record(this.group);
			this.group = new Groups();
			this.groupList = new GroupDao().getAll("from Groups");
			this.department = new Department();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void changeStatus(Groups groups) {

		try {
			if (groups.isStatus() == false) {
				this.groupList.stream().forEach(r -> {
					r.setStatus(false);
					new GroupDao().update(r);
				});
				groups.setStatus(true);
				new GroupDao().update(groups);
				successMessage("success", "group status is well updated");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void registerDepartment() {
		try {
			this.department.setId(UUID.randomUUID().toString());
			new DepartmentDao().record(this.department);
			this.department = new Department();
			this.departmentList = new DepartmentDao().getAll("from Department");
			successMessage("success", "new department is well registered");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void registerEmployee() {
		try {
			Employee em = new EmployeeDao().getPhoneNumber(this.employee.getPhoneNumber());
			if (validatePhone(this.employee.getPhoneNumber())) {
				if (em != null) {
					errorMessage("error", "phone number already exist!!");
				} else {
					this.employee.setId(UUID.randomUUID().toString());
					Groups g = new GroupDao().getOne(this.group.getId());

					this.employee.setGroups(g);
					new EmployeeDao().record(this.employee);
					this.employeeList = new EmployeeDao().getAll("from Employee");
					this.employee = new Employee();
					this.group = new Groups();
					successMessage("success", "new employee is well successfull registered");
				}
			} else {
				errorMessage("error", "please provide right number!!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void createAccountByEmployee() {
		try {
			Employee em = new EmployeeDao().getPhoneNumber(this.employee.getPhoneNumber());
			if (em != null) {
				if (this.employee.getPassword().matches(confirm)) {
					em.setUsername(this.employee.getUsername());
					em.setPassword(this.employee.getPassword());
					new EmployeeDao().update(em);
					successMessage("success", "new account is well successfull created!!");
					RequestContext.getCurrentInstance().execute("PF('created').show(), PF('employee').hide()");
					confirm = "";
					this.employee = new Employee();
				} else {
					errorMessage("error", "password does not matches!!");
				}
			} else {
				errorMessage("error", "Phone number does not exist!!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void showRequest(String request) {
		this.findByEmployeeRequest = new EmployeeRequestDao().findByRequest(request).stream()
				.filter(i -> i.getStatus() == StatusType.approved || i.getStatus()== StatusType.closed).collect(Collectors.toList());
	}

	public List<String> employeeResponse(EmployeeRequest re) {
		return new ResponseDao().employeeResponse(re.getId()).stream().map(i -> i.getDescription())
				.collect(Collectors.toList());
	}

	// @PostConstruct
	// public void display() {
	// String result= new
	// ResponseDao().findByEmployee("ad642bbf-d764-49e4-b776-5e67075afb00",5).getDescription();
	// System.out.println("answer: "+result);
	// }
	public String findEmployeeResponse(Employee emp, EmployeeRequest re) {
		System.out.println("employee: " + emp.getId() + " request: " + re.getId());

		String list = new ResponseDao().findByEmployee(emp.getId(), re.getId()).getDescription();

		return list;

	}

	// public void assignRequestToEmployee() {
	// try {
	// System.out.println("username: "+this.username);
	// Employee e = new EmployeeDao().getUsername(this.username);
	// Employee em = new EmployeeDao().getOne(this.selectedEmployee);
	// if (this.selectedRequestList.isEmpty() == false) {
	// this.selectedRequestList.stream().forEach(re -> {
	// re.setStatus(StatusType.pending);
	// new RequestDao().update(re);
	// EmployeeRequest empRequest = new EmployeeRequest();
	// empRequest.setEmployee(em);
	// empRequest.setRequest(re);
	// empRequest.setRequestDate(new Date());
	// empRequest.setStatus(StatusType.pending);
	// new EmployeeRequestDao().record(empRequest);
	// this.response.setId(UUID.randomUUID().toString());
	// this.response.setEmployeeRequest(empRequest);
	// this.response.setResponseDate(new Date());
	// this.response.setRequestFrom(e);
	// new ResponseDao().record(response);
	// });
	// this.requestList = new RequestDao().employeeRequestSVD();
	// this.selectedRequestList = new ArrayList<>();
	// successMessage("success", "request is well assigned to employee!!");
	// this.response = new Response();
	// this.selectedEmployee = "";
	// this.department = new Department();
	// this.group = new Groups();
	// } else {
	// errorMessage("error", "please select some request!!");
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	public void showDescription(Request request) {
		this.description = request.getDescription();
		this.subject = request.getSubject();
		RequestContext.getCurrentInstance().execute("PF('description').show()");
	}

	public void findGroups() {
		this.groupListByDepartment = new GroupDao().findByDepartment(this.department.getId()).stream()
				.filter(i -> i.isStatus() == false).collect(Collectors.toList());
	}

	public void findEmployee() {
		this.employeesByGroups = new EmployeeDao().findByGroups(this.group.getId());
	}

	public void searchId() {
		List<Request> list = new ArrayList<>();
		try {
			if (this.key == "" || this.key.matches("") || this.key.isEmpty()) {
				list = new RequestDao().findRequest();
			} else {
				list = this.allRequest.stream().filter(i -> i.getId().equalsIgnoreCase(this.key))
						.collect(Collectors.toList());
			}
			this.allRequest = list;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public Groups getGroup() {
		return group;
	}

	public void setGroup(Groups group) {
		this.group = group;
	}

	public List<Department> getDepartmentList() {
		return departmentList;
	}

	public void setDepartmentList(List<Department> departmentList) {
		this.departmentList = departmentList;
	}

	public List<Groups> getGroupList() {
		return groupList;
	}

	public void setGroupList(List<Groups> groupList) {
		this.groupList = groupList;
	}

	public List<Location> getLocationList() {
		return locationList;
	}

	public void setLocationList(List<Location> locationList) {
		this.locationList = locationList;
	}

	public List<Location> getDistrictList() {
		return districtList;
	}

	public void setDistrictList(List<Location> districtList) {
		this.districtList = districtList;
	}

	public String getConfirm() {
		return confirm;
	}

	public void setConfirm(String confirm) {
		this.confirm = confirm;
	}

	public String getLocationId() {
		return locationId;
	}

	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}

	public String getDistrict1() {
		return district1;
	}

	public void setDistrict1(String district1) {
		this.district1 = district1;
	}

	public List<Employee> getEmployeeList() {
		return employeeList;
	}

	public void setEmployeeList(List<Employee> employeeList) {
		this.employeeList = employeeList;
	}

	public List<Request> getRequestList() {
		return requestList;
	}

	public List<Request> getSelectedRequestList() {
		return selectedRequestList;
	}

	public String getSelectedEmployee() {
		return selectedEmployee;
	}

	public void setRequestList(List<Request> requestList) {
		this.requestList = requestList;
	}

	public void setSelectedRequestList(List<Request> selectedRequestList) {
		this.selectedRequestList = selectedRequestList;
	}

	public void setSelectedEmployee(String selectedEmployee) {
		this.selectedEmployee = selectedEmployee;
	}

	public Response getResponse() {
		return response;
	}

	public List<Groups> getGroupListByDepartment() {
		return groupListByDepartment;
	}

	public List<Employee> getEmployeesByGroups() {
		return employeesByGroups;
	}

	public void setResponse(Response response) {
		this.response = response;
	}

	public void setGroupListByDepartment(List<Groups> groupListByDepartment) {
		this.groupListByDepartment = groupListByDepartment;
	}

	public void setEmployeesByGroups(List<Employee> employeesByGroups) {
		this.employeesByGroups = employeesByGroups;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public List<Request> getAllRequest() {
		return allRequest;
	}

	public void setAllRequest(List<Request> allRequest) {
		this.allRequest = allRequest;
	}

	public List<EmployeeRequest> getFindByEmployeeRequest() {
		return findByEmployeeRequest;
	}

	public void setFindByEmployeeRequest(List<EmployeeRequest> findByEmployeeRequest) {
		this.findByEmployeeRequest = findByEmployeeRequest;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

}
