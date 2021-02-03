/**
 * Sample Skeleton for 'teacherRegularExams.fxml' Controller Class
 */

package com.example.controlers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;

public class teacherRegularExams implements Initializable {

	@FXML // fx:id="backBTN"
	private Button backBTN; // Value injected by FXMLLoader

	@FXML // fx:id="openExamBtn"
	private Button openExamBtn; // Value injected by FXMLLoader

	@FXML // fx:id="ExamsList"
	private ListView<String> ExamsList; // Value injected by FXMLLoader

	@FXML
	void back(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/project/CheckExam.fxml"));
		Parent Main = loader.load();
		CheckExamController secController = loader.getController();
		secController.init(teacherMainPageController.username, teacherMainPageController.password, false);
		Scene scene = new Scene(Main);
		Stage Window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		Window.setTitle("Check Exams list");
		Window.setScene(scene);
		Window.show();

	}

	@FXML
	void openExam(ActionEvent event) throws IOException {
		if (!(ExamsList.getSelectionModel().getSelectedIndex() >= 0))
			return;
		Instance.sendMessage(Command.getHanedByID.ordinal() + "@"
				+ ExamsList.getSelectionModel().getSelectedItem().split("\n")[0].split(" ")[2]);

		FileChooser fc = new FileChooser();
		fc.setTitle("Download file");
		fc.setInitialFileName("student Exam");// description:"Word file",_extensions:"*.doc"
		fc.getExtensionFilters().addAll(new ExtensionFilter("Word file", "*.doc"));
		File file = fc.showSaveDialog(null);
		PrintWriter p = new PrintWriter(file);
		List<String> list = new ObjectMapper().readValue(Instance.getClientConsole().getMessage().toString(),
				ArrayList.class);
		String lines = "";
		for (String s : list)
			lines += s + "\n";
		p.write(lines);
		p.close();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		try {
			Instance.sendMessage(Command.getHandedExams.ordinal() + "@" + teacherMainPageController.username + "@"
					+ teacherMainPageController.password);
			List<String> l = new ObjectMapper().readValue(Instance.getClientConsole().getMessage().toString(),
					ArrayList.class);
			ExamsList.getItems().addAll(l);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
