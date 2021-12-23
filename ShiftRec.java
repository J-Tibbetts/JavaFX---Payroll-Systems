/*
  Jeff Tibbetts
  08.16.2021
  Final Project - Java 285
  Original ReadEmployeeRecord, WriteEmployeeRecord, EmployeeRecord and EmpRec all provided
  by Prof Haley

*/
/*
  Shifts for this business reference a time period dictated by the period's
  start and end, a dept and jobCode which reference that shift's position,
  and an employeeId, referencing the employee scheduled.
  
  Shifts 

*/

import java.text.SimpleDateFormat;
import java.text.DecimalFormat;
import java.util.Date;

public class ShiftRec implements Time_IFace {
	private int shiftId;
	private char dept;
	private int jobCode;
	private double payrate;
	private int empId;
	private Date start;
	private Date end;
	

	//Argument based Constructor
	public ShiftRec(int shiftId, char dept, int jobCode, double payrate, int empId, Date start, Date end){
		this.shiftId = shiftId;
		this.dept = dept;
		this.jobCode = jobCode;
		this.payrate = payrate;
		this.empId = empId;		
		this.start=new Date(start.getTime());
		this.end = new Date(end.getTime());
	}
		//This calcLabor function will calculate pay for entire shift

	
	public int getShiftId(){
		
		return shiftId;
	}
		
	public char getDept(){
		
		return dept;
	}
	
	public int getJobCode(){
		
		return jobCode;
	}
	
	public double getPayrate(){
		
		return payrate;
	}
	
	public int getEmpId(){
		
		return empId;
	}
	
	public Date getStart(){
		
		return start;
	}
	
	public Date getEnd(){
		
		return end;
	}
	
	//This is the basis of most calculations involving the shift object
	public double calcLabor(){
		
		return (Time_IFace.calcHours(this.start, this.end)*payrate);
	}
	
	//This is an abbridged listing for the shift object, omitting the position
	//and employee data, which can be called using both the shift's accessor and
	//mututator functions and the PositionRec_IFace.getPosition(dept, jobCode)
	//and the EmpRec_IFace.getEmployee(empId) methods
	public String getRosterListing(){
		SimpleDateFormat dateFormat=new SimpleDateFormat("M/dd/yyyy, hh:mm a");
		SimpleDateFormat endFormat=new SimpleDateFormat("hh:mm a");
		return dateFormat.format(start)+"-"+endFormat.format(end);
	}
	
	//Returns a single line string to list all members of the shift data in a larger
	//report
	public String toString(){
		
		SimpleDateFormat dateFormat=new SimpleDateFormat("hh:mm a, M/dd/yyyy");
		DecimalFormat idFormat=new DecimalFormat("000");
		DecimalFormat jobCodeFormat=new DecimalFormat("00");
		String deptStr;
		
		return
		(idFormat.format(empId)+"-"+dept+jobCodeFormat.format(jobCode)+" "+dateFormat.format(start)+"-"+dateFormat.format(end));
	}
}