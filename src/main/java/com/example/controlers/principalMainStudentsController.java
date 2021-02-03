/**
 * Sample Skeleton for 'principalMainStudents.fxml' Controller Class
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
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

public class principalMainStudentsController implements Initializable {

	@FXML // fx:id="studentsList"
	private ListView<String> studentsList; // Value injected by FXMLLoader

	@FXML // fx:id="backBTN"
	private Button backBTN; // Value injected by FXMLLoader
	@FXML // fx:id="examsBTN"
	private Button examsBTN; // Value injected by FXMLLoader

	@FXML // fx:id="viewStBTN"
	private Button viewStBTN; // Value injected by FXMLLoader
	public static String backTo = "/com/example/project/principalMainStudents.fxml";

	@FXML
	void back(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource(backTo));
		Parent Main = loader.load();
		// todoListController secController = loader.getController();

		// secController.init(username, password, "Principal");
		Scene scene = new Scene(Main);
		Stage Window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		Window.setTitle("Main page");
		Window.setScene(scene);
		Window.show();
	}

	@FXML
	void showExams(ActionEvent event) throws IOException {
		if (!backTo.equals("/com/example/project/PrincipalMainPage.fxml")) {
			Instance.sendMessage(Command.studentChecked.ordinal() + "@" + principalStudentInfoContoller.stId);
		} else
			Instance.sendMessage(Command.getALLChecked.ordinal() + "");
		OpenCheckedExamController.isTeacher = false;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/project/CheckExam.fxml"));
		Parent Main = loader.load();
		CheckExamController secController = loader.getController();
		CheckExamController.isTeacher = false;
		CheckExamController.backto = principalMainStudentsController.backTo;
		secController.init("", "", true);
		Scene scene = new Scene(Main);
		Stage Window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		Window.setTitle("Exams list");
		Window.setScene(scene);
		Window.show();
	}

	@FXML
	void viewStudent(ActionEvent event) throws IOException {
		principalStudentInfoContoller.thisPageFXML = "/com/example/project/principalStudentInfo.fxml";
		if (!(studentsList.getSelectionModel().getSelectedIndex() >= 0))
			return;
		Instance.sendMessage(Command.getStudentByIDNUM.ordinal() + "@"
				+ studentsList.getSelectionModel().getSelectedItem().split("\n")[0].split(" ")[2]);
		String[] datas = Instance.getClientConsole().getMessage().toString().split("@");
		principalStudentInfoContoller.name = datas[0];
		principalStudentInfoContoller.stId = datas[1];
		principalStudentInfoContoller.isprinc = true;
		principalStudentInfoContoller.courses = new ObjectMapper().readValue(datas[3], ArrayList.class);
		principalStudentInfoContoller.exams = new ObjectMapper().readValue(datas[2],
				new TypeReference<List<checkedExam>>() {
				});
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/project/principalStudentInfo.fxml"));
		Parent Main = loader.load();

		System.out.println("dd: " + datas[2]);
		principalStudentInfoContoller secController = loader.getController();

		secController.setData();

		Scene scene = new Scene(Main);
		Stage Window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		Window.setTitle("Student Info");
		Window.setScene(scene);
		Window.show();

	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		try {
			init();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void init() throws IOException {
		Instance.sendMessage(Command.getAllStudents.ordinal() + "");
		String dataString = Instance.getClientConsole().getMessage().toString();
		System.out.println("dataaaaaaaaa: " + dataString);
		String[] data = dataString.split("@");
		List<String> l = new ArrayList<>();
		for (String s : data)
			l.add(s);

		// studentsList = new ListView<>();
		studentsList.getItems().removeAll(studentsList.getItems());
		studentsList.getItems().addAll(l);
	}

}
