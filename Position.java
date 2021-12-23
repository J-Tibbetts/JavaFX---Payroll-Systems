/*
  Jeff Tibbetts
  08.16.2021
  Final Project - Java 285
  Original ReadEmployeeRecord, WriteEmployeeRecord, EmployeeRecord and EmpRec all provided
  by Prof Haley
  
*/
/*
	Read/Write objecct for positions,
	modified from original EmployeeRecord.java
*/
import java.io.*;
import java.io.RandomAccessFile;
import java.text.DecimalFormat;

public class Position {
	private String title;
	private char dept;
	private int jobCode;
	private double payrate;
	
	public Position(){
		this.jobCode = 0;		
		this.title="";
		this.dept='B';
		this.payrate = 0.0;
	}
	

	
    public void write(RandomAccessFile file) throws IOException{
        
        
        StringBuffer buff;
        if(title != null){
            buff = new StringBuffer(title);
        }else{
            buff = new StringBuffer(15);
        }
        buff.setLength(15); //set length
        file.writeChars(buff.toString()); //write string as sequense of characters
        

        file.writeChar(dept);
        file.writeInt(jobCode);
		file.writeDouble(payrate);
    }

    public void read(RandomAccessFile file) throws IOException{
    
        
        char[] arr = new char[15];
        for(int i=0; i<arr.length; i++){
            arr[i] = file.readChar();//read chars from a file
        }
        title = new String(arr);
       
        
        dept = file.readChar();
        jobCode = file.readInt();
		payrate = file.readDouble();
    }
	
	//Accessors and mutators
	public void setTitle(String title){
		
		this.title = title;
	}
	
	public void setDept(char dept){

		this.dept = dept;
	}
	
	public void setJobCode(int jobCode){
		
		this.jobCode = jobCode;
	}
	
	public void setPayrate(double payrate){
		
		this.payrate=payrate;
	}
	
	public String getTitle(){
		
		return title;
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
	
	public static int size(){
		
		return ((15*2)+2+4+8);
		//15 char[]+char+int+double
	}
}