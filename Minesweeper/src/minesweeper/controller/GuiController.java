package minesweeper.controller;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import minesweeper.Gui.gameImages.BoardImage;
import minesweeper.Gui.gameImages.ButtonImage;
import minesweeper.Gui.gameImages.MainImage;
import minesweeper.cui.Initiable;
import minesweeper.game.Minesweeper;
import minesweeper.game.MinesweeperListener;
import minesweeper.game.Piece;
import minesweeper.game.Square;
import minesweeper.images.ImageData;
import minesweeper.images.InvalidDimensionException;
import minesweeper.images.NotImageException;
import minesweeper.solver.Solver;

/**
 * A Gui controller for the Minesweeper game.
 * @author Troy Shaw
 *
 */
public class GuiController implements MinesweeperListener, Initiable {
	
	private Minesweeper game;

	//default values
	private int width = 8, height = 8, numMines = 10;

	private MainImage mainImage;
	private BoardImage boardImage;
	private ButtonImage buttonImage;

	private JFrame frame;
	private JPanel panel;
	
	private Solver solver;
	
	public GuiController() {
		
	}
	
	public GuiController(JFrame frame, JPanel panel) {
		this.frame = frame;
		this.panel = panel;
	}
	
	/**
	 * Starts a new game with the same parameters as the last game.
	 */
	public void newGame() {
		if (game != null) game.stopTimer();
		
		//first create game and solver
		game = new Minesweeper(width, height, numMines, this);
		solver = game.getSolver();
		
		//then tell our visual components to reset themselves
		mainImage.resetBoard(width, height, numMines);
		mainImage.setPositions();
		mainImage.redraw();
		
		//then we repaint our main panel
		panel.repaint();
	}

	/**
	 * Starts a new game with the given parameters.
	 * @param width
	 * @param height
	 * @param numMines
	 */
	public void newGame(int width, int height, int numMines) {
		this.width = width;
		this.height = height;
		this.numMines = numMines;
		
		//call normal newgame method
		newGame();
		
		//reset our frames size and redraw
		Dimension d = new Dimension(mainImage.getWidth(), mainImage.getHeight());
		panel.setPreferredSize(d);
		panel.setSize(d);
		panel.setMaximumSize(d);
		panel.setMinimumSize(d);
		panel.repaint();
		
		//board has likely changed size, so we need to pack the frame
		frame.pack();
	}

	/**
	 * Solves the game until no more mines can be logically deduced.
	 */
	public void solve() {
		solver.solve();
	}
	
	public void registerButtonImage(ButtonImage buttonImage) {
		this.buttonImage = buttonImage;
	}
	
	public void registerMainImage(MainImage mainImage) {
		this.mainImage = mainImage;
	}
	
	public void registerBoardImage(BoardImage boardImage) {
		this.boardImage = boardImage;
	}

	public void leftClick(int x, int y) {
		game.revealSquare(x, y);
	}

	public void rightClick(int x, int y) {
		game.toggleSquare(x, y);
	}

	public void bothClick(int x, int y) {
		game.revealSurrounding(x, y);
	}

	public void quit() {
		System.exit(0);
	}

	public void displayStats() {

	}

	public void displayHelp() {

	}

	public void displayAbout() {

	}

	@Override
	public void alreadyClickedEvent() {
		// TODO Auto-generated method stub

	}

	@Override
	public void squareRevealed(int x, int y, int numMines) {
		boardImage.drawPiece(x, y, Piece.getPiece(numMines));
	}

	@Override
	public void squareFlagged(int x, int y) {
		boardImage.drawPiece(x, y, Piece.flag);
		mainImage.redraw();
		panel.repaint();
	}

	@Override
	public void mineRevealed(int x, int y) {
		boardImage.disableBoard();
		boardImage.drawPiece(x, y, Piece.redMine);
	}

	@Override
	public void squareQuestioned(int x, int y) {
		boardImage.drawPiece(x, y, Piece.question);
		mainImage.redraw();
		panel.repaint();
	}

	@Override
	public void squareUnmarked(int x, int y) {
		boardImage.drawPiece(x, y, Piece.blank);
		mainImage.redraw();
		panel.repaint();
	}


	@Override
	public void squareBomb(int x, int y) {
		boardImage.drawPiece(x, y, Piece.mine);
	}

	@Override
	public void incorrectFlag(int x, int y) {
		boardImage.drawPiece(x, y, Piece.incorrectFlag);
	}
	
	@Override
	public void totalFlagsChanged(int numFlags) {
		mainImage.setMinesLeft(numFlags);
		mainImage.redraw();
		panel.repaint();
	}
	
	@Override
	public void gameLost() {
		buttonImage.setDead();
		boardImage.disableBoard();
		mainImage.redraw();
		panel.repaint();
	}
	

	@Override
	public void gameWon() {
		buttonImage.setWon();
		boardImage.disableBoard();
		mainImage.redraw();
		panel.repaint();
	}

	@Override
	public void tick() {
		mainImage.incrementTimer();
		panel.repaint();
	}

	@Override
	public void moveFinished() {
		boardImage.redraw();
		mainImage.redraw();
		panel.repaint();
	}
	
	public boolean hasDied() {
		return game.hasDied();
	}
	
	public boolean hasWon() {
		return game.hasWon();
	}

	public Piece getPiece(int x, int y) {
		Square s = game.getSquare(x, y);
		return s == null ? null : s.getPiece();
	}

	public Square[][] getPieces() {
		return game.getSquares();
	}
	
	public Piece[][] getSurroundingPieces(int x, int y) {
		Piece[][] p = new Piece[3][3];

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				p[i][j] = getPiece(x + i - 1, y + j - 1);
			}
		}

		return p;
	}

	public void setDefaultSkin() {
		ImageData.loadDefaultImages();
		
		mainImage.reskin();
		frame.repaint();
	}
	
	public void loadSkin(File file) {
		if (file == null) throw new NullPointerException("File cannot be null");
		
		try {
			ImageData.loadImages(file);
		} catch (InvalidDimensionException e) {
			JOptionPane.showMessageDialog(null, "Image must be 144 x 122 pixels", "Error reskinning", JOptionPane.ERROR_MESSAGE); 
			return;
		} catch (NotImageException e) {
			JOptionPane.showMessageDialog(null, "File wasn't an image", "Error reskinning", JOptionPane.ERROR_MESSAGE); 
			return;
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "There was an error reading file", "Error reskinning", JOptionPane.ERROR_MESSAGE); 
			return;
		}
		
		mainImage.reskin();
		frame.repaint();
	}
	
	@Override
	public void startNewGame() {
		newGame();
	}
}