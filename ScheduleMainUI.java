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
import javafx.scene.control.ComboBox;
import javafx.scene.control.ComboBoxBase;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.Priority;


import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane; //place horiz and vert nodes
import javafx.scene.layout.VBox;//place nodes in a single column
import javafx.scene.layout.HBox;//place nodes in a single row
import javafx.scene.layout.GridPane; //grid
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;

import javafx.geometry.Pos;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.text.Font;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.io.IOException;

/*
	This and reports are the only non-templet classes of this oversized project
	This class provides access to all other classes within the folder via the top
	menu bar. It's own stage allows a user to select a reporting period and will display
	the shifts which occur within the time period and summerize the labor data
*/
public class ScheduleMainUI extends Application implements Time_IFace, Shift_IFace, Employee_IFace, Position_IFace{
	private Stage primaryStage, secondaryStage;

	private ComboBox<String> cboStart, cboEnd;
	private Label lblViewingDate;
	private VBox laborReportBox, employeeRosterBox;
	private Button btnSelect;
	
	private Report report;
	private HashMap<String, Date> startDates, endDates;
	public static void main(String[] args){
		launch(args);
	}
	
	public void start(Stage primaryStage){
		this.primaryStage=primaryStage;
		secondaryStage=new Stage();
		startDates=new HashMap<String, Date>(Time_IFace.getDates(30));
		endDates=new HashMap<String, Date>(Time_IFace.getDates(30));
		report=new Report(Time_IFace.getOpenTime(new Date()), Time_IFace.getCloseTime(new Date()));

		BorderPane root=new BorderPane();
		
		GridPane pane=new GridPane();
		pane.setAlignment(Pos.CENTER);
		
		pane.setHgap(20);
		pane.setVgap(10);
		pane.setPadding(new Insets(10,10,10,10));
		
		
		RowConstraints row1=new RowConstraints();
		row1.setVgrow(Priority.NEVER);
		RowConstraints row2=new RowConstraints();
		row2.setVgrow(Priority.ALWAYS);
		
		ColumnConstraints column =new ColumnConstraints();
		column.setPercentWidth(47);
		
		pane.getColumnConstraints().addAll(column, column);
		pane.getRowConstraints().addAll(row1,row1,row1,row1,row1,row1,row2);
		
		pane.add(new Label("Report Period Start: "), 0, 0);
		cboStart=new ComboBox<String>();
		cboStart.getItems().addAll(FXCollections.observableArrayList(startDates.keySet()).sorted(
			(String d1, String d2)->{
			if(startDates.get(d1).before(startDates.get(d2))){return -1;}
			else if(startDates.get(d1).after(startDates.get(d2))){return 1;}
			else{return 0; }
			}));
		ReportStartBox handle1=new ReportStartBox();
		cboStart.setOnAction(handle1);
		pane.add(cboStart, 0, 1);
		
		pane.add(new Label("Report Period End: "), 0, 2);
		cboEnd=new ComboBox<String>();
		pane.add(cboEnd, 0, 3);
		
		btnSelect=new Button();
		btnSelect.setText("Report");
		SelectButton handle2=new SelectButton();
		btnSelect.setOnAction(handle2);
		pane.add(btnSelect, 0, 5);
		
		pane.add(new Label("Labor Report: "), 0, 6);
		laborReportBox=new VBox(5);
		pane.add(laborReportBox, 0, 7);
		
		
		lblViewingDate=new Label("Currently viewing: ");
		lblViewingDate.setBackground(new Background(new BackgroundFill(Color.web("#CE1126"), null, null)));
		pane.add(lblViewingDate, 1,0);
		pane.add(new Label("Employee Roster: "), 1, 2);
	
		employeeRosterBox=new VBox(10);
		
		pane.add(employeeRosterBox, 1, 3, 1, 5);
		
		
		root.setTop(getMenus());
		root.setCenter(pane);
		
		updateGUI();
		Scene scene=new Scene(root);
		primaryStage.setTitle("Scheduler Pro");
		primaryStage.setWidth(800);
		primaryStage.setHeight(600);
		primaryStage.setScene(scene);
		primaryStage.show();


	}
	
	
	public MenuBar getMenus(){

		MenuBar menuBar = new MenuBar();

		
		Menu shiftMenu=new Menu("Shifts");
		MenuItem addShift=new MenuItem("Add Shift");
		addShift.setOnAction(e->new WriteShiftRec(secondaryStage));
		MenuItem viewShifts=new MenuItem("View Shifts");
		viewShifts.setOnAction(e->new ReadShiftRec(secondaryStage));
		shiftMenu.getItems().addAll(addShift, viewShifts);
		
		Menu employeeMenu=new Menu("Employees");
		MenuItem addEmployee=new MenuItem("Add Employee Record");
		addEmployee.setOnAction(e->new WriteEmployeeRecord(secondaryStage));
		MenuItem viewEmployees=new MenuItem("View Employees Records");
		viewEmployees.setOnAction(e->new ReadEmployeeRecord(secondaryStage));
		employeeMenu.getItems().addAll(addEmployee, viewEmployees);
		
		Menu positionMenu=new Menu("Positions");
		MenuItem addJobCodes=new MenuItem("Add Position Record");
		addJobCodes.setOnAction(e->new WritePositionRecord(secondaryStage));
		MenuItem viewJobCodes=new MenuItem("View Position Record");
		viewJobCodes.setOnAction(e->new ReadPositionRecord(secondaryStage));
		positionMenu.getItems().addAll(addJobCodes, viewJobCodes);
		
		menuBar.getMenus().addAll(shiftMenu, employeeMenu, positionMenu);
		
		return menuBar;
	}
	
