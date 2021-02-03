package com.example.controlers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.example.ServerClientEntities.Command;
import com.example.ServerClientEntities.Instance;
import com.fasterxml.jackson.databind.ObjectMapper;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

public class TeacherShowStudentsExam {

	@FXML // fx:id="backBTN"
	private Button backBTN; // Value injected by FXMLLoader

	@FXML // fx:id="ExamsList"
	private ListView<String> ExamsList; // Value injected by FXMLLoader

	@FXML // fx:id="filterBtn"
	private ComboBox<String> filterBtn; // Value injected by FXMLLoader

	private static List<String> examsDiscription = new ArrayList<String>();

	@FXML
	void back(ActionEvent event) throws IOException {

		FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/project/CheckExam.fxml"));
		Scene scene = new Scene(loader.load());
		Stage Window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		CheckExamController ses = loader.getController();
		ses.init(CheckExamController.username, CheckExamController.password, false);
		Window.setTitle("Check Exams list");
		Window.setScene(scene);
		Window.show();

	}

	@FXML
	void showFilter(ActionEvent event) throws IOException {

		String selection = filterBtn.getSelectionModel().getSelectedItem();

		if (selection.equals("ALL")) {
			Instance.sendMessage(
					Command.thisTeacherStudentChecked.ordinal() + "@" + teacherMainPageController.username);
			List<String> res = new ObjectMapper().readValue(Instance.getClientConsole().getMessage().toString(),
					ArrayList.class);
			ExamsList.getItems().removeAll(ExamsList.getItems());
			ExamsList.getItems().addAll(res);
		} else {

			Instance.sendMessage(Command.TeacherExamsByCourse.ordinal() + "@" + teacherMainPageController.username + "@"
					+ selection);
			String json = Instance.getClientConsole().getMessage().toString();
			if (json.equals("")) {
				ExamsList.getItems().removeAll(ExamsList.getItems());
				return;
			}

			List<String> l = new ObjectMapper().readValue(json, ArrayList.class);
			ExamsList.getItems().removeAll(ExamsList.getItems());
			ExamsList.getItems().addAll(l);

		}
	}

	public void init(List<String> examsDisc) {
		this.examsDiscription.clear();
		this.examsDiscription = examsDisc;
		ExamsList.getItems().removeAll(ExamsList.getItems());
		ExamsList.getItems().addAll(examsDisc);
		Instance.getClientConsole().setMessage(null);
		try {
			Instance.getClientConsole().sendToServer("" + Command.teacherCourses.ordinal() + "@"
					+ teacherMainPageController.username + "@" + teacherMainPageController.password);
			while (Instance.getClientConsole().getMessage() == null) {
				System.out.println("waiting for server");
			}
			String json = Instance.getClientConsole().getMessage().toString();
			System.out.println("json: " + json);
			List<String> ll = new ObjectMapper().readValue(json, ArrayList.class);
			System.out.println("ll: " + ll);
			filterBtn.getItems().removeAll(filterBtn.getItems());
			ll.add(0, "ALL");
			filterBtn.getItems().addAll(ll);
			filterBtn.getSelectionModel().select(0);
			// loadAll();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
