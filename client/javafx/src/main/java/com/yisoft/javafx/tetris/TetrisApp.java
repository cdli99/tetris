package com.yisoft.javafx.tetris;

import javafx.application.Application;
import javafx.stage.Stage;

public class TetrisApp extends Application{
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		Tetris tetris=new Tetris();
		tetris.play(primaryStage);
	}
}