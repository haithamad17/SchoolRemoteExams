/**
 * Sample Skeleton for 'startOnHandExam.fxml' Controller Class
 */

package com.example.controlers;

import java.io.File;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.example.ServerClientEntities.Command;
import com.example.ServerClientEntities.Instance;
import com.example.operations.generalOps;
import com.example.project.startApp;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;

public class startOnHandExam {
	static int mintsExam = 10;
	static int secondsExam = 60;
	@FXML // fx:id="idNum"
	private TextField idNum; // Value injected by FXMLLoader

	@FXML // fx:id="examCode"
	private TextField examCode; // Value injected by FXMLLoader

	@FXML // fx:id="submitExambtn"
	private Button submitExambtn; // Value injected by FXMLLoader
	@FXML // fx:id="enterExamBtn"
	private Button enterExamBtn; // Value injected by FXMLLoader

	@FXML // fx:id="backBtn"
	private Button backBtn; // Value injected by FXMLLoader

	@FXML // fx:id="minuts"
	private TextField minuts; // Value injected by FXMLLoader

	@FXML // fx:id="seconds"
	private TextField seconds; // Value injected by FXMLLoader
	@FXML // fx:id="errorTxt"
	private Text errorTxt; // Value injected by FXMLLoader
	private String exCode;
	static String duration = "";
	static String course = "";
	static String teacherId = "";
	static boolean firstTime = true;
	static int lastAddition = 0;
	private Thread t = null;
	static boolean done = false;
	static int checks = 0;

	@FXML
	void enterExam(ActionEvent event) throws IOException {
		if (!validations())
			return;
		exCode = examCode.getText();
		Instance.sendMessage(Command.getExamIdBycode.ordinal() + "@" + examCode.getText() + "@onhand");
		String respone = Instance.getClientConsole().getMessage().toString();
		if (respone.equals("exam not available")) {
			errorTxt.setText("exam not available");
			return;
		}
		Instance.sendMessage(
				Command.getExamById.ordinal() + "@" + Instance.getClientConsole().getMessage().toString() + "@onhand");
		String[] args = Instance.getClientConsole().getMessage().toString().split("@");
		duration = args[3];
		mintsExam = Integer.parseInt(duration) - 1;
		course = args[5];
		teacherId = args[12];
		firstTime = true;
		lastAddition = 0;
		done = false;
		FileChooser fc = new FileChooser();
		fc.setTitle("Download file");
		fc.setInitialFileName("myExam");// description:"Word file",_extensions:"*.doc"
		fc.getExtensionFilters().addAll(new ExtensionFilter("Word file", "*.doc"));
		File file = fc.showSaveDialog(null);
		if (file == null)
			return;
		PrintWriter p = new PrintWriter(file);
		p.write(args[0]);
		p.close();
		submitExambtn.setVisible(true);
		backBtn.setVisible(false);
		examCode.setEditable(false);
		examTimer myTimer = new examTimer();
		this.t = new Thread(myTimer);
		t.start();

	}

	public boolean validations() throws IOException {
		if (examCode.getText().toString().length() != 4) {
			errorTxt.setText("Exam code should be 4 digits!");
			return false;
		}
		if (!Instance.containCH(examCode.getText())) {
			errorTxt.setText("Exam code should only contain digits");
			return false;
		}
		Instance.sendMessage(Command.isStudentExistById.ordinal() + "@" + idNum.getText() + "@"
				+ StudentPageController.username + "@" + StudentPageController.password + "@" + examCode.getText());
		if (Instance.getClientConsole().getMessage().toString().equals("doneIt")) {
			errorTxt.setText("You've already submited you're exam!");
			return false;
		}
		if (!(Instance.getClientConsole().getMessage().toString().equals("exist"))) {

			errorTxt.setText("Invalid ID or exam code");
			return false;
		}
		errorTxt.setText("");
		return true;
	}

