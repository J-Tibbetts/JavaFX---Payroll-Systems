/*
  Jeff Tibbetts
  08.16.2021
  Final Project - Java 285
  Original ReadEmployeeRecord, WriteEmployeeRecord, EmployeeRecord and EmpRec all provided
  by Prof Haley
  
*/
/*
The purpose of this interface is to manipulate date objects
into different sized maps to appear in the menus to select
hiredates, starttimes and end times for shifts/reports

*/
import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;

public interface Time_IFace{
	//By retrieving the time in millisecond using a Date's getTime method
	//we can then multiply the amount of time we would like to add/subtract
	//to these constants to calculate our new date object
	final static int MILLIS_TO_MIN =1000*60,
	                 MILLIS_TO_HR  = 1000*60*60,
					 MILLIS_TO_DAY = 1000*60*60*24,
					 MILLIS_TO_WK  = 1000*60*60*24*7,
					 
					 //These values are to be added to a date object
					 //set to midnight to generate that day's open and close time
					 //restaurants close past midnight, therefore the end value is
					 //27, or 3AM the next day
					 OPEN_HOUR = 10*MILLIS_TO_HR,
					 CLOSE_HOUR = 27*MILLIS_TO_HR;
				
	final static String CAL_DATE      = "MMMM-dd-yyy",
	                    DATE_TIME     = "MMMM-dd-yyy hh:mm a",
						CLOCK_TIME    = "hh:mm a"; 
	
	
	
	//This function will be used to create maps which populate comboboxes which reference
	//retroactive dates such as hiredates or report dates
	public static HashMap<String, Date> getPriorDates(int range){
		return getPriorDates(range, new Date());
	}
	
	public static HashMap<String, Date> getPriorDates(int range, Date date){
		SimpleDateFormat dateFormat=new SimpleDateFormat(CAL_DATE);
		HashMap<String, Date>reportDateOptions=new HashMap<>();
		
		date=Time_IFace.truncateDay(date);
		reportDateOptions.put(dateFormat.format(date),date);
		for(int i = 0; i <=range; i++){

			date=new Date(date.getTime()-MILLIS_TO_DAY);
			reportDateOptions.put(dateFormat.format(date), date);			
		}
		return reportDateOptions;
	}
	
	public static HashMap<String, Date> getComingDates(int range){
		
		return getComingDates(range, new Date());
	}
	
	
	//This function will be used to create mapes which populate comboboxes which reference
	//dates which have yet to occur such as scheduling dates
	public static HashMap<String, Date> getComingDates(int range, Date date){
		SimpleDateFormat dateFormat=new SimpleDateFormat(CAL_DATE);
		date=truncateDay(date);
		
		HashMap<String, Date>comingDateOptions=new HashMap<>();
		
		comingDateOptions.put(dateFormat.format(date), date);
		for(int i=0; i<=range; i++){
			
			date=new Date(date.getTime()+MILLIS_TO_DAY);
			comingDateOptions.put(dateFormat.format(date), date);
		}
		return comingDateOptions;
	}
	
	public static HashMap<String, Date> getDates(int range){
		HashMap<String, Date> dates=new HashMap(getPriorDates(range));
		dates.putAll(getComingDates(range));
		
		return dates;
		
	}
	
	
	public static HashMap<String, Date> getStartTimes(final Date date){
		SimpleDateFormat dateFormat=new SimpleDateFormat(CLOCK_TIME);
		HashMap<String, Date> startTimes=new HashMap<>();
		//First acquire the starting date
		Date time=truncateDay(date);
		//Then establish different benchmarks 
		Date tomorrow=new Date(date.getTime()+MILLIS_TO_DAY);
		time=new Date(date.getTime()+OPEN_HOUR);
		int halfHour = MILLIS_TO_HR/2;
		while(time.before(tomorrow)){
			startTimes.put(dateFormat.format(time), time);
			time=new Date(time.getTime()+halfHour);
		}
		return startTimes;
	}
	
	public static HashMap<String, Date> getEndTimes(final Date start){
		SimpleDateFormat dateFormat=new SimpleDateFormat(DATE_TIME);
		HashMap<String, Date> endTimes=new HashMap<>();
		
		Date closingTime=new Date(truncateDay(start).getTime()+CLOSE_HOUR);
		int halfHour= MILLIS_TO_HR/2;
		Date date=new Date(start.getTime()+halfHour);
		while(!date.after(closingTime)){
			endTimes.put(dateFormat.format(date),date);
			date=new Date(date.getTime()+halfHour);
		}
		return endTimes;
	}
	
	public static double calcHours(Date start, Date end){
		return (int)((end.getTime()-start.getTime())/MILLIS_TO_HR);
	}
	

	public static Date truncateDay(Date date){
		Calendar calendar =new GregorianCalendar();
		calendar.setTimeInMillis(date.getTime());
		int monthTemp = calendar.get(Calendar.MONTH);
		int dayTemp = calendar.get(Calendar.DAY_OF_MONTH);
		int yearTemp = calendar.get(Calendar.YEAR);
		
		calendar=new GregorianCalendar(yearTemp, monthTemp, dayTemp);
		return calendar.getTime();
	}
	
	public static Date getOpenTime(Date date){
		
		return new Date(truncateDay(date).getTime()+OPEN_HOUR);
	}
	
	public static Date getCloseTime(Date date){
		
		return new Date(truncateDay(date).getTime()+CLOSE_HOUR);
	}
	

}