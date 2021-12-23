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

public class WritePositionRecord /*extends Application*/{
    
    //define text fields
    private TextField tfTitle, tfJobCode, tfPayrate;
    private Button done, next;
	private RadioButton BOHOption, FOHOption;
    private ToggleGroup deptOptions;
	private HBox deptBox;
	private Stage stage;
	
    private RandomAccessFile output;
    private Position position;

    //override start()  - to start the application
//    public void start(Stage stage){
	  public WritePositionRecord(Stage stage){
        
        position=new Position();//create employee record object
        
        //open a file
        try{
            
            output = new RandomAccessFile("position.dat", "rw");
            
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
        pane.add(new Label("Title: "), 0, 0); //column, row
        tfTitle = new TextField();
        pane.add(tfTitle, 1, 0);
        
        pane.add(new Label("Dept: "), 0, 1);

		deptOptions = new ToggleGroup(); //create a group for radio buttons		
		FOHOption = new RadioButton("FoH"); //Front of House - host/busser/server/bartender
		FOHOption.setSelected(true);
		FOHOption.setToggleGroup(deptOptions);
		BOHOption = new RadioButton("BoH");//Back of house - dish/prep/cook/chef
		BOHOption.setToggleGroup(deptOptions);		
		deptBox = new HBox(20.0, BOHOption, FOHOption);
		pane.add(deptBox, 1, 1);//add radio buttons to the scene
        
        pane.add(new Label("JobCode: "), 0, 2);
        tfJobCode = new TextField();
        pane.add(tfJobCode, 1, 2);
        
        pane.add(new Label("Payrate: "), 0, 3);
        tfPayrate = new TextField();
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
		
		this.stage=stage;
        //set app title
        stage.setTitle("Add JobCode");
        stage.setWidth(300);
        stage.setHeight(300);
        
        //set scene
        stage.setScene(scene);
        
        //display the stage
        stage.show();
        
    }//start
    
    
    void addPositionRecord(){
        
        //define temporary variables
        char deptTemp='B';
		int jobCodeTemp=0;
		double payrateTemp=0.0;
    
        if(!tfJobCode.getText().equals("")){
        
            try{
                jobCodeTemp = Integer.parseInt(tfJobCode.getText());
				//Only 49 job codes may be entered for each dept
                if(jobCodeTemp >= 1 && jobCodeTemp<50 ){

                    position.setJobCode(jobCodeTemp); //initialize a new jobCode
                }
			}catch(NumberFormatException nfe){
                
                System.err.println("Jobcode number must be entered as an Integer");
            }
            
            position.setTitle(tfTitle.getText());//initialize a job title
            
			if(deptOptions.getSelectedToggle()==FOHOption){
						deptTemp = 'F';

			}
			else{
				deptTemp = 'B';
				jobCodeTemp +=50;					//this will create space for up to 49 records of FOH job codes before
													//the file switches to BOH job codes
			}
            
			position.setDept(deptTemp);
			
            try{
                payrateTemp = new Double(tfPayrate.getText());
                position.setPayrate(payrateTemp);
            
            }catch(NumberFormatException nfe){
                System.err.println("Number must be entered as a Double");
            }
            
            //write information to a file
            try{
                output.seek((long)(jobCodeTemp-1) * Position.size());//set pointer
                position.write(output);
                
				System.out.println(new PositionRec(position).toString());
            }catch(IOException er){
                System.err.println("Error during write to a file: " + er.toString());
                System.exit(1);
            }
            
        }//if statement
        
        //clean text fields
        tfTitle.setText("");
        tfJobCode.setText("");
        tfPayrate.setText("");
    
    }//addEmployeeRecord
    
    
    //Inner classes  - useful for defining handler classes
    class NextButton implements EventHandler<ActionEvent>{
        
        public void handle(ActionEvent e){
            
            addPositionRecord();
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


 /*   public static void main(String[] args){
        launch(args);
    }*/

}//WriteEmployeeRecord