/*
  Jeff Tibbetts
  08.16.2021
  Final Project - Java 285
  Original ReadEmployeeRecord, WriteEmployeeRecord, EmployeeRecord and EmpRec all provided
  by Prof Haley

*/
/*
	With the ShiftRec functioning as a bridge table between employeerec and positionrec,
	the Shift_IFace mostly serves to feed values to the report objects which then interpret the results	
*/
import java.io.*;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Date;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

public interface Shift_IFace{
		public final String SHIFT_FILEPATH="shiftRec.dat";
		
	public static HashMap<String, ShiftRec> getAllShifts()throws IOException{
		return getShiftsBetween(null, null);
	}
	
	public static HashMap<String, ShiftRec> getShiftsBetween(Date startDate, Date endDate)throws IOException{
		
		RandomAccessFile input=new RandomAccessFile(SHIFT_FILEPATH, "r");
		HashMap<String, ShiftRec> shifts=new HashMap<>();
		try{
			Shift shift=new Shift();
			Date startTemp, endTemp;
			while(true){
				shift.read(input);
				startTemp=shift.getStart();
				endTemp=shift.getEnd();
				
				//First predicate makes date filtering get bypassed  altogether, returning every shift on file				
				if(startDate!=null&&endDate!=null){
					//otherwise we check that the shift falls within the timeframe specified by the start and enddate.
					//if the shift has no overlap, we continue the loop without creating additional objects
					if(shift.getEnd().before(startDate) || shift.getStart().after(endDate))
					continue; 
					else{
						//Final step is to create 'temporary start and end times' for the purpose of calculating the shift's 
						//labor cost within the designated time period
						if(shift.getStart().before(startDate))
						startTemp=new Date(startDate.getTime());
						
						if(shift.getEnd().after(endDate))
						endTemp=new Date(endDate.getTime());
					
					}
				}
				ShiftRec shiftRec=new ShiftRec(shift.getShiftId(), shift.getDept(), shift.getJobCode(), shift.getPayrate(), shift.getEmpId(), startTemp, endTemp);
				shifts.put(shiftRec.toString(), shiftRec);
			}
		}catch(EOFException er){
			System.out.println("Shift Data Loaded");
		}
		return shifts;
	}
	
	
	//This method allows the shift to be looked up by it's shiftId within the random access file
	//Once returned, the shift may be stored or its methods may be invoked
	public static ShiftRec getShiftByIndex(int index)throws IOException{
		RandomAccessFile input=new RandomAccessFile(SHIFT_FILEPATH, "r");
		Shift shift=new Shift();
		
		input.seek((index-1)*Shift.size());
		shift.read(input);
		return (new ShiftRec(shift.getShiftId(), shift.getDept(), shift.getJobCode(), shift.getPayrate(), shift.getEmpId(), shift.getStart(), shift.getEnd()));
	}
	
	
	
}