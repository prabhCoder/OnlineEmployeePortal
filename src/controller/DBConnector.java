package controller;

import java.awt.List;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.annotation.ManagedBean;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import model.EmployeeProfile;
import model.Leaves;
import model.Salary;
import model.Timesheet;

import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.RowEditEvent;
import org.primefaces.event.SelectEvent;

import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.ResultSetMetaData;

@ManagedBean(value="dBConnector")
public class DBConnector {
	private String username;
	private String empId;
	private String password;
	private String empName;
	private Date fromDate;
	private Date toDate;
	private String projectId;
	private String workingHrs;
	private Date date;
	private Map<Integer,String> projects;
	private ArrayList<EmployeeProfile> employeeDataList;
	private ArrayList<Leaves> leaves;
	private Leaves leave;
	private ArrayList<Timesheet> timesheet;
	private ArrayList<Salary> salary;
	private Map<String,Integer> months;
	private String adminId;
	private String adminName;
	private String adminUsername;
	private String adminPassword;
	private String salaryMonth;
	private String salaryYear;
	private String salaryAmount;
	private String leaveStatus;
	private int selectedRowIndex=-1;
	private int leaveEmpId;
	private String leaveFromDate;
	private String leaveToDate;
	
	public static void main(String[] args){
		DBConnector obj=new DBConnector();
	obj.getAllMonths();
	}
	
