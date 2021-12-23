/*
  Jeff Tibbetts
  08.16.2021
  Final Project - Java 285
  Original ReadEmployeeRecord, WriteEmployeeRecord, EmployeeRecord and EmpRec all provided
  by Prof Haley

*/
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.layout.StackPane;//place nodes on top on each other
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;		//
import javafx.scene.control.RadioButton;		//Added to select dept
import javafx.scene.control.ComboBox;

import javafx.scene.layout.FlowPane; //place horiz and vert nodes
import javafx.scene.layout.VBox;//place nodes in a single column
import javafx.scene.layout.HBox;//place nodes in a single row
import javafx.scene.layout.GridPane; //grid
import javafx.geometry.Pos;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.text.Font;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.collections.FXCollections;

import java.util.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.io.RandomAccessFile;
import java.io.*;

public class WriteShiftRec /*extends Application*/ implements Time_IFace {
    
    //define text fields
    private TextField tfShiftId, tfPayRate;
	private ComboBox<String> cboStartDate, cboStartTime, cboEndTime, cboJobCode, cboEmpId;
	private RadioButton radBOHOption, radFOHOption, radEventOption;
    private ToggleGroup deptOptions;
	private HBox deptBox;
	private Stage stage;
	
    private Button done, next;
    
    private RandomAccessFile output;
    private Shift shift;
	private HashMap<String, Integer> employees;
	private HashMap<String, PositionRec>positions;
	private HashMap<String, Date> startDates, startTimes, endTimes;


