package com.yisoft.javafx.tetris;

import static com.yisoft.javafx.tetris.Piece.BLOCK_SIZE;

import java.util.concurrent.ArrayBlockingQueue;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

public class Board extends Pane {
	private static int NUM_COLUMNS = 10; // total columns on board
	private static int NUM_ROWS = 27; // total rows on board
	/**
	 * The fallen blocks on board. row-wise, i.e., each row holds all xs, and
	 * row is index by y. Also the row[1] is the toppest row, or, the last row
	 * to play. The row on the bottom is row NUM_ROWS.
	 */
	private TetrisRect[][] grid;
	private int score;
	private Tetris game;
	private long delay;
	private Piece currentBlock;
	private ArrayBlockingQueue<Piece> pieceQueue = new ArrayBlockingQueue<Piece>(
			2);
	/**
	 * This is a list of blocks covered by previous currentBlock. Used for
	 * erasing previous blocks during falling
	 */
	private TetrisRect[] currentPos;
	private Preview preview;

	public Board(Tetris tetris) {
		setPrefSize(NUM_COLUMNS * BLOCK_SIZE, NUM_ROWS * BLOCK_SIZE);
		setMaxSize(NUM_COLUMNS * BLOCK_SIZE, NUM_ROWS * BLOCK_SIZE);
		game = tetris;
		reset();
		requestFocus();
		setClip(new Rectangle(NUM_COLUMNS * BLOCK_SIZE, NUM_ROWS * BLOCK_SIZE));
	}

	public int getScore() {
		return this.score;
	}

	public long getDelay() {
		return this.delay;
	}

	public boolean isGameOver() {
		for (Rectangle x : grid[0]) {
			if (x != null)
				return true;
		}
		return false;
	}

	private <T> boolean isAny(T[] arr, T value) {
		for (T v : arr) {
			if (v == value)
				return true;
		}
		return false;
	}

	public void run() {
		boolean okToRun = currentBlock.dropByOne();
		if (!okToRun) {
			storeCurrent();
			if (!isGameOver()) {
				nextPiece();
			}
		}
		game.updateScore();
		updateCurrentPos();
	}

	/**
	 * Gets the information from the current piece about where it is and uses
	 * this to store the piece on the board itself. Then calls removeFilled.
	 */
	private void storeCurrent() {
		int[][] loc = currentBlock.getCurrentRotation();
		int[] displacement = currentBlock.getBasePosition();
		for (int i = 0; i < loc.length; i++) {
			int x = loc[i][0];
			int y = loc[i][1];
			int iy = y + displacement[1];
			int ix = x + displacement[0];
			if (iy >= 0 && ix >= 0)
				grid[iy][ix] = currentPos[i];
		}
		removeFilled();
		delay = Math.max(delay - 2, 80); // improve the speed.
	}

	/**
	 * remove all filled rows and replaces them with empty ones, dropping all
	 * rows above them down each time a row is removed and increasing the score.
	 */
	private void removeFilled() {
		for (int i = 2; i < grid.length; i++) {
			Rectangle[] row = grid[i];
			// check if row is full (no null)
			if (!isAny(row, null)) {
				// remove from canvas blocks in full row
				for (int j = 0; j < row.length; j++) {
					getChildren().remove(grid[i][j]);
					grid[i][j] = null;
				}
				// move down all rows above and move their blocks on the canvas
				for (int k = i - 1; k > 0; k--) {
					TetrisRect[] rowToMove = grid[k];
					for (int l = 0; l < rowToMove.length; l++) {
						if (rowToMove[l] != null) {
							rowToMove[l].move(0, BLOCK_SIZE);
						}
					}
					System.arraycopy(grid[k], 0, grid[k + 1], 0, grid[k].length);
				}

				// insert new blank row at top
				grid[0] = new TetrisRect[NUM_COLUMNS];
				// adjust score for full flow
				score += 10;
			}
		}
	}

	public void moveLeft() {
		if (!isGameOver() && game.isRunning()) {
			currentBlock.move(-1, 0, 0);
		}
		updateCurrentPos();
	}

	public void moveRight() {
		if (!isGameOver() && game.isRunning()) {
			currentBlock.move(1, 0, 0);
		}
		updateCurrentPos();
	}

	public void rotateClockwise() {
		if (!isGameOver() && game.isRunning()) {
			currentBlock.move(0, 0, 1);
		}
		updateCurrentPos();

	}

	public void rotateCounterClockwise() {
		if (!isGameOver() && game.isRunning()) {
			currentBlock.move(0, 0, -1);
		}
		updateCurrentPos();
	}

	public void dropAllTheWay() {
		if (game.isRunning()) {
			boolean ran = currentBlock.dropByOne();
			for (TetrisRect block : currentPos) {
				getChildren().remove(block);
			}
			while (ran) {
				score += 1;
				ran = currentBlock.dropByOne();
			}
			updateCurrentPos();
			storeCurrent();
			if (!isGameOver()) {
				nextPiece();
			}
			game.updateScore();
			updateCurrentPos();
		}
	}

	private void nextPiece() {
		while (pieceQueue.size() < 2) {
			pieceQueue.add(Piece.nextPiece(this));
		}
		currentBlock = pieceQueue.poll();
		currentPos = null;
		showPreviewPiece();
	}

	public void showPreviewPiece() {
		preview.showPiece(pieceQueue.peek());
	}

	public boolean emptyAt(int x, int y) {
		if (!(x >= 0 && x < NUM_COLUMNS))
			return false;
		else if (y < 1)
			return true;
		else if (y >= NUM_ROWS)
			return false;
		return grid[y][x] == null;
	}

	private TetrisRect[] updateCurrentPos() {
		if (currentPos != null) {
			for (TetrisRect r : currentPos) {
				getChildren().remove(r);
			}
		}
		int[][] blocks = currentBlock.getCurrentRotation();
		int[] start = currentBlock.getBasePosition();
		TetrisRect[] newBlocks = new TetrisRect[blocks.length];
		for (int i = 0; i < blocks.length; i++) {
			int[] b = blocks[i];
			newBlocks[i] = new TetrisRect(start[0] * BLOCK_SIZE + b[0]
					* BLOCK_SIZE, start[1] * BLOCK_SIZE + b[1] * BLOCK_SIZE,
					BLOCK_SIZE, BLOCK_SIZE, currentBlock.getColor());
		}
		getChildren().addAll(newBlocks);
		currentPos = newBlocks;
		return currentPos;
	}

	public void reset() {
		getChildren().clear();
		grid = new TetrisRect[NUM_ROWS][NUM_COLUMNS];
		currentBlock = Piece.nextPiece(this);
		score = 0;
		delay = 500;
		updateCurrentPos();
	}

	public void setPreview(Preview preview) {
		this.preview = preview;

	}

}