	@FXML
	void submit(ActionEvent event) throws IOException, InterruptedException {

		FileChooser fc = new FileChooser();
		fc.setTitle("Download file");
		fc.setInitialFileName("myExam");// description:"Word file",_extensions:"*.doc"
		fc.getExtensionFilters().addAll(new ExtensionFilter("Word file", "*.doc"));
		File file = fc.showOpenDialog(null);
		if (file == null)
			return;
		String examDis = "Student ID: " + idNum.getText() + "\n" + "Duration: " + duration + "\n" + "Exam in: "
				+ course;
		System.out.println(examDis);
		List<String> exLines = new ArrayList<String>();
		try (Scanner scanner = new Scanner(file)) {
			String lines = "";
			while (scanner.hasNextLine()) {
				exLines.add(scanner.nextLine());
				// System.out.println("here ");
				// lines += scanner.nextLine() + "\n";
			}
			List<String> exDis = new ArrayList<>();
			exDis.add(idNum.getText());
			exDis.add(duration);
			exDis.add(course);
			Instance.sendMessage(
					Command.submitHanedExam.ordinal() + "@" + teacherId + "@" + generalOps.getJsonString(exDis) + "@"
							+ generalOps.getJsonString(exLines) + "@" + idNum.getText() + "@" + examCode.getText());
			done = true;
			// goBack(event);

		}
	}

	@FXML
	void goBack(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/project/StudentMainPage.fxml"));
		Scene scene = new Scene(loader.load());
		Stage Window = startApp.stageM;

		Window.setTitle("Main page");
		Window.setScene(scene);
		Window.show();
	}

	public void back() {
		secondsExam = 60;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/project/StudentMainPage.fxml"));
		Scene scene;
		try {
			scene = new Scene(loader.load());
			Stage Window = startApp.stageM;

			Window.setTitle("Main page");
			Window.setScene(scene);
			Window.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public class examTimer implements Runnable {
		int second;
		int mints;

		public examTimer() {
			this.second = secondsExam;
			this.mints = mintsExam;
		}

		@Override
		public void run() {
			while (true) {
				if (done) {
					break;
				}
				if (checks >= 4) {
					if (firstTime) {
						try {
							Instance.sendMessage(Command.checkExt.ordinal() + "@" + examCode.getText());
							String respone = Instance.getClientConsole().getMessage().toString();
							if (!respone.equals("NoEx")) {
								mints += Integer.parseInt(respone);
								mintsExam = mints;
								firstTime = false;
								lastAddition = Integer.parseInt(respone);
							}
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else {
						try {
							Instance.sendMessage(Command.checkExt.ordinal() + "@" + examCode.getText());
							String respone = Instance.getClientConsole().getMessage().toString();
							try {
								Integer.parseInt(respone);
								if (!respone.equals("NoEx") && Integer.parseInt(respone) != lastAddition) {
									mints += Integer.parseInt(respone);
									mintsExam = mints;
									lastAddition = Integer.parseInt(respone);
								}
							} catch (Exception e) {
								// TODO: handle exception
							}

						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
					checks = 0;
				} else {
					checks++;
				}

				second--;
				secondsExam = second;
				seconds.setText(Integer.toString(second));
				minuts.setText(Integer.toString(mints));
				if (second <= 0) {
					mints--;
					mintsExam = mints;
					minuts.setText(Integer.toString(mints));
					if (mints < 0) {
						seconds.setText("00");
						minuts.setText("00");
						break;
					}
					second = 60;
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			Platform.runLater(() ->

			{
				if (!done) {
					List<String> exDis = new ArrayList<>();
					exDis.add(idNum.getText());
					exDis.add(duration);
					exDis.add(course);
					List<String> lines = new ArrayList<>();
					lines.add("Student with id: " + idNum.getText() + " Didn't finish in time");
					try {
						Instance.sendMessage(Command.submitHanedExam.ordinal() + "@" + teacherId + "@"
								+ generalOps.getJsonString(exDis) + "@" + generalOps.getJsonString(lines) + "@"
								+ idNum.getText() + "@" + examCode.getText());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				back();

			});

		}

	}
}
