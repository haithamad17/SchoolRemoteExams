/**
 * Sample Skeleton for 'onlineOnHandExamEnter.fxml' Controller Class
 */

package com.example.controlers;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class EnterExamNavigate {

	@FXML // fx:id="enterExamBtn"
	private Button enterExamBtn; // Value injected by FXMLLoader

	@FXML // fx:id="onhandExamBTN"
	private Button onhandExamBTN; // Value injected by FXMLLoader
	

    @FXML // fx:id="backBtn"
    private Button backBtn; // Value injected by FXMLLoader

	@FXML
	void enterExam(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/project/StudentStartExamPage.fxml"));
		Parent Main = loader.load();
		StudentStartExamPageController secController = loader.getController();

		secController.init(StudentPageController.username, StudentPageController.password);
		Scene scene = new Scene(Main);
		Stage Window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		Window.setTitle("Exam Log In");
		Window.setScene(scene);
		Window.show();
	}

	@FXML
	void enterOnHand(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/project/startOnHandExam.fxml"));
		Parent Main = loader.load();
		Scene scene = new Scene(Main);
		Stage Window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		Window.setTitle("Exam Log In hand");
		Window.setScene(scene);
		Window.show();
	}
	

    @FXML
    void goBack(ActionEvent event) throws IOException {
    	
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/project/StudentMainPage.fxml"));
		Parent Main = loader.load();
		Scene scene = new Scene(Main);
		Stage Window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		StudentPageController sesController = loader.getController();
		sesController.init(StudentPageController.username, StudentPageController.password);
		Window.setTitle("Main Page");
		Window.setScene(scene);
		Window.show();

    }

}
