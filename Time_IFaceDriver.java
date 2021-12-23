/*
  Jeff Tibbetts
  08.16.2021
*/
//This is just a driver I used to tweek the Time_IFace 
//


import java.util.Date;
import java.text.SimpleDateFormat;

public class Time_IFaceDriver implements Time_IFace {

public static void main(String[] args){
	Date date=new Date();
	SimpleDateFormat dateFormat=new SimpleDateFormat(Time_IFace.DATE_TIME);
	
	System.out.println("Today's date: "+dateFormat.format(date));
	Date date2=Time_IFace.truncateDay(date);
	System.out.println("Truncated date: "+dateFormat.format(date2));
	for(int i=0; i<30; i++){
		date2=new Date(date2.getTime()+Time_IFace.MILLIS_TO_DAY);
		System.out.println("Next day is: "+dateFormat.format(date2));
		System.out.println("Bar opens at: "+dateFormat.format(new Date(date2.getTime()+Time_IFace.OPEN_HOUR))+"\n"
							+"\tCloses at: "+dateFormat.format(new Date(date2.getTime()+Time_IFace.CLOSE_HOUR)));
	}
}	
}