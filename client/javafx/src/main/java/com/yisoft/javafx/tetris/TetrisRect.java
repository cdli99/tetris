package com.yisoft.javafx.tetris;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class TetrisRect extends Rectangle{
	
	public TetrisRect(double x, double y, double w,
			double h, Color fillColor) {
		super(x, y, w, h);
		setFill(fillColor);
		setStroke(Color.BLACK);
	}

	public TetrisRect move(int dx, int dy) {
		this.setTranslateX(this.getTranslateX()+dx);
		this.setTranslateY(this.getTranslateY()+dy);
		return this;
	}
}