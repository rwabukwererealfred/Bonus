package com.issues.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;

import com.issues.dao.CustomerDao;
import com.issues.dao.EmployeeDao;
import com.issues.dao.EmployeeRequestDao;
import com.issues.dao.RequestDao;
import com.issues.dao.ResponseDao;
import com.issues.model.Customer;
import com.issues.model.Department;
import com.issues.model.Employee;
import com.issues.model.EmployeeRequest;
import com.issues.model.Groups;
import com.issues.model.Request;
import com.issues.model.Response;
import com.issues.model.StatusType;

@ManagedBean(eager = true)
@SessionScoped
public class LoginController extends Message1 implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7906017338837529527L;

	private Customer customer;
	private String username;
	private String password;
	private Request request;
	private List<Request> requestList;
	private Employee employee;
	private Response response;
	private String selectedEmployee, others;
	private List<EmployeeRequest> selectedRequestList;
	private String status;
	private List<Request> selectedRequestList1;
	private List<EmployeeRequest> employeePendingRequest, employeeClosedRequest;
	private List<Response> responseList;
	private boolean customerLogin, admimLogin;

	public LoginController() {
		this.others = "";
		this.customerLogin = false;
		this.admimLogin = false;
		this.selectedRequestList1 = new ArrayList<Request>();
		this.responseList = new ArrayList<Response>();
		this.employeePendingRequest = new ArrayList<EmployeeRequest>();
		this.employeeClosedRequest = new ArrayList<EmployeeRequest>();
		this.status = "";
		this.response = new Response();
		this.selectedEmployee = "";
		this.selectedRequestList = new ArrayList<EmployeeRequest>();
		this.requestList = new ArrayList<Request>();
		this.customer = new Customer();
		this.request = new Request();
		this.username = "";
		this.password = "";
		this.employee = new Employee();
	}

	public void userLogin() {
		try {
			if (this.username.matches("admin") && this.password.matches("123")) {
				this.admimLogin = true;
				FacesContext.getCurrentInstance().getExternalContext().redirect("companyRegistration.xhtml");

			} else {
				Customer c = new CustomerDao().getUsername(this.username);
				if (c != null) {
					if (c.getPassword().matches(this.password)) {
						this.customer = c;
						this.customerLogin = true;
						this.requestList = new RequestDao().customerRequest(this.customer.getId());
						FacesContext.getCurrentInstance().getExternalContext().redirect("SendRequest.xhtml");
					} else {
						errorMessage("error", "password does not matches with username!!");
					}
				} else {
					Employee e = new EmployeeDao().getUsername(this.username);
					if (e != null) {
						if (e.getPassword().matches(this.password)) {
							this.employee = e;
							this.employeeClosedRequest = new EmployeeRequestDao()
									.findEmployeeRequest(this.employee.getId()).stream()
									.filter(i -> i.getStatus().equals(StatusType.closed))
									.collect(Collectors.toList());
							if (e.getGroups().isStatus()) {
								this.requestList = new RequestDao().employeeRequestSVD();
								FacesContext.getCurrentInstance().getExternalContext()
										.redirect("SVDRecieveRequest.xhtml");
							} else {
								this.employeePendingRequest = new EmployeeRequestDao()
										.findEmployeeRequest(this.employee.getId()).stream()
										.filter(i -> i.getStatus().equals(StatusType.pending))
										.collect(Collectors.toList());
								
								FacesContext.getCurrentInstance().getExternalContext()
										.redirect("EmployeeRequest.xhtml");
							}
						} else {
							errorMessage("error", "password does not matches with username!!");
						}
					} else {
						errorMessage("error", "username and password does not exist!!");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void logout() throws IOException {
		this.requestList = new ArrayList<Request>();
		this.customer = new Customer();
		this.employee = new Employee();
		this.admimLogin = false;
		this.customerLogin = false;
		FacesContext.getCurrentInstance().getExternalContext().redirect("login.xhtml");
	}

	public void sendRequest() {
		try {
			Customer c = new CustomerDao().getOne(this.customer.getId());
			this.request.setId("RQS"+UUID.randomUUID().toString().substring(0, 2)+c.getId().substring(0,2));
			this.request.setCustomer(c);
			this.request.setRequestDate(new Date());
			this.request.setStatus(StatusType.New);
			if (this.request.getSubject().equalsIgnoreCase("Other")) {
				this.request.setSubject(this.others);
			}
			new RequestDao().record(this.request);
			successMessage("success", "your request is well successfull sent!!");
			this.request = new Request();
			this.others = "";
			this.requestList = new RequestDao().customerRequest(this.customer.getId());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void employeeSendRequest() {
		try {
			Employee em = new EmployeeDao().getOne(this.selectedEmployee);
			if (!this.selectedRequestList.isEmpty()) {
				this.selectedRequestList.stream().forEach(r -> {
					if (status.matches("Closed")) {
						Request req = new RequestDao().getOne(r.getRequest().getId());
						req.setStatus(StatusType.closed);
						new RequestDao().update(req);
						r.setStatus(StatusType.closed);
						new EmployeeRequestDao().update(r);
						this.response.setId(UUID.randomUUID().toString());
						this.response.setEmployeeRequest(r);
						this.response.setResponseDate(new Date());
						this.response.setRequestFrom(this.employee);
						new ResponseDao().record(this.response);
						this.sendMessage(r.getRequest().getCustomer(), this.response.getDescription(),
								this.response.getSubject());
						System.out.print("weewewe third closed");
					} else if (this.status.matches("Pending")) {
						r.setStatus(StatusType.approved);
						new EmployeeRequestDao().update(r);
						EmployeeRequest empRequest = new EmployeeRequest();
						empRequest.setEmployee(em);
						empRequest.setRequest(r.getRequest());
						empRequest.setRequestDate(new Date());
						empRequest.setStatus(StatusType.pending);
						new EmployeeRequestDao().record(empRequest);
						this.response.setId(UUID.randomUUID().toString());
						this.response.setEmployeeRequest(r);
						this.response.setResponseDate(new Date());
						this.response.setRequestFrom(this.employee);
						new ResponseDao().record(this.response);
						System.out.print("weewewe third closed");
					}
				});
				this.employeePendingRequest = new EmployeeRequestDao().findEmployeeRequest(this.employee.getId())
						.stream().filter(i -> i.getStatus().equals(StatusType.pending)).collect(Collectors.toList());
				this.employeeClosedRequest = new EmployeeRequestDao().findEmployeeRequest(this.employee.getId())
						.stream().filter(i -> i.getStatus().equals(StatusType.closed)).collect(Collectors.toList());
				this.selectedRequestList = new ArrayList<EmployeeRequest>();
				this.response = new Response();
				this.selectedEmployee = "";
				Registration registration = new Registration();
				registration.setDepartment(new Department());
				registration.setGroup(new Groups());
				if (status.matches("Closed")) {
					successMessage("success", "successfull closed!!");
				} else {
					successMessage("success", "request is well assigned to employee!!");
				}

			} else {
				errorMessage("error", "please select some request!!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//	public void returnReponse(EmployeeRequest request) {
//		try {
//			this.responseList = new ResponseDao().employeeResponse(request.getId());
//			RequestContext.getCurrentInstance().execute("PF('response').show()");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

	public void assignRequestToEmployee() {
		try {
			System.out.println("username: " + this.username);

			Employee em = new EmployeeDao().getOne(this.selectedEmployee);
			if (this.selectedRequestList1.isEmpty() == false) {
				this.selectedRequestList1.stream().forEach(re -> {
					EmployeeRequest empRequest = new EmployeeRequest();
					if (this.status.matches("Pending")) {
						EmployeeRequest empRe = new EmployeeRequest();
						empRe.setEmployee(this.employee);
						empRequest.setStatus(StatusType.pending);
						empRe.setStatus(StatusType.approved);
						empRequest.setEmployee(em);
						empRe.setRequest(re);
						empRe.setRequestDate(new Date());
						new EmployeeRequestDao().record(empRe);
						new RequestDao().update(re);
						empRequest.setRequest(re);
						empRequest.setRequestDate(new Date());
						new EmployeeRequestDao().record(empRequest);
						this.response.setId(UUID.randomUUID().toString());
						this.response.setEmployeeRequest(empRe);
						this.response.setResponseDate(new Date());
						this.response.setRequestFrom(this.employee);
						new ResponseDao().record(response);
						
					} else {
						re.setStatus(StatusType.closed);
						empRequest.setEmployee(this.employee);
						empRequest.setStatus(StatusType.approved);
						new RequestDao().update(re);
						empRequest.setRequest(re);
						empRequest.setRequestDate(new Date());
						new EmployeeRequestDao().record(empRequest);
						this.response.setId(UUID.randomUUID().toString());
						this.response.setEmployeeRequest(empRequest);
						this.response.setResponseDate(new Date());
						this.response.setRequestFrom(this.employee);
						new ResponseDao().record(response);
						this.sendMessage(re.getCustomer(), this.response.getDescription(),
								this.response.getSubject());
					}
					
					

				});
				
				if (this.status.matches("Pending")) {
					successMessage("success", "request is well assigned to employee!!");
				} else {
					successMessage("success", "request is well successfull Closed!!");
				}

				this.response = new Response();
				this.selectedEmployee = "";
				this.requestList = new RequestDao().employeeRequestSVD();
				this.selectedRequestList = new ArrayList<>();
			} else {
				errorMessage("error", "please select some request!!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Customer getCustomer() {
		return customer;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Request getRequest() {
		return request;
	}

	public void setRequest(Request request) {
		this.request = request;
	}

	public List<Request> getRequestList() {
		return requestList;
	}

	public void setRequestList(List<Request> requestList) {
		this.requestList = requestList;
	}

	public Employee getEmployee() {
		return employee;
	}

	public Response getResponse() {
		return response;
	}

	public String getSelectedEmployee() {
		return selectedEmployee;
	}

	public List<EmployeeRequest> getSelectedRequestList() {
		return selectedRequestList;
	}

	public void setSelectedRequestList(List<EmployeeRequest> selectedRequestList) {
		this.selectedRequestList = selectedRequestList;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public void setResponse(Response response) {
		this.response = response;
	}

	public void setSelectedEmployee(String selectedEmployee) {
		this.selectedEmployee = selectedEmployee;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<EmployeeRequest> getEmployeePendingRequest() {
		return employeePendingRequest;
	}

	public List<EmployeeRequest> getEmployeeClosedRequest() {
		return employeeClosedRequest;
	}

	public void setEmployeePendingRequest(List<EmployeeRequest> employeePendingRequest) {
		this.employeePendingRequest = employeePendingRequest;
	}

	public void setEmployeeClosedRequest(List<EmployeeRequest> employeeClosedRequest) {
		this.employeeClosedRequest = employeeClosedRequest;
	}

	public List<Response> getResponseList() {
		return responseList;
	}

	public void setResponseList(List<Response> responseList) {
		this.responseList = responseList;
	}

	public List<Request> getSelectedRequestList1() {
		return selectedRequestList1;
	}

	public void setSelectedRequestList1(List<Request> selectedRequestList1) {
		this.selectedRequestList1 = selectedRequestList1;
	}

	public boolean isCustomerLogin() {
		return customerLogin;
	}

	public boolean isAdmimLogin() {
		return admimLogin;
	}

	public void setCustomerLogin(boolean customerLogin) {
		this.customerLogin = customerLogin;
	}

	public void setAdmimLogin(boolean admimLogin) {
		this.admimLogin = admimLogin;
	}

	public String getOthers() {
		return others;
	}

	public void setOthers(String others) {
		this.others = others;
	}

}
