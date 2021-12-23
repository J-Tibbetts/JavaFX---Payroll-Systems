/*
  Jeff Tibbetts
  08.16.2021
  Final Project - Java 285
  Original ReadEmployeeRecord, WriteEmployeeRecord, EmployeeRecord and EmpRec all provided
  by Prof Haley
  
*/
/*
	The following interface allows the implementing class to access static functions which retrieve
	data from the emprec file. Its used to load comboBoxes that reference EmpRec as well
*/
import java.util.Date;
import java.util.HashMap;

import java.io.RandomAccessFile;
import java.io.*;

import java.text.SimpleDateFormat;
import java.lang.NumberFormatException;
import java.io.IOException;


public interface Employee_IFace {
	
	public static final String EMPLOYEE_FILEPATH = "employeeRecord.dat";
	
	//This function will access the EmpRec file to create a hashMap containing keys which are strings with basic employe
	//info, passing the key to the map will return the value of the Employee's record number, which gets stored in the file
	public static HashMap<String, Integer> getEmployeeOptions(char deptFilter, int positionCode) throws IOException{
		HashMap<String, Integer> employeeOptions = new HashMap<>();
		RandomAccessFile input=new RandomAccessFile(EMPLOYEE_FILEPATH, "r");;
		
		try{
			EmployeeRecord employee=new EmployeeRecord();
			while(true){
				employee.read(input);
				if(employee.getDept()!=deptFilter&&deptFilter!='E')
					continue;
				if(employee.getMaxPosition() < positionCode)
					continue;
				EmpRec emprec=new EmpRec(employee);
				
				employeeOptions.put(emprec.getComboString(), employee.getRecord());
				
			}
		}catch(EOFException er){
			System.out.println("Employee Data Loaded");
		}
	
		return employeeOptions;
	}
	
	//This function will return an emprec file with the specified record#
	//Once returned this Emprec can be stored or its methods may be invoked
	public static EmpRec getEmployeeRecord(int EmpId)throws IOException{
		RandomAccessFile input=new RandomAccessFile(EMPLOYEE_FILEPATH, "r");
		EmployeeRecord employee=new EmployeeRecord();

		input.seek((EmpId-1)*EmployeeRecord.size());
		employee.read(input);
		
		EmpRec emprec=new EmpRec(employee);
		input.close();
		
		return emprec;
	}
	
}