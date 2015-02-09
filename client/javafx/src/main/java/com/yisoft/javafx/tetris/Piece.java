package com.yisoft.javafx.tetris;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javafx.scene.paint.Color;

/**
 * Class responsible for the pieces and their movements
 */
public class Piece {
	public static int BLOCK_SIZE = 15; // width of a square block

	public static int[][][] T = rotations(new int[][] { { 0, 0 }, { -1, 0 },
			{ 1, 0 }, { 0, -1 } });
	public static int[][][] L = rotations(new int[][] { { 0, 0 }, { 0, -1 },
			{ 0, 1 }, { 1, 1 } });
	public static int[][][] IL = rotations(new int[][] { { 0, 0 }, { 0, -1 },
			{ 0, 1 }, { -1, 1 } });
	public static int[][][] S = rotations(new int[][] { { 0, 0 }, { -1, 0 },
			{ 0, -1 }, { 1, -1 } });
	public static int[][][] Z = rotations(new int[][] { { 0, 0 }, { 1, 0 },
			{ 0, -1 }, { -1, -1 } });
	public static int[][][][] All_Pieces = new int[][][][] {
			{ { { 0, 0 }, { 1, 0 }, { 0, 1 }, { 1, 1 } } }, // square (only needs one)
			T, // T
			{ { { 0, 0 }, { -1, 0 }, { 1, 0 }, { 2, 0 } }, { { 0, 0 }, { 0, -1 }, { 0, 1 }, { 0, 2 } } }, // long (only needs two) 
			L, // L
			IL, // inverted L
			S, // S
			Z, // Z
	};

	public static int[][][] samplePiece() {
		int index = (int) (Math.random() * All_Pieces.length);
		return All_Pieces[index];
	}

	public static Piece nextPiece(Board board) {
		return new Piece(samplePiece(), board);
	}

	private static int[][][] rotations(int[][] from) {
		List<int[]> rotate1 = Arrays.asList(from).stream().map(p -> {
			return new int[] { -p[1], p[0] };
		}).collect(Collectors.toList());
		List<int[]> rotate2 = Arrays.asList(from).stream().map(p -> {
			return new int[] { -p[0], -p[1] };
		}).collect(Collectors.toList());
		List<int[]> rotate3 = Arrays.asList(from).stream().map(p -> {
			return new int[] { p[1], -p[0] };
		}).collect(Collectors.toList());
		return new int[][][] { from, rotate1.toArray(new int[][] { { 0 } }),
				rotate2.toArray(new int[][] { { 0 } }),
				rotate3.toArray(new int[][] { { 0 } }), };
	}

	public Color[] All_Colors = new Color[] { Color.DARKGREEN, Color.DARKBLUE,
			Color.DARKRED, Color.GOLD, Color.PURPLE, Color.ORANGERED,
			Color.LIGHTSKYBLUE };
	private int[][][] allRotations;
	private int rotationIndex;
	private int color;
	private int[] basePosition;
	private Board board;
	private boolean moved;

	public Piece(int[][][] allPieces, Board board) {
		this.allRotations = allPieces;
		this.rotationIndex = (int) (Math.random() * (allPieces.length));
		this.color = (int) (Math.random() * (All_Colors.length));
		this.basePosition = new int[] { 5, 0 };
		this.board = board;
		this.moved = true;
	}

	public int[][] getCurrentRotation() {
		return this.allRotations[this.rotationIndex];
	}
	
	public int[] getBound(){
		int xmin= Integer.MAX_VALUE;
		int xmax = Integer.MIN_VALUE;
		int ymin= Integer.MAX_VALUE;
		int ymax = Integer.MIN_VALUE;
		int[][] blocks = getCurrentRotation();
		for(int i=0;i<blocks.length;i++){
			if(xmin>blocks[i][0]) xmin=blocks[i][0];
			if(xmax<blocks[i][0]) xmax=blocks[i][0];
			if(ymin>blocks[i][1]) ymin=blocks[i][1];
			if(ymax<blocks[i][1]) ymax=blocks[i][1];
		}
		return new int[]{xmin, ymin, xmax, ymax};
	}

	public Color getColor() {
		return All_Colors[color];
	}

	public int[] getBasePosition() {
		return basePosition;
	}

	public boolean isMoved() {
		return moved;
	}

	public boolean dropByOne() {
		moved = move(0, 1, 0);
		return moved;
	}

	/**
	 * Take the intended movement in x, y and rotation and checks to see if the
	 * movement is possible. If it is, make this movement and returns true.
	 * <p/>
	 * This does not move the block!
	 * 
	 * @param deltaX
	 * @param deltaY
	 * @param deltaRotation
	 * @return
	 */
	public boolean move(int deltaX, int deltaY, int deltaRotation) {
		/*
		 * Ensures that the rotation will always be a possible formation (as
		 * opposed to null) by altering the intended rotation so that it stays
		 * within the bound of the rotation array.
		 */
		boolean canMove = true;
		int potentialIndex = (rotationIndex + deltaRotation)
				% allRotations.length;
		if(potentialIndex<0)
			potentialIndex+=allRotations.length;
		int[][] potential = allRotations[potentialIndex];
		/*
		 * For each individual block in the piece, check it the intended move
		 * will put the block in a occupied space
		 */
		for (int[] p : potential) {
			if (!board.emptyAt(p[0] + deltaX + basePosition[0], p[1] + deltaY
					+ basePosition[1]))
				canMove = false;
		}
		if (canMove) {
			basePosition[0] += deltaX;
			basePosition[1] += deltaY;
			rotationIndex = potentialIndex;
		}
		return canMove;
	}
	
}
