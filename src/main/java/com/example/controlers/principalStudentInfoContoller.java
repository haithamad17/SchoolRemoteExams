/**
 * Sample Skeleton for 'principalStudentInfo.fxml' Controller Class
 */

package com.example.controlers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.example.ServerClientEntities.Command;
import com.example.ServerClientEntities.Instance;
import com.example.entities.checkedExam;
import com.fasterxml.jackson.databind.ObjectMapper;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class principalStudentInfoContoller implements Initializable {

	@FXML // fx:id="courseFillt"
	private ListView<String> courseFillt; // Value injected by FXMLLoader

	@FXML // fx:id="studentID"
	private Text studentID; // Value injected by FXMLLoader

	@FXML // fx:id="studentInfo"
	private Text studentInfo; // Value injected by FXMLLoader

	@FXML // fx:id="studentGBA"
	private Text studentGBA; // Value injected by FXMLLoader

	@FXML // fx:id="courseGrade"
	private Text courseGrade; // Value injected by FXMLLoader

	@FXML // fx:id="examBTN"
	private Button examBTN; // Value injected by FXMLLoader

	@FXML // fx:id="back"
	private Button back; // Value injected by FXMLLoader
	static String stId = "";
	static String name = "";
	static String gbaGrade = "";
	static List<String> courses = new ArrayList<>();
	static List<checkedExam> exams = new ArrayList<>();
	static String backTo = "/com/example/project/principalMainStudents.fxml";
	static String thisPageFXML = "";
	static boolean isprinc = false;

	@FXML
	void back(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource(backTo));
		Parent Main = loader.load();
		Scene scene = new Scene(Main);
		Stage Window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		Window.setTitle("Students informations");
		Window.setScene(scene);
		Window.show();
	}

	@FXML
	void getExamInfo(ActionEvent event) {

	}

	@FXML
	void viewExam(ActionEvent event) throws IOException {
		Instance.sendMessage(Command.studentChecked.ordinal() + "@" + principalStudentInfoContoller.stId);
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/project/studentCheckedExams.fxml"));
		Parent Main = loader.load();
		studentCheckedExamsController secController = loader.getController();
		studentCheckedExamsController.backTo = principalStudentInfoContoller.thisPageFXML;
		secController.init(
				new ObjectMapper().readValue(Instance.getClientConsole().getMessage().toString(), ArrayList.class));
		Scene scene = new Scene(Main);
		Stage Window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		Window.setTitle("Exams list");
		Window.setScene(scene);
		Window.show();
	}

	public void setData() {
		studentInfo.setText(name);
		studentID.setText(stId);
		courseFillt.getItems().removeAll(courseFillt.getItems());
		courseFillt.getItems().addAll(courses);
		double avg = 0;

		for (checkedExam exam : exams) {
			avg += exam.getGrade();
		}
		if (exams.size() != 0) {
			studentGBA.setText(Double.toString(avg / exams.size()));
		}
	}

	public static void resetAll() {
		principalStudentInfoContoller.stId = "";
		principalStudentInfoContoller.name = "";
		principalStudentInfoContoller.gbaGrade = "";
		principalStudentInfoContoller.courses.clear();
		principalStudentInfoContoller.exams.clear();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		if (isprinc)
			examBTN.setVisible(false);
		setData();
	}
}
