/*
  Jeff Tibbetts
  08.16.2021
  Final Project - Java 285
  Original ReadEmployeeRecord, WriteEmployeeRecord, EmployeeRecord and EmpRec all provided
  by Prof Haley

*/
/*
	The following class stores data for the various clock in codes to 
	be used by hourly employees in this workplace.
	Records are identified with a composite key, consisting of the 
	dept code and a jobCode

*/

import java.text.DecimalFormat;


public class PositionRec{
	private String title;
	private char dept;
	private int jobCode;
	private double payrate;
	
	public PositionRec(String title, char dept, int jobCode, double payrate){
		this.title =title;
		this.dept =dept;
		this.jobCode =jobCode;
		this.payrate=payrate;
	}
	
	public PositionRec(Position p){
		this.title=p.getTitle();
		this.dept=p.getDept();
		this.jobCode=p.getJobCode();
		this.payrate=p.getPayrate();
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
	
	//Will return a string omitting the position's payrate, keeping private information
	//off a public access document
	public String getRosterListing(){
		DecimalFormat jobCodeformat=new DecimalFormat("000");
		return dept+jobCodeformat.format(jobCode)+" "+title;
	}
	
	//Will Return a String to be placed in a hashMap, this string be the key to the position's
	//read position within the file
	public String toString(){
		DecimalFormat jobCodeformat=new DecimalFormat("000");
		DecimalFormat money = new DecimalFormat("$0.00");

		return(dept+jobCodeformat.format(jobCode)+":"+title+" - "+money.format(payrate));
	}
}
