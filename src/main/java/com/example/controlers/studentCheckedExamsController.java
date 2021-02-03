/**
 * Sample Skeleton for 'studentCheckedExams.fxml' Controller Class
 */

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

public class studentCheckedExamsController implements Initializable {
	static String backTo = "";
	@FXML // fx:id="backBTN"
	private Button backBTN; // Value injected by FXMLLoader

	@FXML // fx:id="openExamBtn"
	private Button openExamBtn; // Value injected by FXMLLoader

	@FXML // fx:id="ExamsList"
	private ListView<String> ExamsList; // Value injected by FXMLLoader
	static String thisFXML = "/com/example/project/studentCheckedExams.fxml";
	static List<String> exams = new ArrayList<>();

	@FXML
	void back(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource(backTo));
		Parent Main = loader.load();

		Scene scene = new Scene(Main);
		Stage Window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		Window.setTitle("Exams list");
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
		OpenCheckedExamController.isTeacher = false;
		OpenCheckedExamController.back_ = thisFXML;
		secController.initByExam(Instance.getClientConsole().getMessage().toString());
		Scene scene = new Scene(Main);
		Stage Window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		Window.setTitle("Student exam");
		Window.setScene(scene);
		Window.show();
	}

	public void init(List<String> examsDisc) {
		ExamsList.getItems().removeAll(ExamsList.getItems());
		exams = examsDisc;
		ExamsList.getItems().addAll(examsDisc);
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		ExamsList.getItems().addAll(exams);
	}
}
