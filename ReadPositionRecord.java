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


public class ReadPositionRecord /*extends Application*/{
//define text fields
    private TextField tfTitle, tfJobCode, tfPayrate;
    private Button done, next;
	private RadioButton BOHOption, FOHOption;
    private ToggleGroup deptOptions;
	private HBox deptBox;
	private Stage stage;
	
    private RandomAccessFile input;
    private Position position;
    private PositionRec positionRec;
    
    //override start()  - to start the application
//    public void start(Stage stage){
	public ReadPositionRecord(Stage stage){
		this.stage=stage;
        position = new Position();//create employee record object

        //open a file
        try{
            
            input = new RandomAccessFile("position.dat", "r");
            
        }catch(IOException er){
            System.err.println("Could not opened a file: " + er.toString());
            System.exit(1);
        }
        
        position = new Position(); //initialize object
        
        GridPane pane = new GridPane();
        pane.setAlignment(Pos.CENTER);
        
        pane.setHgap(8);//set horizontal gap
        pane.setVgap(5);//set vertical gap
        
        //set padding - inside offset
        pane.setPadding(new Insets(10, 10, 10, 10));
        
        //create form
        pane.add(new Label("Title: "), 0, 0); //column, row
        tfTitle = new TextField();
		tfTitle.setEditable(false);
        pane.add(tfTitle, 1, 0);
        
        pane.add(new Label("Dept: "), 0, 1);

		deptOptions = new ToggleGroup(); //create a group for radio buttons		
		FOHOption = new RadioButton("FoH"); //Front of House - host/busser/server/bartender
		FOHOption.setToggleGroup(deptOptions);
		FOHOption.setDisable(true);

		BOHOption = new RadioButton("BoH");//Back of house - dish/prep/cook/chef
		BOHOption.setToggleGroup(deptOptions);		
		BOHOption.setDisable(true);	
		deptBox = new HBox(20.0, BOHOption, FOHOption);
		pane.add(deptBox, 1, 1);//add radio buttons to the scene
        
        pane.add(new Label("JobCode: "), 0, 2);
        tfJobCode = new TextField();
		tfJobCode.setEditable(false);
        pane.add(tfJobCode, 1, 2);
        
        pane.add(new Label("Payrate: "), 0, 3);
        tfPayrate = new TextField();
		tfPayrate.setEditable(false);
        pane.add(tfPayrate, 1, 3);
        
        next = new Button("Next");
        next.setMaxWidth(Double.MAX_VALUE);
        NextButton handler1 = new NextButton();//create handler
        next.setOnAction(handler1); //register handler
        pane.add(next, 0, 4);
        GridPane.setHalignment(next, HPos.LEFT);
        
        done = new Button("Done");
        done.setMaxWidth(Double.MAX_VALUE);
        DoneButton handler2 = new DoneButton(); //create handler
        done.setOnAction(handler2);//register handler
        pane.add(done, 1, 4);
        GridPane.setHalignment(done, HPos.RIGHT);
        
        //create scene
        Scene scene = new Scene(pane);
    
        //set app title
        stage.setTitle("JobCode - Read");
        stage.setWidth(300);
        stage.setHeight(300);
        
        //set scene
        stage.setScene(scene);
        
        //display the stage
        stage.show();
        
        
    }//start
    
    void readPositionRecord(){
        
        //define temporary variables
		String titleTemp="";
        char deptTemp;
		int jobCodeTemp=0;
		double payrateTemp=0.0;
        
        try{
            do{
                //read from a file
                position.read(input);
				
				titleTemp=position.getTitle();
				deptTemp=position.getDept();
				jobCodeTemp=position.getJobCode();
				payrateTemp=position.getPayrate();
                
				positionRec=new PositionRec(titleTemp, deptTemp, jobCodeTemp, payrateTemp);
        
            }while(position.getJobCode() == 0);//?????
            
            System.out.println(positionRec.toString());
            

            //write employee information to a form

            
        }catch(EOFException er){
            closeFile();
        
        }catch(IOException er){
            System.err.println( "Error during read from file: " + er.toString() );
            stage.close();
        }
        
    
    }//readEmployeeRecord
	
	void updateGUI(){
        DecimalFormat dollar = new DecimalFormat( "$0.00" );
            tfTitle.setText(position.getTitle());

			if(position.getDept() == 'F')
				FOHOption.setSelected(true);
			else
				BOHOption.setSelected(true);
			
            tfJobCode.setText(String.valueOf(position.getJobCode()));
            tfPayrate.setText(String.valueOf(dollar.format(position.getPayrate())));
	}
    
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
            
            readPositionRecord();
            updateGUI();
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

}//ReadEmployeeRecord
