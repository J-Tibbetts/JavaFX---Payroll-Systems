/*
  Jeff Tibbetts
  08.16.2021
  Final Project - Java 285
  Original ReadEmployeeRecord, WriteEmployeeRecord, EmployeeRecord and EmpRec all provided
  by Prof Haley
  -Its been a great semester!

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
import javafx.scene.control.ComboBoxBase;
import javafx.scene.control.ComboBox;

import javafx.collections.FXCollections;

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

import java.util.Date;
import java.util.HashMap;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.io.RandomAccessFile;
import java.io.*;
import java.lang.String;
import java.text.ParseException;

public class WriteEmployeeRecord /*extends Application*/ implements Time_IFace {
    //define text fields
    private TextField tfRecord, tfFirstName, tfLastName, tfPhone, tfEmail;
	private ComboBox<String> cboMaxPosition, cboHireDate;
	private RadioButton radBOHOption, radFOHOption;
    private ToggleGroup deptOptions;
	private HBox deptBox;
	private Stage stage;
	
    private Button done, next;
    
    private RandomAccessFile output;
    private EmployeeRecord employeeRecord;
	private HashMap<String, Integer> positions;
	private HashMap<String, Date> hireDateOptions;
	private SimpleDateFormat date;

    //override start()  - to start the application
	  public WriteEmployeeRecord(Stage stage){
//    public void start(Stage stage){
        date = new SimpleDateFormat(Time_IFace.CAL_DATE);
        employeeRecord = new EmployeeRecord();//create employee record object
		positions = new HashMap<String, Integer>();							//Initialize empty map to be populated with position codes
																			//Once the dept is specified

		hireDateOptions= new HashMap<String, Date>(Time_IFace.getPriorDates(60));	//Create a map to coordinate the hireDate combobox with 67 dates
	
        //open a file
        try{
            
            output = new RandomAccessFile("employeeRecord.dat", "rw");
            
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
        pane.add(new Label("Record #: "), 0, 0); //column, row
        tfRecord = new TextField();			
		try{
		tfRecord.setText(String.valueOf((output.length()/EmployeeRecord.size()) + 1));	//default record # will be the index 
		}catch(IOException er){															//after the highest index recorded
			System.err.println("Error occured calculating record #");
		}											
        pane.add(tfRecord, 1, 0);
        
        pane.add(new Label("First Name: "), 0, 1);
        tfFirstName = new TextField();
        pane.add(tfFirstName, 1, 1);
        
        pane.add(new Label("Last Name: "), 0, 2);
        tfLastName = new TextField();
        pane.add(tfLastName, 1, 2);
        
        pane.add(new Label("Phone: "), 0, 3);
        tfPhone = new TextField();
        pane.add(tfPhone, 1, 3);
        
        pane.add(new Label("Email: "), 0, 4);
        tfEmail = new TextField();
        pane.add(tfEmail, 1, 4);
        
        pane.add(new Label("Dept: "), 0, 5);
		deptOptions = new ToggleGroup(); //create a group for radio buttons		
		radFOHOption = new RadioButton("FoH"); //Front of House - host/busser/server/bartender
		PositionSelector handler3=new PositionSelector();
		radFOHOption.setOnAction(handler3);
		radFOHOption.setToggleGroup(deptOptions);
		radBOHOption = new RadioButton("BoH");//Back of house - dish/prep/cook/chef
		radBOHOption.setOnAction(handler3);
		radBOHOption.setToggleGroup(deptOptions);	

		deptBox = new HBox(20.0, radBOHOption, radFOHOption);
		pane.add(deptBox, 1, 5);//add radio buttons to the scene
        
        pane.add(new Label("Max Position: "), 0, 6);
        cboMaxPosition=new ComboBox<String>();
		cboMaxPosition.setPromptText("Select a Dept To View Positions");
        pane.add(cboMaxPosition, 1, 6);
        
        pane.add(new Label("Hire Date: "), 0, 7);
        cboHireDate=new ComboBox<String>();

		cboHireDate.getItems().addAll(FXCollections.observableArrayList(hireDateOptions.keySet()).sorted(
			(String d1, String d2)-> { if(hireDateOptions.get(d1).before(hireDateOptions.get(d2))){ return 1; }
										else if (hireDateOptions.get(d1).after(hireDateOptions.get(d2))){ return -1;}
										else{return 0; }
										}));

		cboHireDate.setPromptText(date.format(new Date()));

        pane.add(cboHireDate, 1, 7);
        
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
        stage.setTitle("Employee Record - Write");
        stage.setWidth(400);
        stage.setHeight(400);
        
        //set scene
        stage.setScene(scene);
        
        //display the stage
        stage.show();
        
    }//start
    
    
    void addEmployeeRecord(){
        
        //define temporary variables
        int recordTemp = 0;
        String maxPositionTemp, hireDateTemp;
        char deptTemp;
    
        if(!tfRecord.getText().equals("")||cboMaxPosition.getValue()!=null){
        
            try{
                recordTemp = Integer.parseInt(tfRecord.getText());
                if(recordTemp >= 1){
                    employeeRecord.setRecord(recordTemp); //initialize a record
                }
                
            }catch(NumberFormatException nfe){
                
                System.err.println("Record number must be entered as an Integer");
            }
            
            employeeRecord.setFirstName(tfFirstName.getText());//initialize a job title
            
            employeeRecord.setLastName(tfLastName.getText());//initialize a name
            
			employeeRecord.setPhone(tfPhone.getText());
			
			employeeRecord.setEmail(tfEmail.getText());
			
			if(deptOptions.getSelectedToggle()==radFOHOption){
				deptTemp = 'F';
			}
			else{
				deptTemp = 'B';
			}
			
			employeeRecord.setDept(deptTemp);
           
			
			maxPositionTemp=cboMaxPosition.getValue();
            employeeRecord.setMaxPosition(positions.get(maxPositionTemp));//Use selected combobox string to retrieve associated
																		  //value in map

            
			hireDateTemp=cboHireDate.getValue();
			employeeRecord.setHireDate(hireDateOptions.get(hireDateTemp));

            
            //write information to a file
            try{
                output.seek((long)(recordTemp-1) * EmployeeRecord.size());//set pointer
                employeeRecord.write(output);
				System.out.println(new EmpRec(employeeRecord).toString());
                
            }catch(IOException er){
                System.err.println("Error during write to a file: " + er.toString());
                System.exit(1);
            }
            
        }//if statement
        
        //clean text fields
        		try{
		tfRecord.setText(String.valueOf((output.length()/EmployeeRecord.size()) + 1));	//default record # will be the index 
				}catch(IOException er){															//after the highest index recorded
			System.err.println("Error occured calculating record #");
		}
        tfFirstName.setText("");
		tfFirstName.requestFocus();
        tfLastName.setText("");
        tfPhone.setText("");
        tfEmail.setText("");
		radBOHOption.setSelected(false);
		radFOHOption.setSelected(false);
		cboMaxPosition.getItems().clear();
        cboMaxPosition.setPromptText("Select a Dept To View Positions");
    
    }//addEmployeeRecord
	
	class PositionSelector implements EventHandler<ActionEvent>, Position_IFace{
		
		public void handle(ActionEvent e){
			positions.clear();
			cboMaxPosition.getItems().clear();
			
			char deptTemp=' ';
			if(deptOptions.getSelectedToggle()==radFOHOption)
				deptTemp = 'F';
			else if(deptOptions.getSelectedToggle()==radBOHOption)
				deptTemp = 'B';
			
			try{
				positions =Position_IFace.getJobCodeMap(deptTemp);
			}catch(IOException er){
				System.err.println("An error was encountered accessing \""+Position_IFace.POSITION_FILEPATH+"\"");
			}
			
				cboMaxPosition.getItems().addAll
				(FXCollections.observableArrayList(positions.keySet()).sorted(
				(String p1, String p2)->{
					if(p1.charAt(0) == 'F'&&p2.charAt(0) == 'B'){
						return 1;
					}
					else if(p1.charAt(0) =='B' & p2.charAt(0) == 'F'){
						return -1;
					}
					else {
						return Integer.parseInt(p1.substring(1,3).trim())-Integer.parseInt(p2.substring(1,3).trim());
					}
				}));
		}
	}
	
	
    //Inner classes  - useful for defining handler classes
    class NextButton implements EventHandler<ActionEvent>{
        
        public void handle(ActionEvent e){
            
            addEmployeeRecord();
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

/*
    public static void main(String[] args){
        launch(args);
    }
*/
}//WriteEmployeeRecord