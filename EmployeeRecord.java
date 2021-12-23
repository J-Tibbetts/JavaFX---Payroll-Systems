/*
  Jeff Tibbetts
  08.16.2021
  Final Project - Java 285
  Original ReadEmployeeRecord, WriteEmployeeRecord, EmployeeRecord and EmpRec all provided
  by Prof Haley
  -Its been a great semester!
*/
/*
	Read/Write object for Employee records
	original, payroll oriented fields, have been
	exchanged for scheduling and contact info
*/
//package pkg;

import java.io.*;
import java.io.RandomAccessFile;
import java.util.Date;

public class EmployeeRecord{

    //define properties
    private int record;
    private String firstName;
    private String lastName;
	private String phone;
	private String email;
    private char dept;
    private int maxPosition;
    private Date hireDate;
    
    //default constructor
    public EmployeeRecord(){
        
        record = 0;
        firstName = "";
        lastName = "";
		phone = "";
		email = "";
        dept = ' ';
        maxPosition = 0;
        this.hireDate = new Date();
    }
    
    //define write method - to write data to a file
    public void write(RandomAccessFile file) throws IOException{
        
        file.writeInt(record);
        
        StringBuffer buff;
		
        if(firstName != null){
            buff = new StringBuffer(firstName);
        }else{
            buff = new StringBuffer(15);
        }
        buff.setLength(15); //set length
        file.writeChars(buff.toString()); //write string as sequense of characters
        
        if(lastName != null){
            buff = new StringBuffer(lastName);
        }else{
            buff = new StringBuffer(15);
        }
        buff.setLength(15);
        file.writeChars(buff.toString());
        
		
		if(phone !=null){
			buff =new StringBuffer(phone);
		}else{
			buff =new StringBuffer(11);
		}
		buff.setLength(11);
		file.writeChars(buff.toString());
		
		if(email !=null){
			buff =new StringBuffer(email);
		}else{
			buff =new StringBuffer(20);
		}
		buff.setLength(20);
		file.writeChars(buff.toString());
		
		
        file.writeChar(dept);
        file.writeInt(maxPosition);
		file.writeLong(hireDate.getTime());
    }
    
    //define read method to read a record from a file
    public void read(RandomAccessFile file) throws IOException{
    
        record = file.readInt();
        
        char[] arr = new char[15];
        for(int i=0; i<arr.length; i++){
            arr[i] = file.readChar();//read chars from a file
        }
        firstName = new String(arr);
        
        char[] arr2 = new char[15];
        for(int i=0; i<arr2.length; i++){
            arr2[i] = file.readChar();//read chars from a file
        }
        lastName = new String(arr2);
        
		
		char[] arr3 =new char[11];
		for(int i=0; i<arr3.length; i++){
			arr3[i] =file.readChar();//continue reading characters from file
		}
		phone = new String(arr3);
		
		char[] arr4 =new char[20];
		for(int i=0; i<arr4.length; i++){
			arr4[i]=file.readChar();
		}
		email=new String(arr4);
        
        dept = file.readChar();
        maxPosition = file.readInt();
		hireDate = new Date(file.readLong());
    }
    
    //define mutator methods
    public void setRecord(int record){
        
        this.record = record;
    }
    
    public void setFirstName(String firstName){
        
        this.firstName = firstName;
    }
    
    public void setLastName(String lastName){
        
        this.lastName = lastName;
    }
	
	public void setPhone(String phone){
	
		this.phone = phone;
	}
	
	public void setEmail(String email){
		
		this.email=email;
	}
	
    
    public void setDept(char dept){
        
        this.dept = dept;
    }
    
    public void setMaxPosition(int maxPosition){
        
        this.maxPosition = maxPosition;
    }
    
    public void setHireDate(Date hireDate){
		
		this.hireDate =hireDate;
	}
    
    //define accessor methods
    
    public int getRecord(){
        
        return this.record;
    }
    
    public String getFirstName(){
        
        return this.firstName;
    }
    
    public String getLastName(){
        
        return this.lastName;
    }
	
	public String getPhone(){
		
		return this.phone;
	}
	public String getEmail(){
		
		return this.email;
	}
    
    public char getDept(){
        
        return this.dept;
    }
    
    public int getMaxPosition(){
        
        return this.maxPosition;
    }
    
    public Date getHireDate(){
        
        return this.hireDate;
    }
	
	

    
    //define size() method which return size of a record
    public static int size(){
    
        return (4+15*2+15*2+11*2+20*2+2+4+8); //int+char[15]+char[15]+char[11]+char[20]+char+int+date
    }
    
    
}