    //override start()  - to start the application
//    public void start(Stage stage){
	  public WriteShiftRec(Stage stage){

		shift=new Shift();
		
		startDates= new HashMap<String, Date>(Time_IFace.getComingDates(30));	//Create a map to coordinate the hireDate combobox with 67 dates
		startTimes=new HashMap<String, Date>();
		endTimes=new HashMap<String, Date>();	
		positions=new HashMap<String, PositionRec>();
		employees=new HashMap<String, Integer>();
        
		
        //open a file
        try{
            
            output = new RandomAccessFile("shiftRec.dat", "rw");
            
        }catch(IOException er){
            System.err.println("Could not opened a file: " + er.toString());
            System.exit(1);
        }
        
        
        //create a pane
        GridPane pane = new GridPane();
        pane.setAlignment(Pos.CENTER);
        
        pane.setHgap(8);//set horizontal gap
        pane.setVgap(5);//set vertical gap
        
        //set padding - inside offset
        pane.setPadding(new Insets(10, 10, 10, 10));
        
        //create form
        pane.add(new Label("Shift #: "), 0, 0); //column, row
        tfShiftId = new TextField();
		try{
		tfShiftId.setText(String.valueOf(output.length()/Shift.size() +1 ));//index assigned will be next open index on roster
		tfShiftId.setEditable(false);
		}catch(IOException er){
			System.err.println("An error occurred setting shift value "+er.toString());
		}
        pane.add(tfShiftId, 1, 0);
        
        pane.add(new Label("Start Date: "), 0, 1);
        cboStartDate=new ComboBox<String>();
		SimpleDateFormat dateFormat=new SimpleDateFormat(Time_IFace.DATE_TIME);
		cboStartDate.getItems().addAll(FXCollections.observableArrayList(startDates.keySet()).sorted(
		(String d1, String d2)->{
				 if(startDates.get(d1).before(startDates.get(d2))){return -1;}
				 else if(startDates.get(d1).after(startDates.get(d2))){return 1;}
				 else{ return 0; }

		} ));
		StartDateSelector handle4=new StartDateSelector();
		cboStartDate.setOnAction(handle4);
//		pane.add(cboStartDate, 1, 1);
		
//		pane.add(new Label("Start Time: "), 0, 2);
		cboStartTime=new ComboBox<String>();
		cboStartTime.getItems().addAll(FXCollections.observableArrayList(startTimes.keySet()).sorted(
		(String d1, String d2)->{
			if(startTimes.get(d1).before(startTimes.get(d2))){return -1;}
			else if(startTimes.get(d1).before(startTimes.get(d2))){return 1;}
		else{return 0;}}));
		
		StartTimeSelector handle5=new StartTimeSelector();
		cboStartTime.setOnAction(handle5);
//		pane.add(cboStartTime, 1, 2);
		pane.add(new HBox(5, cboStartDate, cboStartTime),1,1);
        pane.add(new Label("End Time: "), 0, 3);
		cboEndTime=new ComboBox<String>();
        pane.add(cboEndTime, 1, 3);
        
        pane.add(new Label("Dept: "), 0, 4);
		deptOptions = new ToggleGroup(); //create a group for radio buttons		
		radFOHOption = new RadioButton("FoH"); //Front of House - host/busser/server/bartender
		DeptSelector handler3=new DeptSelector();
		radFOHOption.setOnAction(handler3);
		radFOHOption.setToggleGroup(deptOptions);
		radBOHOption = new RadioButton("BoH");//Back of house - dish/prep/cook/chef
		radBOHOption.setOnAction(handler3);
		radBOHOption.setToggleGroup(deptOptions);
		radEventOption =new RadioButton("Event");
		radEventOption.setOnAction(handler3);
		radEventOption.setToggleGroup(deptOptions);
		deptBox = new HBox(20.0, radBOHOption, radFOHOption, radEventOption);
		pane.add(deptBox, 1, 4);//add radio buttons to the scene
        
        pane.add(new Label("Job Code: "), 0, 5);
        cboJobCode = new ComboBox();
		JobCodeSelector handle6=new JobCodeSelector();
		cboJobCode.setOnAction(handle6);
		cboJobCode.setPromptText("Select a dept to view jobCodes");
        pane.add(cboJobCode, 1, 5);
        
        pane.add(new Label("PayRate: "), 0, 6);
        tfPayRate=new TextField();
		tfPayRate.setEditable(false);
        pane.add(tfPayRate, 1, 6);
		
		pane.add(new Label("Employee: "), 0, 7);
        cboEmpId = new ComboBox();
        pane.add(cboEmpId, 1, 7);
        
        next = new Button("Next");
        next.setMaxWidth(Double.MAX_VALUE);
        NextButton handler1 = new NextButton();//create handler
        next.setOnAction(handler1); //register handler
        pane.add(next, 0, 8);
        GridPane.setHalignment(next, HPos.LEFT);
        
        done = new Button("Done");
        done.setMaxWidth(Double.MAX_VALUE);
        DoneButton handler2 = new DoneButton(); //create handler
        done.setOnAction(handler2);//register handler
        pane.add(done, 1, 8);
        GridPane.setHalignment(done, HPos.RIGHT);
        
        //create scene
        Scene scene = new Scene(pane);
		
		this.stage=stage;
        //set app title
        stage.setTitle("Shift Record - Write");
        stage.setWidth(450);
        stage.setHeight(400);
        
        //set scene
        stage.setScene(scene);
        
        //display the stage
        stage.show();
        
    }//start
	
    
    void addShiftRec(){
        
        //define temporary variables
        int shiftIdTemp = 0, jobCodeTemp=0, empIdTemp=0;
        char deptTemp;
		double payrateTemp = 0.0;
		String startDateTemp, endDateTemp, jobCodeStr;
		SimpleDateFormat date;
		
        if(!tfShiftId.getText().equals("")){
        
            try{
                shiftIdTemp = Integer.parseInt(tfShiftId.getText());
                if(shiftIdTemp >= 1){
                    shift.setShiftId(shiftIdTemp); //initialize a record
                }
                
            }catch(NumberFormatException nfe){
                
                System.err.println("Record number must be entered as an Integer");
            }
			
			shift.setStart(startTimes.get(cboStartTime.getValue()));
			shift.setEnd(endTimes.get(cboEndTime.getValue()));			
			
			if(deptOptions.getSelectedToggle()==radFOHOption){
				deptTemp = 'F';
			}
			else if(deptOptions.getSelectedToggle()==radBOHOption){
				deptTemp = 'B';
			}else{
				deptTemp = 'E';
			}
            
            shift.setDept(deptTemp);//initialize a job title
			jobCodeStr = cboJobCode.getValue();
			
			jobCodeTemp=positions.get(jobCodeStr).getJobCode();
			shift.setJobCode(jobCodeTemp);


            payrateTemp = positions.get(jobCodeStr).getPayrate();
            shift.setPayrate(payrateTemp);//initialize a number of dependents
                

            
			shift.setStart(startTimes.get(cboStartTime.getValue()));
			shift.setEnd(endTimes.get(cboEndTime.getValue()));
			
			shift.setEmpId(employees.get(cboEmpId.getValue()));
			
            
            //write information to a file
            try{
                output.seek((long)(shiftIdTemp-1) * Shift.size());//set pointer
                shift.write(output);
				
            }catch(IOException er){
                System.err.println("Error during write to a file: " + er.toString());
                System.exit(1);
            }
            
        }//if statement
        
        //clean text fields
        clearAllFields();
		
    
    }//addEmployeeRecord

