/*
  Jeff Tibbetts
  08.16.2021
  Final Project - Java 285
  Original ReadEmployeeRecord, WriteEmployeeRecord, EmployeeRecord and EmpRec all provided
  by Prof Haley

*/

/*
	Read/Write objecct for shifts,
	modified from original EmployeeRecord.java
*/
import java.io.RandomAccessFile;
import java.io.*;
import java.util.Date;

public class Shift {
	private int shiftId;
	private Date start;
	private Date end;
	private char dept;
	private int jobCode;
	private double payrate;
	private int empId;
	
	//default constructor
	public Shift(){
		this.shiftId = 0;
		this.start=new Date();
		this.end=new Date();
		this.dept = 'T';
		this.jobCode = 0;
		this.payrate = 0.0;
		this.empId = 0;
	}
	
	public void write(RandomAccessFile file) throws IOException{
        file.writeLong(start.getTime());
		file.writeLong(end.getTime());
        file.writeInt(shiftId);
        file.writeChar(dept);
		file.writeInt(jobCode);
        file.writeDouble(payrate);
		file.writeInt(empId);
    }
	
	public void read(RandomAccessFile file) throws IOException{
		setStart(new Date(file.readLong()));
		setEnd(new Date(file.readLong()));
        shiftId = file.readInt();
        dept = file.readChar();
		jobCode = file.readInt();
        payrate = file.readDouble();
		empId = file.readInt();
		
    }
	
	public void setShiftId(int shiftId){
		
		this.shiftId =shiftId;
	}
	
	public void setStart(Date start){
		
		this.start = start;
	}
	
	public void setEnd(Date end){
		
		this.end = end;
	}

	public void setDept(char dept){
		
		this.dept = dept;
	}	
	
	public void setJobCode(int jobCode){
		
		this.jobCode = jobCode;
	}

	public void setPayrate(double payrate){
		
		this.payrate = payrate;
	}
	
	public void setEmpId(int empId){
		
		this.empId = empId;
	}	
	
	public int getShiftId(){
		
		return shiftId;
	}
	
	public Date getStart(){
		
		return start;
	}
	
	public Date getEnd(){
		
		return end;
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

	
	public static int size(){
		
		return (8+8+4+2+4+8+4);
		//date+date+int+char+int+double+int
	}
}