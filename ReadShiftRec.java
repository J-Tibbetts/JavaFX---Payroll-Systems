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
import java.text.SimpleDateFormat;
import java.text.DecimalFormat;
import java.io.RandomAccessFile;
import java.io.*;

public class ReadShiftRec /*extends Application*/implements Shift_IFace{
    
    //define text fields
    private TextField tfShiftId, tfStart, tfEnd, tfJobCode, tfPayRate, tfEmpId, tfNetPay;
	private RadioButton BOHOption, FOHOption;
    private ToggleGroup deptOptions;
	private HBox deptBox;
	private Stage stage;
	
    private Button done, next;
    
    private RandomAccessFile input;
	private Shift shift;
    private ShiftRec shiftRec;

    //override start()  - to start the application
//    public void start(Stage stage){
	public ReadShiftRec(Stage stage){
		this.stage=stage;
        SimpleDateFormat date = new SimpleDateFormat("DD-MMM-YYYY");
		shift=new Shift();
        
        //open a file
        try{
            
            input = new RandomAccessFile("shiftRec.dat", "r");
            
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
		tfShiftId.setEditable(false);
        pane.add(tfShiftId, 1, 0);
        
        pane.add(new Label("Start Time: "), 0, 1);
        tfStart = new TextField();
		tfStart.setEditable(false);				//Need to change on edit!
        pane.add(tfStart, 1, 1);
        
        pane.add(new Label("End Time: "), 0, 2);
		tfEnd = new TextField();				
        tfEnd.setEditable(false);				//Need to change on edit
        pane.add(tfEnd, 1, 2);
        
        pane.add(new Label("Dept: "), 0, 3);
		deptOptions = new ToggleGroup(); //create a group for radio buttons		
		FOHOption = new RadioButton("FoH"); //Front of House - host/busser/server/bartender
		FOHOption.setToggleGroup(deptOptions);

		BOHOption = new RadioButton("BoH");//Back of house - dish/prep/cook/chef
		BOHOption.setToggleGroup(deptOptions);			
		deptBox = new HBox(20.0, BOHOption, FOHOption);
		pane.add(deptBox, 1, 3);//add radio buttons to the scene
        
        pane.add(new Label("Job Code: "), 0, 4);
        tfJobCode = new TextField();
		tfJobCode.setEditable(false);
        pane.add(tfJobCode, 1, 4);
        
        pane.add(new Label("PayRate: "), 0, 5);
        tfPayRate=new TextField();
		tfPayRate.setEditable(false);
        pane.add(tfPayRate, 1, 5);
		
		pane.add(new Label("Employee: "), 0, 6);
        tfEmpId = new TextField();
		tfEmpId.setEditable(false);
        pane.add(tfEmpId, 1, 6);
		
		pane.add(new Label("Net Pay: "), 0, 7);
		tfNetPay=new TextField();
		tfNetPay.setEditable(false);
		pane.add(tfNetPay, 1,7);
		
        
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
        stage.setTitle("Shift Record - Write");
        stage.setWidth(400);
        stage.setHeight(400);
        
        //set scene
        stage.setScene(scene);
        
        //display the stage
        stage.show();
        
    }//start
	
	    void readShiftRec(){
        
		
        //define temporary variables
		int shiftIdTemp = 0, jobCodeTemp=0, empIdTemp=0;
        char deptTemp;
		double payrateTemp = 0.0;
		Date startTemp=new Date(),
			 endTemp=new Date();

        try{
            do{
                //read from a file
                shift.read(input);
            
                shiftIdTemp = shift.getShiftId();
                startTemp = new Date(shift.getStart().getTime());
                endTemp = new Date(shift.getEnd().getTime());
				deptTemp = shift.getDept();
                jobCodeTemp = shift.getJobCode();
                payrateTemp = shift.getPayrate();
  				empIdTemp =shift.getEmpId();
                //public ShiftRec(int shiftId, char dept, int jobCode, double payrate, int empId, Date start, Date end)
                shiftRec = new ShiftRec(shiftIdTemp, deptTemp, jobCodeTemp, payrateTemp, empIdTemp, startTemp, endTemp);
            
        
            }while(shift.getShiftId() == 0);//?????
            
            System.out.println(shiftRec.toString());
            
            //get Date field
            SimpleDateFormat dateformat = new SimpleDateFormat("MM/dd/YYYY");
			DecimalFormat currency = new DecimalFormat("$0.00");
            
            //write employee information to a form
			tfShiftId.setText(String.valueOf(shiftIdTemp));
            tfStart.setText(String.valueOf(dateformat.format(startTemp)));
            tfEnd.setText(String.valueOf(dateformat.format(endTemp)));
            
			if(deptTemp == 'F')
				FOHOption.setSelected(true);
			else
				BOHOption.setSelected(true);			
			
            tfJobCode.setText(String.valueOf(jobCodeTemp));
            tfPayRate.setText(String.valueOf(currency.format(payrateTemp)));
			tfNetPay.setText(String.valueOf(currency.format(shiftRec.calcLabor())));
			

			
        }catch(EOFException er){
            closeFile();
        
        }catch(IOException er){
            System.err.println( "Error during read from file: " + er.toString() );
            System.exit( 1 );
        }
        
    
    }//readEmployeeRecord
    
    void closeFile(){
        try{
            
            input.close(); //close a file
            
        }catch(IOException er){
            
            System.err.println("File not closed properly: " + er.toString());
            System.exit(1);
        }
        
        stage.close();
    }
    
    //Inner classes  - useful for defining handler classes
    class NextButton implements EventHandler<ActionEvent>{
        
        public void handle(ActionEvent e){
            
            readShiftRec();
        }
    }//NextButton
    
    class DoneButton implements EventHandler<ActionEvent>{
        
        public void handle(ActionEvent e){
            
            closeFile();
        }
    }//DoneButton
    
//    public static void main(String[] args){
//        launch(args);
//    }

}