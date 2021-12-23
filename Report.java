/*
  Jeff Tibbetts
  08.16.2021
  Final Project - Java 285
  Original ReadEmployeeRecord, WriteEmployeeRecord, EmployeeRecord and EmpRec all provided
  by Prof Haley

*/
/*
	This class utilizes the Shift_IFace to store a HashMap storing ShiftRecs
	over a designated period of time. Should the period assigned to the labor
	report be null, the report will retrieve all scheduled shifts
	
*/
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.DecimalFormat;

import java.util.HashMap;
import java.util.ArrayList;
import java.io.IOException;

public class Report implements Shift_IFace{
	Date periodStart;
	Date periodEnd;
	
	HashMap<String, ShiftRec>shifts;
	ArrayList<Integer> employees;
	
	
	public Report(Date periodStart, Date periodEnd){
		this.periodStart=new Date(periodStart.getTime());
		this.periodEnd=new Date(periodEnd.getTime());
		try{
		shifts=new HashMap<String, ShiftRec>(Shift_IFace.getShiftsBetween(periodStart, periodEnd));
		}catch(IOException er){
			System.err.println("An error occurred accessing shift files");
			shifts=new HashMap<String, ShiftRec>();
		}
		
		
		employees=new ArrayList<Integer>();
		for(String str: shifts.keySet()){
			if(!employees.contains(shifts.get(str).getEmpId()))
				employees.add(shifts.get(str).getEmpId());
		}
	}
	
	//Accessors were added, however, given the report creates a new
	//shift set on creation, independent manipulation of dates would
	//distort data
	public Date getPeriodStart(){
		
		return periodStart;
	}
	
	public Date getPeriodEnd(){
		
		return periodEnd;
	}
	
	public double getDeptLabor(char dept){
		double accumulator = 0.0;
		for(String str: shifts.keySet()){
			if(shifts.get(str).getDept()==dept)
				accumulator += shifts.get(str).calcLabor();
		}
		return accumulator;
	}
	
	public double getEmployeeLabor(int empId){
		double accumulator = 0.0;
		for(String str: shifts.keySet()){
			if(shifts.get(str).getEmpId()==empId)
				accumulator += shifts.get(str).calcLabor();
		}
		return accumulator;
	}
	

	//This will take the entire shiftSet and cross reference every empId with its EmpRec file
	//and every position with its dept and jobcode
	public ArrayList<String> getEmployeeRoster()throws IOException{
		EmpRec emprec;
		ArrayList<String> keys=new ArrayList<>(shifts.keySet());
		ArrayList<String> reportLines=new ArrayList<>();
		keys.sort(
		(String s1, String s2)->{
			if(shifts.get(s1).getStart().before(shifts.get(s2).getStart())){return -1;}
			else if(shifts.get(s1).getStart().after(shifts.get(s2).getStart())){return 1;}
			else{return 0;}});
			
		
		for(String str: keys){
			try{
			emprec=Employee_IFace.getEmployeeRecord(shifts.get(str).getEmpId());
			reportLines.add(emprec.getComboString()+"\n"
					+Position_IFace.getPositionRec(emprec.getDept(), shifts.get(str).getJobCode()).getRosterListing()+"\n"
					+shifts.get(str).getRosterListing()+"\n\n");
			}catch(IOException er){
				System.err.println("An error occured accessing RandomAccessFiles "+er.toString());
			}
		}
		return reportLines;
	}
	
	public ArrayList<String> getLaborReport()throws IOException{
		ArrayList<String> reportLines=new ArrayList<>();
		StringBuffer buff=new StringBuffer();
		DecimalFormat money=new DecimalFormat("$0.00");
		EmpRec emprec;
		double fOHLabor=getDeptLabor('F');
		double bOHLabor=getDeptLabor('B');
		double eventLabor=getDeptLabor('E');
		
		reportLines.add("Net Labor Costs: "+money.format(fOHLabor+bOHLabor+eventLabor));
		reportLines.add("Front of House Labor Costs: "+money.format(fOHLabor));
		reportLines.add("Back of House Labor Costs: "+money.format(bOHLabor));
		reportLines.add("Event Labor Costs: "+money.format(eventLabor));
		for(int i: employees){
			try{
			emprec=Employee_IFace.getEmployeeRecord(i);
			reportLines.add("Labor Costs for "+emprec.getComboString()+": "+money.format(getEmployeeLabor(i)));
			}catch(IOException er){
				System.err.println("An error occured accessing RandomAccessFiles "+er.toString());
			}				
		}
		
		return reportLines;
	}
}