	public void clearAllFields(){

		try{
			tfShiftId.setText(String.valueOf(output.length()/Shift.size() +1 ));//index assigned will be next open index on roster
			tfShiftId.setEditable(false);
		}catch(IOException er){
			System.err.println("An error occurred setting shift value "+er.toString());
				tfShiftId.setEditable(true);
		}
		cboEndTime.getItems().clear();
		radFOHOption.setSelected(false);
		radFOHOption.setSelected(false);
		radEventOption.setSelected(false);
		cboJobCode.getItems().clear();		
		tfPayRate.setText("");		
		cboEmpId.getItems().clear();
	}

	class StartDateSelector implements EventHandler<ActionEvent>, Time_IFace{
		public void handle(ActionEvent e){
			if(cboStartDate.getValue()!=""){				
			startTimes=new HashMap<>(Time_IFace.getStartTimes(startDates.get(cboStartDate.getValue())));
			cboEndTime.getItems().clear();	
	
			cboStartTime.getItems().addAll(FXCollections.observableArrayList(startTimes.keySet()).sorted(
			(String d1, String d2)->{
			if(startTimes.get(d1).before(startTimes.get(d2))){return -1;}
			else if(startTimes.get(d1).after(startTimes.get(d2))){return 1;}
			else{return 0; }
		}));
		}
		}
	}
	
	class StartTimeSelector implements EventHandler<ActionEvent>, Time_IFace {
		public void handle(ActionEvent e){
		cboEndTime.getItems().clear();
		if(cboStartTime.getValue()!=null)
		endTimes=new HashMap<>(Time_IFace.getEndTimes(startTimes.get(cboStartTime.getValue())));
					
		cboEndTime.getItems().addAll(FXCollections.observableArrayList(endTimes.keySet()).sorted(
				(String d1, String d2)->{
					if(endTimes.get(d1).before(endTimes.get(d2))){return -1;}
					else if(endTimes.get(d1).after(endTimes.get(d2))){return 1;}
					else {return 0; }
				}));
    }
}
	
	class DeptSelector implements EventHandler<ActionEvent>, Position_IFace{

		public void handle(ActionEvent e){
			employees.clear();
			positions.clear();
			cboJobCode.getItems().clear();
			
			char deptTemp=' ';
			if(deptOptions.getSelectedToggle()==radFOHOption)
				deptTemp = 'F';
			else if(deptOptions.getSelectedToggle()==radBOHOption)
				deptTemp = 'B';
			else 
				deptTemp = 'E';
			
			try{
				positions =new HashMap<String, PositionRec>(Position_IFace.getPositionMap(deptTemp));
			}catch(IOException er){
				System.err.println("An error was encountered accessing \""+Position_IFace.POSITION_FILEPATH+"\"");
			}
			
				cboJobCode.getItems().addAll
				(FXCollections.observableArrayList(positions.keySet()).sorted(
				(String p1, String p2)->{
					if(p1.charAt(0) == 'F'&&p2.charAt(0) == 'B'){
						return 1;
					}
					else if(p1.charAt(0) =='B' & p2.charAt(0) == 'F'){
						return -1;
					}
					else {
						return positions.get(p1).getJobCode()-positions.get(p2).getJobCode();
					}
				}));
		}
	}
	
	class JobCodeSelector implements EventHandler<ActionEvent>, Position_IFace, Employee_IFace{
		public void handle(ActionEvent e){
			if(deptOptions.getSelectedToggle()!=null){
					
					cboEmpId.getItems().clear();
				if(cboJobCode.getValue()!=""){
					String entry=cboJobCode.getValue();
					int jobCodeTemp=positions.get(entry).getJobCode();
					char deptTemp=positions.get(entry).getDept();
					tfPayRate.setText(String.valueOf(positions.get(entry).getPayrate()));
		
		
			try{
				employees=new HashMap<String, Integer>(Employee_IFace.getEmployeeOptions(deptTemp, jobCodeTemp));
			}catch(IOException er){
			System.err.println("An error occurred accessing employee file "+er.toString());
			}
				cboEmpId.getItems().addAll(FXCollections.observableArrayList(employees.keySet()).sorted(
			(String e1, String e2)->
				{return employees.get(e1)-employees.get(e2);}));
			}
			}
		}
	}
    
    //Inner classes  - useful for defining handler classes
    class NextButton implements EventHandler<ActionEvent>{
        
        public void handle(ActionEvent e){
            
            addShiftRec();
        }
    }//NextButton
    
    class DoneButton implements EventHandler<ActionEvent>{
        
        public void handle(ActionEvent e){
            try{
                
                output.close(); //close a file
                
            }catch(IOException er){
                
                System.err.println("File not closed properly: " + er.toString());
                System.exit(1);
            }
            
            stage.close();
        }
    }//DoneButton


   /* public static void main(String[] args){
        launch(args);
    }*/

}//WriteEmployeeRecord
