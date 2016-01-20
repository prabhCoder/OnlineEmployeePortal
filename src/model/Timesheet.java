package model;

public class Timesheet {

	private int empId;
	private String project;
	private String empName;
	private String workingHrs;
	private String date;
	
	
	public Timesheet() {
		super();
		// TODO Auto-generated constructor stub
	}
	public int getEmpId() {
		return empId;
	}
	public void setEmpId(int empId) {
		this.empId = empId;
	}
	public String getProject() {
		return project;
	}
	public void setProject(String project) {
		this.project = project;
	}
	public String getEmpName() {
		return empName;
	}
	public void setEmpName(String empName) {
		this.empName = empName;
	}
	public String getWorkingHrs() {
		return workingHrs;
	}
	public void setWorkingHrs(String workingHrs) {
		this.workingHrs = workingHrs;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	
}
