package com.example.project;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class startApp extends Application {

	private static Scene scene;
	public static Stage stageM = null;

	@Override
	public void start(Stage stage) throws IOException {
		stageM = stage;
		stage.setTitle("Connection page");
		scene = new Scene(loadFXML("/com/example/project/mainConnectionToServer"));
		stage.getIcons().add(new Image("/com/example/project/images/uni_pic.jpg"));
		stage.setScene(scene);
		stage.show();
	}

	static void setRoot(String fxml) throws IOException {
		scene.setRoot(loadFXML(fxml));
	}

	private static Parent loadFXML(String fxml) throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(startApp.class.getResource(fxml + ".fxml"));
		return fxmlLoader.load();
	}

	public static void main(String[] args) {
		launch();
	}
}