	public String getAllMonths(){
		Calendar cal = Calendar.getInstance();
		 months = cal.getDisplayNames(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH);
		System.out.println(months);
	String path=	navigateToAddSalary();
	return path;
		
	}
public String viewLeaves(){
	try{
	Class.forName("com.mysql.jdbc.Driver");
	Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/employee_portal","root", "12345");
	//Statement stat=con.createStatement();
	String query = "SELECT * FROM leaves WHERE empId=?";
	PreparedStatement ps = (PreparedStatement) con.prepareStatement(query);

	ps.setString(1, empId);

	ResultSet set = ps.executeQuery();
	
	leaves=new ArrayList<Leaves>();
	Format formatter = new SimpleDateFormat("dd/MM/yyyy");
	
	while(set.next()){
		Leaves obj=new Leaves();
		
				String fromDt = formatter.format(set.getDate(2));
				String toDt = formatter.format(set.getDate(3));
				obj.setEmpId(set.getInt(1));
	        	obj.setFromDate(fromDt);
	        	obj.setToDate(toDt);
	        	obj.setStatus(set.getString(4));
	        	leaves.add(obj);  	
	        
	      
	}

	
		System.out.println("leaves list:"+leaves.size());
		
	return "/employee/viewLeaves.xhtml?faces-redirect=true";						
}catch(Exception e){
	e.printStackTrace();
}
return null;
}

public String viewLeavesOfEmployees(){
	try{
	Class.forName("com.mysql.jdbc.Driver");
	Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/employee_portal","root", "12345");
	//Statement stat=con.createStatement();
	String query = "SELECT * FROM leaves";
	PreparedStatement ps = (PreparedStatement) con.prepareStatement(query);

	ResultSet set = ps.executeQuery();
	
	leaves=new ArrayList<Leaves>();
	Format formatter = new SimpleDateFormat("dd/MM/yyyy");
	
	while(set.next()){
		Leaves obj=new Leaves();
		
				String fromDt = formatter.format(set.getDate(2));
				String toDt = formatter.format(set.getDate(3));
				obj.setEmpId(set.getInt(1));
	        	obj.setFromDate(fromDt);
	        	obj.setToDate(toDt);
	        	obj.setStatus(set.getString(4));
	        	leaves.add(obj);  		      
	}

		System.out.println("leaves list:"+leaves.size());
		
	return "/admin/viewLeaves.xhtml?faces-redirect=true";						
}catch(Exception e){
	e.printStackTrace();
}
return null;
}

public void onRowSelect(SelectEvent event){ 
	leave=new Leaves();
	
	 leave = (Leaves)event.getObject();
	     

	 System.out.println("Leave object:"+leave.getEmpId()+","+leave.getFromDate()+","+leave.getToDate());
	 } 


public String viewSalary(){
	try{
	Class.forName("com.mysql.jdbc.Driver");
	Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/employee_portal","root", "12345");
	//Statement stat=con.createStatement();
	String query = "SELECT * FROM salary WHERE empId=?";
	PreparedStatement ps = (PreparedStatement) con.prepareStatement(query);

	ps.setString(1, empId);

	ResultSet set = ps.executeQuery();
	
	salary=new ArrayList<Salary>();
	
	while(set.next()){
		
			Salary obj=new Salary();
			
			obj.setEmpId(set.getInt(1));
			obj.setAmount(set.getString(2));
			obj.setMonth(set.getString(3));
			obj.setYear(set.getString(4));
				
		salary.add(obj);  	
	      
	}
		System.out.println("salary list of all months:"+salary.size());
		
	return "/employee/viewSalary.xhtml?faces-redirect=true";						
}catch(Exception e){
	e.printStackTrace();
}
return null;
}

public String viewTimesheet(){
	try{
	Class.forName("com.mysql.jdbc.Driver");
	Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/employee_portal","root", "12345");
	//Statement stat=con.createStatement();
	String query = "SELECT * FROM timesheet WHERE empId=?";
	PreparedStatement ps = (PreparedStatement) con.prepareStatement(query);

	ps.setString(1, empId);

	ResultSet set = ps.executeQuery();
	
	timesheet=new ArrayList<Timesheet>();
	Format formatter = new SimpleDateFormat("dd/MM/yyyy");
	
	while(set.next()){
		Timesheet obj=new Timesheet();
		
				String date = formatter.format(set.getDate(5));
				obj.setEmpId(set.getInt(1));
				obj.setProject(set.getString(2));
				obj.setEmpName(set.getString(3));
				obj.setWorkingHrs(set.getString(4));
				obj.setDate(date);
				timesheet.add(obj);  	
	        
	      
	}

	
		System.out.println("timesheet list:"+timesheet.size());
		
	return "/employee/viewTimesheet.xhtml?faces-redirect=true";						
}catch(Exception e){
	e.printStackTrace();
}
return null;
}


public String viewAdminProfile(){
	System.out.println("admin username:"+adminUsername);
	try{
		Class.forName("com.mysql.jdbc.Driver");
		Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/employee_portal","root", "12345");
		//Statement stat=con.createStatement();
		String query = "SELECT * FROM admin WHERE username = ?";
		PreparedStatement ps = (PreparedStatement) con.prepareStatement(query);

		ps.setString(1, adminUsername);

		ResultSet set = ps.executeQuery();
		while(set.next()){
		        	adminId= set.getString(1);
		        	adminName= set.getString(2);
		        	adminUsername= set.getString(3);
		        	adminPassword= set.getString(4);	      
		}
	System.out.println(adminId+","+adminName+","+adminUsername+","+adminPassword);
		return "/admin/viewProfile.xhtml?faces-redirect=true";						
	}catch(Exception e){
		e.printStackTrace();
	}
	return null;	
}

public String viewProfile(){
	
	System.out.println("Link called, username is"+ username);
	
	 ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
    
try{
	Class.forName("com.mysql.jdbc.Driver");
	Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/employee_portal","root", "12345");
	//Statement stat=con.createStatement();
	String query = "SELECT * FROM employee WHERE username = ?";
	PreparedStatement ps = (PreparedStatement) con.prepareStatement(query);

	ps.setString(1, username);

	ResultSet set = ps.executeQuery();
 
	while(set.next()){
		
	        	empId= set.getString(1);
	        	empName= set.getString(2);
	        	username= set.getString(3);
	        	password= set.getString(4);
	      
	}
	
		System.out.println("Resultset is :"+empId+","+empName+","+username+","+password);
		
	return "/employee/viewProfile.xhtml?faces-redirect=true";						
}catch(Exception e){
	e.printStackTrace();
}
return null;
	
}

public String getAllProjects(){
	
	System.out.println("Link called, username is"+ username);
	
	 ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
    
try{
	Class.forName("com.mysql.jdbc.Driver");
	Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/employee_portal","root", "12345");
	//Statement stat=con.createStatement();
	String query = "SELECT * FROM projects";
	PreparedStatement ps = (PreparedStatement) con.prepareStatement(query);
	ResultSet set = ps.executeQuery();
 
	projects=resultSetToArrayList(set);
	System.out.println("Map of peojects:"+projects.get(1));
							
}catch(Exception e){
	e.printStackTrace();
}
return navigateToTimesheet();
	
}

public Map resultSetToArrayList(ResultSet rs) throws SQLException{
	Map<Integer, String> myMap = new HashMap<Integer, String>();

	while (rs.next()) {
	    Integer column1 = rs.getInt(1);
	    String column2 = rs.getString(2);
	    myMap.put(column1, column2);
	}
	
	return myMap;
	}

public String doLogout(){
	  FacesContext facesContext = FacesContext.getCurrentInstance();
	  HttpSession httpSession = (HttpSession)facesContext.getExternalContext().getSession(false);
	  httpSession.invalidate();
	  return "/index.xhtml?faces-redirect=true";
}

public String navigateToEditProfile(){
	 System.out.println("Forwarding to edit profile page.........");
	
     System.out.println("emp id is:"+empId);
	 return "/employee/editProfile.xhtml?faces-redirect=true&empId=" + empId;
}

public String navigateToAdminEditProfile(){
	 System.out.println("Forwarding to edit profile page.........");
	
    System.out.println("emp id is:"+empId);
	 return "/admin/editProfile.xhtml?faces-redirect=true&empId=" + adminId;
}

public String navigateToLeavePg(){
	System.out.println("Forwarding to apply leaves page..........");
	
    System.out.println("emp id is:"+empId);
	 return "/employee/leaveApply.xhtml?faces-redirect=true&empId=" + empId;
}

public String navigateToViewAllLeaves(){
System.out.println("Forwarding to apply leaves page..........");
	
    System.out.println("emp id is:"+empId);
	 return "/admin/viewLeaves.xhtml?faces-redirect=true";
}
public String navigateToAddSalary(){
	
System.out.println("Forwarding to add salary page..........");
	
    System.out.println("emp id is:"+adminId);
	 return "/admin/addSalary.xhtml?faces-redirect=true&adminId=" + adminId;
	 
}

public String navigateToTimesheet(){
System.out.println("Forwarding to add timesheet page..........");
	
    System.out.println("emp id is:"+empId);
	 return "/employee/addTimesheet.xhtml?faces-redirect=true&empId=" + empId;
}

public String navigateToEditLeaves(){
	
	System.out.println("Updating..");
	 if(leave != null && selectedRowIndex > -1)
		 System.out.println("selecetd row index is :"+selectedRowIndex+","+leave.getEmpId()+","+leave.getFromDate()+","+leave.getStatus()+","+leaveStatus); 
	 leaveEmpId=leave.getEmpId();
	 leaveFromDate=leave.getFromDate();
	 leaveToDate=leave.getToDate();
	 return "/admin/editLeaves.xhtml?faces-redirect=true";
}

public void updateLeaveStatus(){
	System.out.println("leave status:"+leaveStatus);
	SimpleDateFormat from = new SimpleDateFormat("dd/MM/yyyy");
	SimpleDateFormat to = new SimpleDateFormat("yyyy-MM-dd");
	Date fromDt = null;
	try {
		fromDt = from.parse(leaveFromDate);
	} catch (ParseException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}       // 01/02/2014
	Date toDt = null;
	try {
		toDt = from.parse(leaveToDate);
	} catch (ParseException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
	String mysqlFromDtString = to.format(fromDt)+" 00:00:00.00000";     // 2014-02-01
	String mysqlToDtString = to.format(toDt)+" 00:00:00.00000";     // 2014-02-01
	
		
	System.out.println("dates:"+mysqlFromDtString+","+mysqlToDtString);		 
	 Connection con = null;
	 try{
		 
			Class.forName("com.mysql.jdbc.Driver");
			 con=DriverManager.getConnection("jdbc:mysql://localhost:3306/employee_portal","root", "12345");
			Statement stat=con.createStatement();
			 con.setAutoCommit(true);
			 stat = con.createStatement();
			 
		     String sql = "UPDATE leaves " +
		                  "SET status = '"+leaveStatus+"' Where empId="+leaveEmpId+" and fromDate='"+mysqlFromDtString+"' and toDate='"+mysqlToDtString+"'";
		     stat.executeUpdate(sql);
		     viewLeavesOfEmployees();
		}catch(Exception e){
			e.printStackTrace();
		}
}

public String updateProfile(){
	
	System.out.println("Updating..");
	
	 ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
    String newId=empId;
    String newName=ec.getRequestParameterMap().get("form:name");
    String newUsername=ec.getRequestParameterMap().get("form:username");
    String newPassword=ec.getRequestParameterMap().get("form:password");
    Connection con = null;
try{
	Class.forName("com.mysql.jdbc.Driver");
	 con=DriverManager.getConnection("jdbc:mysql://localhost:3306/employee_portal","root", "12345");
	Statement stat=con.createStatement();
	 con.setAutoCommit(true);
	 stat = con.createStatement();
     String sql = "UPDATE employee " +
                  "SET employeename = '"+newName+"',username='"+newUsername+"',password='"+newPassword +"'WHERE idemployee="+newId;
     stat.executeUpdate(sql);
	 /*String query = "update employee set employeename = ? , username=? , password=? where idemployee=?";
     PreparedStatement preparedStmt = (PreparedStatement) con.prepareStatement(query);
     preparedStmt.setString(1,newName);
     preparedStmt.setString(2, newUsername);
     preparedStmt.setString(3, newPassword);
     preparedStmt.setString(4, newId);

     // execute the java preparedstatement
    boolean status=preparedStmt.execute();
     System.out.println("No. of rows updated: "+status);*/
      
	return viewProfile();					
}catch(Exception e){
	e.printStackTrace();
}

return null;
	
}


public String updateAdminProfile(){
	
	System.out.println("Updating..");
	
	 ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
    String newId=adminId;
    String newName=ec.getRequestParameterMap().get("form:name");
    String newUsername=ec.getRequestParameterMap().get("form:username");
    String newPassword=ec.getRequestParameterMap().get("form:password");
    Connection con = null;
try{
	Class.forName("com.mysql.jdbc.Driver");
	 con=DriverManager.getConnection("jdbc:mysql://localhost:3306/employee_portal","root", "12345");
	Statement stat=con.createStatement();
	 con.setAutoCommit(true);
	 stat = con.createStatement();
     String sql = "UPDATE admin " +
                  "SET name = '"+newName+"',username='"+newUsername+"',password='"+newPassword +"'WHERE idadmin="+newId;
     stat.executeUpdate(sql);
     
	return viewAdminProfile();					
}catch(Exception e){
	e.printStackTrace();
}

return null;
	
}

public String employeeAuthentication(){
	
	  ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
	     username = ec.getRequestParameterMap().get("formId:username");
	     password = ec.getRequestParameterMap().get("formId:password");
	    System.out.println("name is ;"+username);
	System.out.println("checking.......");
	try{
		Class.forName("com.mysql.jdbc.Driver");
		Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/employee_portal","root", "12345");
		Statement stat=con.createStatement();
		ResultSet set=stat.executeQuery("select * from employee");
		while(set.next()){
			empId=set.getString(1);
			empName=set.getString(2);
			if(username.equals(set.getString(3)) && password.equals(set.getString(4))){
				//ec.redirect("empLoginSuccess.xhtml");
				System.out.println("Authenticated!!!!!!!!   Username: "+set.getString(3)+", Password: "+set.getString(4));
				return  "/employee/empLoginSuccess.xhtml?faces-redirect=true&username=" + username;
				
			}else{
				System.out.println("Authentication error!!!");
				return null;
			}	
		}			
	}catch(Exception e){
		e.printStackTrace();
	}
	return null;
		
}

public String adminAuthentication(){
	
	  ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
	     adminUsername = ec.getRequestParameterMap().get("formId1:username");
	     adminPassword = ec.getRequestParameterMap().get("formId1:password");
	System.out.println("checking.......");
	try{
		Class.forName("com.mysql.jdbc.Driver");
		Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/employee_portal","root", "12345");
		Statement stat=con.createStatement();
		ResultSet set=stat.executeQuery("select * from admin");
		while(set.next()){
			adminId=set.getString(1);
			
			if(adminUsername.equals(set.getString(3)) && adminPassword.equals(set.getString(4))){
				//ec.redirect("empLoginSuccess.xhtml");
				System.out.println("Authenticated!!!!!!!!   Username: "+set.getString(3)+", Password: "+set.getString(4));
				return  "/admin/adminLoginSuccess.xhtml?faces-redirect=true&username=" + adminUsername;
				
			}else{
				System.out.println("Authentication error!!!");
				return null;
			}	
		}			
	}catch(Exception e){
		e.printStackTrace();
	}
	return null;
		
}

public String insertLeaves(){
	
	System.out.println("Inserting..");
	
	 ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
	 
	 DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss a");       
	String strFromDate = df.format(fromDate);
	String strToDate = df.format(toDate);
	System.out.println("string date: "+strFromDate);
	
	 SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss a"); // your template here
	 java.util.Date formattedFromDate = null;
	try {
		formattedFromDate = formatter.parse(strFromDate);
	} catch (ParseException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
	 java.sql.Date sqlFromDate = new java.sql.Date(formattedFromDate.getTime());
	 
	 java.util.Date formattedToDate = null;
	try {
		formattedToDate = formatter.parse(strToDate);
	} catch (ParseException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
	 java.sql.Date sqlToDate = new java.sql.Date(formattedToDate.getTime());
	 
    String status="Pending";
    System.out.println("Values:"+empId+","+sqlFromDate+","+sqlToDate);
    Connection con = null;
try{
	Class.forName("com.mysql.jdbc.Driver");
	 con=DriverManager.getConnection("jdbc:mysql://localhost:3306/employee_portal","root", "12345");
	 Statement stat=con.createStatement();
	 con.setAutoCommit(true);
	 stat = con.createStatement();
	 String sql = "INSERT INTO leaves " + "VALUES ('"+empId+"','"+sqlFromDate+"','"+sqlToDate+"','"+status+"')";
     stat.executeUpdate(sql);
	      
	return "/employee/empLoginSuccess.xhtml?faces-redirect=true";	
	
}catch(Exception e){
	e.printStackTrace();
}
finally
{
    try
    {
        con.close();
        System.out.println("Connection closed");
    }
    catch(SQLException e)
    {
        System.err.println(e);
        System.exit(1);
    }

 
}
return null;
	
}

public String addSalary(){

    Connection con = null;
try{
	Class.forName("com.mysql.jdbc.Driver");
	 con=DriverManager.getConnection("jdbc:mysql://localhost:3306/employee_portal","root", "12345");
	 Statement stat=con.createStatement();
	 con.setAutoCommit(true);
	 stat = con.createStatement();
	 String sql = "INSERT INTO salary " + "VALUES ('"+empId+"','"+salaryAmount+"','"+salaryMonth+"','"+salaryYear+"')";
     stat.executeUpdate(sql);
	      
	return "/employee/adminLoginSuccess.xhtml?faces-redirect=true";	
	
}catch(Exception e){
	e.printStackTrace();
}
finally
{
    try
    {
        con.close();
        System.out.println("Connection closed");
    }
    catch(SQLException e)
    {
        System.err.println(e);
        System.exit(1);
    }

 
}
return null;
	
}

public String addTimesheet(){
	
	System.out.println("Adding timesheet..");
	
	 	 
	 DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss a");       
	String strDate = df.format(date);
	
	 SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss a"); 
	 java.util.Date formattedDate = null;
	try {
		formattedDate = formatter.parse(strDate);
	} catch (ParseException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
	 java.sql.Date sqlDate = new java.sql.Date(formattedDate.getTime());
	 
	
    String status="Pending";
    System.out.println("Values:"+empId+","+sqlDate+","+projectId+","+workingHrs+","+empName);
    Connection con = null;
try{
	Class.forName("com.mysql.jdbc.Driver");
	 con=DriverManager.getConnection("jdbc:mysql://localhost:3306/employee_portal","root", "12345");
	 Statement stat=con.createStatement();
	 con.setAutoCommit(true);
	 stat = con.createStatement();
	 String sql = "INSERT INTO timesheet " + "VALUES ('"+empId+"','"+projectId+"','"+empName+"','"+workingHrs+"','"+sqlDate+"')";
     stat.executeUpdate(sql);
	      
	return "/employee/empLoginSuccess.xhtml?faces-redirect=true";	
	
}catch(Exception e){
	e.printStackTrace();
}
finally
{
    try
    {
        con.close();
        System.out.println("Connection closed");
    }
    catch(SQLException e)
    {
        System.err.println(e);
        System.exit(1);
    }

 
}
return null;
	
}

public String getUsername() {
	return username;
}

public void setUsername(String username) {
	this.username = username;
}

public ArrayList<EmployeeProfile> getEmployeeDataList() {
	return employeeDataList;
}

public void setEmployeeDataList(ArrayList<EmployeeProfile> employeeDataList) {
	this.employeeDataList = employeeDataList;
}

public String getEmpId() {
	return empId;
}

public void setEmpId(String empId) {
	this.empId = empId;
}

public String getPassword() {
	return password;
}

public void setPassword(String password) {
	this.password = password;
}

public String getEmpName() {
	return empName;
}

public void setEmpName(String empName) {
	this.empName = empName;
}

public Date getFromDate() {
	return fromDate;
}

public void setFromDate(Date fromDate) {
	this.fromDate = fromDate;
}

public Date getToDate() {
	return toDate;
}

public void setToDate(Date toDate) {
	this.toDate = toDate;
}

public String getProjectId() {
	return projectId;
}

public void setProjectId(String projectId) {
	this.projectId = projectId;
}

public String getWorkingHrs() {
	return workingHrs;
}

public void setWorkingHrs(String workingHrs) {
	this.workingHrs = workingHrs;
}

public Date getDate() {
	return date;
}

public void setDate(Date date) {
	this.date = date;
}

public Map<Integer, String> getProjects() {
	return projects;
}

public void setProjects(Map<Integer, String> projects) {
	this.projects = projects;
}

public ArrayList<Leaves> getLeaves() {
	return leaves;
}

public void setLeaves(ArrayList<Leaves> leaves) {
	this.leaves = leaves;
}

public ArrayList<Timesheet> getTimesheet() {
	return timesheet;
}

public void setTimesheet(ArrayList<Timesheet> timesheet) {
	this.timesheet = timesheet;
}

public ArrayList<Salary> getSalary() {
	return salary;
}

public void setSalary(ArrayList<Salary> salary) {
	this.salary = salary;
}

public String getAdminId() {
	return adminId;
}

public void setAdminId(String adminId) {
	this.adminId = adminId;
}

public String getAdminName() {
	return adminName;
}

public void setAdminName(String adminName) {
	this.adminName = adminName;
}

public String getAdminUsername() {
	return adminUsername;
}

public void setAdminUsername(String adminUsername) {
	this.adminUsername = adminUsername;
}

public String getAdminPassword() {
	return adminPassword;
}

public void setAdminPassword(String adminPassword) {
	this.adminPassword = adminPassword;
}

public String getSalaryMonth() {
	return salaryMonth;
}

public void setSalaryMonth(String salaryMonth) {
	this.salaryMonth = salaryMonth;
}

public String getSalaryYear() {
	return salaryYear;
}

public void setSalaryYear(String salaryYear) {
	this.salaryYear = salaryYear;
}

public String getSalaryAmount() {
	return salaryAmount;
}

public void setSalaryAmount(String salaryAmount) {
	this.salaryAmount = salaryAmount;
}

public Map<String, Integer> getMonths() {
	return months;
}

public void setMonths(Map<String, Integer> months) {
	this.months = months;
}

public String getLeaveStatus() {
	return leaveStatus;
}

public void setLeaveStatus(String leaveStatus) {
	this.leaveStatus = leaveStatus;
}

public Leaves getLeave() {
	return leave;
}

public void setLeave(Leaves leave) {
	this.leave = leave;
}

public int getSelectedRowIndex() {
	return selectedRowIndex;
}

public void setSelectedRowIndex(int selectedRowIndex) {
	this.selectedRowIndex = selectedRowIndex;
}

public int getLeaveEmpId() {
	return leaveEmpId;
}

public void setLeaveEmpId(int leaveEmpId) {
	this.leaveEmpId = leaveEmpId;
}

public String getLeaveFromDate() {
	return leaveFromDate;
}

public void setLeaveFromDate(String leaveFromDate) {
	this.leaveFromDate = leaveFromDate;
}

public String getLeaveToDate() {
	return leaveToDate;
}

public void setLeaveToDate(String leaveToDate) {
	this.leaveToDate = leaveToDate;
}



}
