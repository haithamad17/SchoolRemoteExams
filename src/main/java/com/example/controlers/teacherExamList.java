/**
 * Sample Skeleton for 'teacherExamsList.fxml' Controller Class
 */

package com.example.controlers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

//import org.graalvm.compiler.lir.alloc.lsra.Interval.UsePosList;
import org.hibernate.Query;
import org.hibernate.Session;

import com.example.ServerClientEntities.Command;
import com.example.ServerClientEntities.Instance;
import com.example.entities.Course;
import com.example.entities.Exam;
import com.example.operations.ExamOps;
import com.example.project.dataBase;
import com.fasterxml.jackson.databind.ObjectMapper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

public class teacherExamList implements Initializable {
	static String useString = "";
	static String passString = "";
	ObservableList<String> list = FXCollections.observableArrayList();
	@FXML // fx:id="ExamsList"
	private ListView<String> ExamsList; // Value injected by FXMLLoader
	@FXML // fx:id="showExambtn"
	private Button showExambtn; // Value injected by FXMLLoader
	@FXML // fx:id="backBTN"
	private Button backBTN; // Value injected by FXMLLoader
	@FXML // fx:id="createExamBTN"
	private Button createExamBTN; // Value injected by FXMLLoader
	@FXML // fx:id="startExamBtn"
	private Button startExamBtn; // Value injected by FXMLLoader
	@FXML // fx:id="checkExamsbtn"
	private Button checkExamsbtn; // Value injected by FXMLLoader
	@FXML // fx:id="regularExBTN"
	private Button regularExBTN; // Value injected by FXMLLoader

	@FXML
	void startExam(ActionEvent event) throws IOException {

		if (!(ExamsList.getSelectionModel().getSelectedIndex() >= 0))
			return;
		Instance.sendMessage(Command.getExamCode.ordinal() + "@"
				+ ExamsList.getSelectionModel().getSelectedItem().split("\n")[0].split(" ")[2]);
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/project/examCodeGenerating.fxml"));
		Parent Main = loader.load();
		ExamCodeGenerateController secController = loader.getController();
		String course = ExamsList.getSelectionModel().getSelectedItem().split("\n")[3].split(" ")[1];
		secController.init(Instance.getClientConsole().getMessage().toString(), useString, passString,course);
		Scene scene = new Scene(Main);
		Stage Window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		Window.setTitle("Generate Code");
		Window.setScene(scene);
		Window.show();
	}

	@FXML
	void checkExams(ActionEvent event) throws IOException {
		//if (!(ExamsList.getSelectionModel().getSelectedIndex() >= 0))
			//return;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/project/CheckExam.fxml"));
		Parent Main = loader.load();
		CheckExamController secController = loader.getController();
		secController.init(useString, passString, false);
		Scene scene = new Scene(Main);
		Stage Window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		Window.setTitle("Check Exams list");
		Window.setScene(scene);
		Window.show();

	}

	@FXML
	void showRegular(ActionEvent event) {

	}

	@FXML
	void back(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/project/teacherMainPage.fxml"));
		Scene scene = new Scene(loader.load());
		Stage Window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		Window.setTitle("Exams list");
		Window.setScene(scene);
		Window.show();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub

	}

	public void init(String teacherName, String teacherPass) throws IOException {
		useString = teacherName;
		passString = teacherPass;
		Instance.sendMessage(Command.teacherExams.ordinal() + "@" + teacherName + "@" + teacherPass);
		String json = Instance.getClientConsole().getMessage().toString();
		List<String> l = new ObjectMapper().readValue(json, ArrayList.class);
		ExamsList.getItems().addAll(l);
	}

	@FXML
	void showExam(ActionEvent event) throws IOException {
		if (!(ExamsList.getSelectionModel().getSelectedIndex() >= 0))
			return;
		Instance.sendMessage(Command.getExamById.ordinal() + "@"
				+ ExamsList.getSelectionModel().getSelectedItem().split("\n")[0].split(" ")[2]);

		FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/project/examCreation.fxml"));
		Parent Main = loader.load();
		examCreateController secController = loader.getController();

		secController.initByExam(Instance.getClientConsole().getMessage().toString());
		Scene scene = new Scene(Main);
		Stage Window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		Window.setTitle("Create exam main page");
		Window.setScene(scene);
		Window.show();
	}

	@FXML
	void createExam(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/project/examCreation.fxml"));
		Parent Main = loader.load();
		examCreateController secController = loader.getController();

		secController.init(useString, passString);
		Scene scene = new Scene(Main);
		Stage Window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		Window.setTitle("Create exam main page");
		Window.setScene(scene);
		Window.show();
	}

}