	public void updateGUI(){
		SimpleDateFormat dateFormat=new SimpleDateFormat(Time_IFace.CAL_DATE);
		lblViewingDate.setText("Currently viewing "+dateFormat.format(report.getPeriodStart())+" to "+dateFormat.format(report.getPeriodEnd()));
		employeeRosterBox.getChildren().clear();
		
		try{
			Background employeeRosterBackground=new Background(new BackgroundFill(Color.web("#FFFFFF"),null,null));
		for(String str:report.getEmployeeRoster()){
			Label label=new Label(str);
			label.setBackground(employeeRosterBackground);
			employeeRosterBox.getChildren().add(label);
		}
		}catch(IOException er){
			System.err.println("Error occurred accessing shift records "+er.toString());
		}
		
		laborReportBox.getChildren().clear();
		try{
		Background laborReportBackground=new Background(new BackgroundFill(Color.web("#006847"), null, null));
		for(String str:report.getLaborReport()){
			Label label=new Label(str);
			label.setBackground(laborReportBackground);
			laborReportBox.getChildren().add(label);
		}
		}catch(IOException er){
			System.err.println("Error occurred accessing shift records "+er.toString());
		}	
		cboStart.setPromptText(dateFormat.format(report.getPeriodStart()));
		cboEnd.setPromptText(dateFormat.format(report.getPeriodEnd()));
	}

	class SelectButton implements EventHandler<ActionEvent>{
		public void handle(ActionEvent e){
			Date startTemp=startDates.get(cboStart.getValue());
			Date endTemp=endDates.get(cboEnd.getValue());
				report=new Report(startTemp, endTemp);

			updateGUI();
		}
	}
	class ReportStartBox implements EventHandler<ActionEvent>{
		public void handle(ActionEvent e){
			cboEnd.getItems().clear();
			endDates=new HashMap<String, Date>(Time_IFace.getComingDates(30, startDates.get(cboStart.getValue())));
			cboEnd.getItems().addAll(FXCollections.observableArrayList(endDates.keySet()).sorted(
			(String d1, String d2)->{
			if(endDates.get(d1).before(endDates.get(d2))){return -1;}
			else if(endDates.get(d1).after(endDates.get(d2))){return 1;}
			else{return 0; }
			}));
		}
	}
}