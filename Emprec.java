/*
  Jeff Tibbetts
  08.16.2021
  Final Project - Java 285
  Original ReadEmployeeRecord, WriteEmployeeRecord, EmployeeRecord and EmpRec all provided
  by Prof Haley
  
*/
/*
	I have three sets of records for you and two of them aren't emprec!
	And the one that is, happens to be heavily modified

	This version of emprec contains employee contact and scheduling information
	Given this business' employees use jobcodes to clock in, the payrate field
	is irrelevent, and asking a restaurant employee their age or dependents is
	more likely to incur an HR intervention.
	This EmpRec integrates the primary key of Positions(char dept, int jobcode)
	and creates an inequity relationship where members of a dept can be scheduled
	up to a certain jobcode within their dept.

*/

import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.DecimalFormat;

public class EmpRec {
	
	private int record;
    private String firstName;
    private String lastName;
	private String phone;
	private String email;
    private char dept;
    private int maxPosition;
    private Date hireDate;
	
	
	public EmpRec(int record, String firstName, String lastName, String phone, String email, char dept, int maxPosition, Date hireDate){
		this.record=record;
		this.firstName=firstName;
		this.lastName=lastName;
		this.phone=phone;
		this.email=email;
		this.dept=dept;
		this.maxPosition=maxPosition;
		this.hireDate=new Date(hireDate.getTime());
	}
	
	public EmpRec(EmployeeRecord e){
		this.record=e.getRecord();
		this.firstName=e.getFirstName();
		this.lastName=e.getLastName();
		this.phone=e.getPhone();
		this.email=e.getEmail();
		this.maxPosition=e.getMaxPosition();
		this.hireDate=new Date(e.getHireDate().getTime());
	}
	
	public char getDept(){
		
		return dept;
	}
	
	//This string is to be used both for populating combo boxes with employee details and 
	//as the employee's headder for rosterListings
	public String getComboString(){
		DecimalFormat empIdFormat=new DecimalFormat("000");
		return (dept+empIdFormat.format(record)+" "+firstName+" "+lastName);
	}
	
	public String toString(){
		DecimalFormat empIdFormat=new DecimalFormat("000");
		SimpleDateFormat dateFormat=new SimpleDateFormat("MMMM-dd-YYYY");
		return
			("EmpID: "+record+"\n"
			+"First Name: "+firstName+"\n"
			+"Last Name: "+lastName+"\n"
			+"Phone: "+phone+"\n"
			+"Email: "+email+"\n"
			+"Department: "+dept+"\n"
			+"Max JobCode: "+maxPosition+"\n"
			+"HireDate: "+dateFormat.format(hireDate));
	};
	
}