package com.example.controlers;

import java.io.IOException;
import java.util.Random;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class StudentMotivationPageController {

	String[] quotes = { "Nothing is impossible. The word itself says ‘I’m Possible’",
			"Everybody is a genius. But if you judge a fish by its ability to climb a tree, it will spend its whole life believing that it is stupid.",
			"My advice is, never do tomorrow what you can do today. Procrastination is the thief of time.",
			"Success consists of going from failure to failure without loss of enthusiasm",
			"Don’t say you don’t have enough time. You have exactly the same amount of hours per day that were given to… Michelangelo, Mother Teresa, Leonardo Di Vinci… and Albert Einstein.",
			"Trust yourself, you know more than you think you do", "I haven’t started yet either…" };

	@FXML // fx:id="showMotivation"
	private TextArea showMotivation; // Value injected by FXMLLoader

	@FXML // fx:id="motivationShower"
	private Button motivationShower; // Value injected by FXMLLoader

	@FXML // fx:id="backBtn"
	private Button backBtn; // Value injected by FXMLLoader

	@FXML
	void goBack(ActionEvent event) throws IOException {

		FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/project/StudentMainPage.fxml"));
		Parent Main = loader.load();
		// teacherExamList secController = loader.getController();
		// secController.init(username, password);
		Scene scene = new Scene(Main);
		Stage Window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		// Window.setTitle("Daily Motivation");
		Window.setScene(scene);
		Window.show();

	}

	@FXML
	void showQuote(ActionEvent event) {

		String quote = getQuote();
		showMotivation.setText(quote);

	}

	String getQuote() {

		String quote = "";
		Random rand = new Random();
		int rand_int = rand.nextInt(7);
		quote = quotes[rand_int];
		return quote;
	}

}
