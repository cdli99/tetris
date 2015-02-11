package com.yisoft.javafx.tetris;

import java.time.LocalTime;
import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

public class Tetris {
	private Group root;
	private Board board;
	private Text scoreText;
	private boolean running;
	private Timer timer;
	private boolean turboMode;

	private Parent constructNode() {
		root = new Group();

		VBox container = new VBox();
		container.setPadding(new Insets(10));
		container.setSpacing(8);
		container.setAlignment(Pos.CENTER);

		// top container
		MyButton newBtn = new MyButton("New");
		newBtn.setOnAction(actionEvent ->{
			newGame();
		});
		MyButton pauseBtn = new MyButton("Pause");
		pauseBtn.setOnAction(actionEvent -> {
			pause();
		});
		MyButton quitBtn = new MyButton("Quit");
		quitBtn.setOnAction(actionEvent -> {
			exit();
		});

		ButtonBar top = new ButtonBar(5, newBtn, pauseBtn, quitBtn);
		top.setAlignment(Pos.CENTER);

		// score
		scoreText = new Text("0");
		scoreText.setFill(Color.BLUE);
		scoreText.setFont(Font.font("Helvetica", FontWeight.BOLD, 10));
		TextFlow score = new TextFlow(new Text("Score: "), scoreText);
		scoreText.setStyle("-fx-border-color: white; -fx-border-width: 5;");

		// center
		HBox center = new HBox();
		board = new Board(this);
		board.setStyle("-fx-background-color: grey; -fx-border-width: 1; -fx-border-color: white");

		VBox centerRight = new VBox();
		Preview preview= new Preview();
		board.setPreview(preview);
		centerRight.getChildren().add(preview);
		preview.setStyle("-fx-border-color: white; -fx-border-width: 5;");

		center.getChildren().addAll(board, centerRight);

		// bottom container
		GridPane bottom = new GridPane();
		bottom.setHgap(10);
		bottom.setVgap(10);
		bottom.setPadding(new Insets(0, 10, 0, 10));
		bottom.setAlignment(Pos.CENTER);

		MyButton upBtn = new MyButton("Rotate ^)");
		bottom.add(upBtn, 1, 0);
		MyButton leftBtn = new MyButton("Left <");
		bottom.add(leftBtn, 0, 1);
		MyButton spaceBtn = new MyButton("Drop SP");
		spaceBtn.setMaxWidth(Double.POSITIVE_INFINITY);
		GridPane.setFillWidth(spaceBtn, true);
		bottom.add(spaceBtn, 1, 1);
		MyButton rightBtn = new MyButton("Right >");
		bottom.add(rightBtn, 2, 1);
		MyButton downBtn = new MyButton("Rotate (^");
		bottom.add(downBtn, 1, 2);
		// bottom.setGridLinesVisible(true);

		container.getChildren().addAll(top, score, center, bottom);

		VBox.setVgrow(center, Priority.ALWAYS);
		VBox.setVgrow(top, Priority.NEVER);
		VBox.setVgrow(bottom, Priority.NEVER);

		System.out.printf("canvas=%s, top=%s, bottom=%s\n", VBox.getVgrow(top),
				VBox.getVgrow(center), VBox.getVgrow(bottom));

		root.getChildren().addAll(container);
		Platform.runLater(() -> board.requestFocus());
		return root;
	}

	public void play(Stage primaryStage) {
		Scene scene = new Scene(constructNode(), Color.LIGHTBLUE);
		primaryStage.setScene(scene);
		addKeyListener(scene);
		primaryStage.resizableProperty().setValue(false);
		primaryStage.show();
		tick();
	}

	private void addKeyListener(Scene scn) {
		scn.setOnKeyReleased(keyEvent -> {
			switch(keyEvent.getCode().getName()){
			case "S":
			case "Down":
				setTurboMode(false);
				break;
			}
		});
		scn.setOnKeyPressed(keyEvent -> {
			System.out.printf("%s%s\n", keyEvent.getCode().getName(),
					keyEvent.isShiftDown() ? "+SHIFT" : "");
			KeyCombination pound = new KeyCodeCombination(KeyCode.Q,
					KeyCombination.CONTROL_DOWN);
			if (pound.match(keyEvent)) {
				exit();
			}
			switch (keyEvent.getCode().getName()) {
			case "N":
				newGame();
				break;
			case "P":
				pause();
				break;
			case "A":
			case "Left":
				board.moveLeft();
				break;
			case "D":
			case "Right":
				board.moveRight();
				break;
			case "S":
			case "Down":
				setTurboMode(true);
				break;
			case "W":
			case "Up":
				board.rotateCounterClockwise();
				break;
			case "Q":
			case "Space":
				board.dropAllTheWay();
				break;
			}
		});
	}

	private void exit() {
		deleteTimer();
		Platform.exit();
	}

	private void tick() {
		System.out.printf("runGame......%b\n",board.isGameOver());
		running = true;
		if (!board.isGameOver() && running) {
			deleteTimer();

			System.out.printf("runGame>>> start, getDelay()=%d\n",board.getDelay());
			timer=new Timer();
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					System.out.println("Running timerTask...");
					Platform.runLater(() -> {
						System.out.println("PLatform>>>Runlater...");
						System.out.println(LocalTime.now());
						board.tick();
						tick();
					});
				}
			}, isTurboMode()?board.getDelay()/4:board.getDelay());
		}
	}

	private void deleteTimer() {
		if (timer != null){
			timer.cancel();
			timer.purge();
		}		
	}

	private void pause() {
		if (running) {
			running = false;
			timer.cancel();
			timer.purge();
		} else {
			tick();
		}
	}

	private void newGame() {
		System.out.println("newGame");
		scoreText.setText("" + board.getScore());
		board.reset();
		tick();
		board.requestFocus();
	}

	public void updateScore() {
		scoreText.setText("" + board.getScore());

	}

	public boolean isRunning() {
		return this.running;
	}

	public boolean isTurboMode() {
		return turboMode;
	}

	public void setTurboMode(boolean turboMode) {
		this.turboMode = turboMode;
	}

	private class MyButton extends Button{

		public MyButton(String text) {
			super(text);
		}

		@Override
		public void requestFocus() {
		}
	}
	


}
