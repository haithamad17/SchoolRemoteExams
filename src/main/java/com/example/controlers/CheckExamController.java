package com.example.controlers;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.example.ServerClientEntities.Command;
import com.example.ServerClientEntities.Instance;
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

public class CheckExamController implements Initializable {
	static boolean isTeacher = true;
	static String username = "";
	static String password = "";
	static boolean isPrincipal = false;
	@FXML // fx:id="backBTN"
	private Button backBTN; // Value injected by FXMLLoader

	@FXML // fx:id="openExamBtn"
	private Button openExamBtn; // Value injected by FXMLLoader

	@FXML // fx:id="myExamsBtn"
	private Button myExamsBtn; // Value injected by FXMLLoader

	@FXML // fx:id="ExamsList"
	private ListView<String> ExamsList; // Value injected by FXMLLoader
	@FXML // fx:id="regularExBTN"
	private Button regularExBTN; // Value injected by FXMLLoader
	static String backto = "/com/example/project/teacherExamsList.fxml";

	public void setvisibilty() {
		if (isPrincipal == true) {

			myExamsBtn.setVisible(false);
			regularExBTN.setVisible(false);

		}
	}

	@FXML
	void back(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource(backto));
		Parent Main = loader.load();
		if (isTeacher) {
			teacherExamList secController = loader.getController();

			secController.init(teacherExamList.useString, teacherExamList.passString);
		}

		Scene scene = new Scene(Main);
		Stage Window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		Window.setTitle("Exam list");
		Window.setScene(scene);
		Window.show();
	}

	@FXML
	void showMyExams(ActionEvent event) throws IOException {

		Instance.sendMessage(Command.thisTeacherStudentChecked.ordinal() + "@" + username);
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/project/TeacherShowStudentsExam.fxml"));
		Parent Main = loader.load();
		TeacherShowStudentsExam secController = loader.getController();

		secController.init(
				new ObjectMapper().readValue(Instance.getClientConsole().getMessage().toString(), ArrayList.class));
		Scene scene = new Scene(Main);
		Stage Window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		Window.setTitle("Finished Exams");
		Window.setScene(scene);
		Window.show();

	}

	@FXML
	void openExam(ActionEvent event) throws IOException, SQLException {

		if (!(ExamsList.getSelectionModel().getSelectedIndex() >= 0))
			return;
		Instance.sendMessage(Command.getCheckedExamById.ordinal() + "@"
				+ ExamsList.getSelectionModel().getSelectedItem().split("\n")[0].split(" ")[2]);

		FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/project/OpenCheckedExam.fxml"));
		Parent Main = loader.load();
		OpenCheckedExamController secController = loader.getController();
		if (!isTeacher)
			OpenCheckedExamController.isTeacher = false;
		secController.initByExam(Instance.getClientConsole().getMessage().toString());
		Scene scene = new Scene(Main);
		Stage Window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		Window.setTitle("Student exam");
		Window.setScene(scene);
		Window.show();

	}

	public void init(String usrName, String password, boolean isP) throws IOException {
		CheckExamController.username = usrName;
		CheckExamController.password = password;
		CheckExamController.isPrincipal = isP;
		//if (!(ExamsList.getSelectionModel().getSelectedIndex() >= 0))
			//return;
		setvisibilty();
		if (isTeacher)
			Instance.sendMessage(Command.getTeacherExamGenerated.ordinal() + "@" + usrName + "@" + password);
		else {

			Instance.sendMessage(Command.getALLChecked.ordinal() + "");
		}
		String json = Instance.getClientConsole().getMessage().toString();
		List<String> l = new ObjectMapper().readValue(json, ArrayList.class);
		ExamsList.getItems().addAll(l);
		System.out.println("Teacher usrName " + usrName);

	}

	@FXML
	void showRegular(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/project/teacherRegularExams.fxml"));
		Parent Main = loader.load();
		Scene scene = new Scene(Main);
		Stage Window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		Window.setTitle("Regular Exams");
		Window.setScene(scene);
		Window.show();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		if (isPrincipal == true) {

			myExamsBtn.setVisible(false);
			regularExBTN.setVisible(false);

		}

	}
}
