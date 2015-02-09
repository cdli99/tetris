package com.yisoft.javafx.tetris;

import static com.yisoft.javafx.tetris.Piece.BLOCK_SIZE;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

public class Preview extends Pane {
	public Preview() {
		setPrefSize(100,100);
		setMaxSize(100,100);
		setClip(new Rectangle(100,100));
	}

	public void showPiece(Piece piece) {
		this.getChildren().clear();
		
		int[][] blocks = piece.getCurrentRotation();
		int[] bound = piece.getBound();
		double x = (bound[2]+bound[0]+1)*BLOCK_SIZE/2.0d;
		double y = (bound[3]+bound[1]+1)*BLOCK_SIZE/2.0d;
		
		int dx = (int)(getWidth()/2-x);
		int dy = (int)(getHeight()/2-y);
		
		TetrisRect[] newBlocks = new TetrisRect[blocks.length];
		for(int i=0;i<blocks.length;i++){
			int[] b = blocks[i];
			newBlocks[i]=new TetrisRect(
					b[0]*BLOCK_SIZE+dx,
					b[1]*BLOCK_SIZE+dy,
					BLOCK_SIZE,BLOCK_SIZE, piece.getColor());
		}
		getChildren().addAll(newBlocks);
	}
}
