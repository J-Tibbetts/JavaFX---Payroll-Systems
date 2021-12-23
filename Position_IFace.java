/*
  Jeff Tibbetts
  08.16.2021
  Final Project - Java 285
  Original ReadEmployeeRecord, WriteEmployeeRecord, EmployeeRecord and EmpRec all provided
  by Prof Haley

*/

//This interface is used with the various javaFx classes which access position data, it can return either a
//hashMap containing all the position codes for a dept or a specific positionrec file to access its methods

import java.io.*;
import java.io.RandomAccessFile;
import java.util.HashMap;

public interface Position_IFace{
	public static final String POSITION_FILEPATH = "position.dat";
	
		//This static function will open the exisiting position record file and return a hashmap with keys
		//that can be loaded into a combo box that unlock positionRec objects. Full objects are stored for this
		//function because shifts will need both the jobCode and payrate, employees would just need jobCodes
	public static HashMap<String, Integer> getJobCodeMap(char deptFilter) throws IOException{
		RandomAccessFile input=new RandomAccessFile(POSITION_FILEPATH, "r");
		HashMap<String, Integer> positions = new HashMap<>();

		try{
			Position position=new Position();

			while(true){
				position.read(input);
				if(position.getDept()!=deptFilter&&deptFilter!='E')
					continue;
				PositionRec positionRec=new PositionRec(position);

				positions.put(positionRec.toString(), position.getJobCode());
			}
		}catch(EOFException e){
			System.out.println("PositionRec data loaded");
			input.close();
		}
		
		return positions;
	}
	
	public static HashMap<String, PositionRec> getPositionMap(char deptFilter) throws IOException{
		RandomAccessFile input=new RandomAccessFile(POSITION_FILEPATH,"r");
		HashMap<String, PositionRec> positions=new HashMap<>();
		
		try{
			Position position=new Position();
			
			while(true){
				position.read(input);
				if(position.getDept()!=deptFilter&&deptFilter!='E')
					continue;
				PositionRec positionRec=new PositionRec(position);
				
				positions.put(positionRec.toString(), positionRec);
			}
		}catch(EOFException e){
			System.out.println("PositionRec data loaded");
			input.close();
		}
		
		return positions;
	}
	
	//This function is used to return a positionRec object from the positionrec file
	//This object can either be assigned to a variable or called to execute one of the
	//positionRec methods
	public static PositionRec getPositionRec(char dept, int jobCode)throws IOException{
		RandomAccessFile input=new RandomAccessFile(POSITION_FILEPATH, "r");
		Position position=new Position();

		try{
			if(dept == 'B')
				jobCode+=50;													//Had I started this sooner, the deptments would
																				//be in seperate files
			input.seek((jobCode-1)*position.size());
			position.read(input);
		}catch(EOFException er){
			System.err.println("Specified position could not be found "+er.toString());
		}finally{
			input.close();
		}
			
		PositionRec positionRec=new PositionRec(position);
		return positionRec;
	
	}
	
}