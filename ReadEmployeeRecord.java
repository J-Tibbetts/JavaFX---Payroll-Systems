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
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.RadioButton;
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

import java.io.RandomAccessFile;
import java.io.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReadEmployeeRecord /*extends Application*/ implements Employee_IFace{
//define text fields
    private TextField tfRecord, tfFirstName, tfLastName, tfPhone, tfEmail, tfHireDate, tfMaxPosition;
	private RadioButton BOHOption, FOHOption;
    private ToggleGroup deptOptions;
	private HBox deptBox;
    
    private Button done, next;
    
	private Stage stage;
    private RandomAccessFile input;
    private EmployeeRecord employeeRecord;
    private EmpRec emprec;
    
    //override start()  - to start the application
//    public void start(Stage primaryStage){
	  public ReadEmployeeRecord(Stage stage){
		this.stage=stage;
        employeeRecord = new EmployeeRecord();//create employee record object
        
        //open a file
        try{
            
            input = new RandomAccessFile("employeeRecord.dat", "r");
            
        }catch(IOException er){
            System.err.println("Could not opened a file: " + er.toString());
            //System.exit(1);
			stage.close();
        }
        
        employeeRecord = new EmployeeRecord(); //initialize object
        
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
		tfRecord.setEditable(false);
        pane.add(tfRecord, 1, 0);
        
        pane.add(new Label("First Name: "), 0, 1);
        tfFirstName = new TextField();
		tfFirstName.setEditable(false);
        pane.add(tfFirstName, 1, 1);
        
        pane.add(new Label("Last Name: "), 0, 2);
        tfLastName = new TextField();
		tfLastName.setEditable(false);
        pane.add(tfLastName, 1, 2);
        
        pane.add(new Label("Phone: "), 0, 3);
        tfPhone = new TextField();
		tfPhone.setEditable(false);
        pane.add(tfPhone, 1, 3);
        
        pane.add(new Label("Email: "), 0, 4);
        tfEmail = new TextField();
		tfEmail.setEditable(false);
        pane.add(tfEmail, 1, 4);
        
        pane.add(new Label("Dept: "), 0, 5);
		deptOptions = new ToggleGroup(); //create a group for radio buttons		
		FOHOption = new RadioButton("FoH"); //Front of House - host/busser/server/bartender
		FOHOption.setDisable(true);
		FOHOption.setToggleGroup(deptOptions);

		BOHOption = new RadioButton("BoH");//Back of house - dish/prep/cook/chef
		BOHOption.setDisable(true);
		BOHOption.setToggleGroup(deptOptions);			
		deptBox = new HBox(20.0, BOHOption, FOHOption);
		pane.add(deptBox, 1, 5);//add radio buttons to the scene
        
        pane.add(new Label("Max Position: "), 0, 6);
        tfMaxPosition = new TextField();
		tfMaxPosition.setEditable(false);
        pane.add(tfMaxPosition, 1, 6);
        
        pane.add(new Label("Hire Date: "), 0, 7);
        tfHireDate=new TextField();
		tfHireDate.setEditable(false);
        pane.add(tfHireDate, 1, 7);
        
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
        
        //set app title
        stage.setTitle("Employee Record - Read");
        stage.setWidth(400);
        stage.setHeight(600);
        
        //set scene
        stage.setScene(scene);
        
        //display the stage
        stage.show();
        
    }//start
    
    void readEmployeeRecord(){
        
		
        //define temporary variables
		int recordTemp = 0;
        int maxPositionTemp = 0;
        char deptTemp;
		Date hireDateTemp;
        String firstNameTemp, lastNameTemp, phoneTemp, emailTemp;
        
        try{
            do{
                //read from a file
                employeeRecord.read(input);
            
                recordTemp = employeeRecord.getRecord();
                firstNameTemp = employeeRecord.getFirstName();
                lastNameTemp = employeeRecord.getLastName();
                phoneTemp = employeeRecord.getPhone();
                emailTemp = employeeRecord.getEmail();
                deptTemp = employeeRecord.getDept();
                maxPositionTemp = employeeRecord.getMaxPosition();
                hireDateTemp = employeeRecord.getHireDate();
                
                emprec = new EmpRec(recordTemp,firstNameTemp,lastNameTemp,phoneTemp,emailTemp,deptTemp,maxPositionTemp,hireDateTemp);
            
        
            }while(employeeRecord.getRecord() == 0);//?????
            
            System.out.println(emprec.toString());
            
            //get Date field
            SimpleDateFormat dateformat = new SimpleDateFormat("MM/dd/YYYY, hh:mm");
            
            //write employee information to a form
            tfRecord.setText(String.valueOf(recordTemp));
            tfFirstName.setText(firstNameTemp);
            tfLastName.setText(lastNameTemp);
            tfPhone.setText(phoneTemp);
            tfEmail.setText(emailTemp);
			
			if(deptTemp == 'F')
				FOHOption.setSelected(true);
			else
				BOHOption.setSelected(true);
			
            tfMaxPosition.setText(String.valueOf(maxPositionTemp));
            tfHireDate.setText(dateformat.format(hireDateTemp));
			
        }catch(EOFException er){
            closeFile();
        
        }catch(IOException er){
            System.err.println( "Error during read from file: " + er.toString() );
            stage.close();
        }
        
    
    }//readEmployeeRecord
    
    void closeFile(){
        try{
            
            input.close(); //close a file
            
        }catch(IOException er){
            
            System.err.println("File not closed properly: " + er.toString());
            stage.close();
        }
        
        stage.close();
    }
    
    //Inner classes  - useful for defining handler classes
    class NextButton implements EventHandler<ActionEvent>{
        
        public void handle(ActionEvent e){
            
            readEmployeeRecord();
        }
    }//NextButton
    
    class DoneButton implements EventHandler<ActionEvent>{
        
        public void handle(ActionEvent e){
            
            closeFile();
        }
    }//DoneButton
    
//    public static void main(String[] args){
//        launch(args);
//   }

}//ReadEmployeeRecord
