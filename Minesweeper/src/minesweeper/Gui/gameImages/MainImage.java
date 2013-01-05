package minesweeper.Gui.gameImages;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import minesweeper.Controller.GuiController;
import minesweeper.Gui.gameImages.*;
import minesweeper.images.ImageData;

/**
 * Reprensents the main image of the game. This acts as a "container" for the border images, the main board, the mines left
 * image, the timer image and the button image. Clicks are deferred through this class to the board and button. <p>
 * 
 * The components draw themselves onto this component, then this image is returned to be drawn.
 *
 * @author Troy Shaw
 */
public class MainImage extends GameImage {
	private BorderImage borderImage;
	private NumberImage minesLeftImage;
	private NumberImage timerImage;
	private ButtonImage buttonImage;
	private BoardImage gameImage;

	private BufferedImage thisImage;

	private JPanel parent;
	private GuiController controller;

	private int width, height, numMines;

	public MainImage(JPanel parent, int width, int height, int numMines) {
		this.parent = parent;

		this.width = width;
		this.height = height;
		this.numMines = numMines;

		timerImage = new NumberImage();
		minesLeftImage = new NumberImage(numMines);

		borderImage = new BorderImage(width, height);
		gameImage = new BoardImage(this, width, height);

		buttonImage = new ButtonImage(this);

		thisImage = new BufferedImage(getWidth(), getHeight(), ImageData.bottomLeftCorner.getType());

		setPositions();
		redraw();
	}

	public void resetBoard() {
		minesLeftImage.setValue(numMines);
		minesLeftImage.redraw();

		timerImage.setValue(0);
		timerImage.redraw();

		buttonImage.setDefault();
		buttonImage.redraw();

		gameImage.resetBoard();
		gameImage.redraw();
	}

	public void resetBoard(int width, int height, int numMines) {
		this.width = width;
		this.height = height;
		this.numMines = numMines;

		buttonImage.setDefault();
		buttonImage.redraw();

		gameImage.resetBoard(width, height);
		gameImage.redraw();

		timerImage.setValue(0);
		timerImage.redraw();

		borderImage.resetSize(width, height);
		borderImage.redraw();

		minesLeftImage.setValue(numMines);
		minesLeftImage.redraw();

		//do this last since getWidth() and getHeight() depend on borderImage
		thisImage = new BufferedImage(getWidth(), getHeight(), ImageData.bottomLeftCorner.getType());
	}

	public void setPositions() {
		borderImage.setPosition(0, 0);
		gameImage.setPosition(ImageData.topLeftCorner.getWidth(), ImageData.topLeftCorner.getHeight() + borderImage.getInfoPanelHeight() + ImageData.leftMiddle.getHeight());
		minesLeftImage.setPosition(ImageData.borderComponentWidth + 4, ImageData.borderComponentHeight + 4);
		timerImage.setPosition(getWidth() - ImageData.borderComponentWidth - ImageData.timePanelWidth - 4, ImageData.borderComponentHeight + 4);
		buttonImage.setPosition((borderImage.getWidth() - buttonImage.getImage().getWidth()) / 2, 
				ImageData.topRightCorner.getHeight() + (borderImage.getInfoPanelHeight() - buttonImage.getImage().getHeight()) / 2);
	}


	/**
	 * Redraws the main image with whatever current state the child image components have.
	 */
	@Override
	public void redraw() {
		Graphics2D g = thisImage.createGraphics();

		g.drawImage(borderImage.getImage(), borderImage.getX(), borderImage.getY(), null);
		g.drawImage(gameImage.getImage(), gameImage.getX(), gameImage.getY(), null);
		g.drawImage(timerImage.getImage(), timerImage.getX(), timerImage.getY(), null);
		g.drawImage(minesLeftImage.getImage(), minesLeftImage.getX(), minesLeftImage.getY(), null);
		g.drawImage(buttonImage.getImage(), buttonImage.getX(), buttonImage.getY(), null);
	}

	/**
	 * Increments the timer and redraws the timer image.
	 */
	public void incrementTimer() {
		timerImage.setValue(timerImage.getValue() + 1);

		//redraw timer image
		thisImage.createGraphics().drawImage(timerImage.getImage(), timerImage.getX(), timerImage.getY(), null);
	}

	public void setMinesLeft(int flagsLeft) {
		minesLeftImage.setValue(flagsLeft);
		
		//redraw timer image
		thisImage.createGraphics().drawImage(minesLeftImage.getImage(), minesLeftImage.getX(), minesLeftImage.getY(), null);
	}

	@Override
	public BufferedImage getImage() {
		return thisImage;
	}

	/**
	 * Reskins and redraws all children components.
	 */
	@Override
	public void reskin() {
		borderImage.reskin();
		gameImage.reskin();
		buttonImage.reskin();
		borderImage.reskin();
		timerImage.reskin();
		minesLeftImage.reskin();

		borderImage.redraw();
		gameImage.redraw();
		buttonImage.redraw();
		borderImage.redraw();
		timerImage.redraw();
		minesLeftImage.redraw();

		redraw();
	}

	public void mousePressed(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) buttonImage.mousePressed(e.getX() - buttonImage.getX(), e.getY() - buttonImage.getY());
		gameImage.mousePressed(e);
	}

	public void mouseReleased(MouseEvent e) {
		buttonImage.mouseReleased(e.getX() - buttonImage.getX(), e.getY() - buttonImage.getY());
		gameImage.mouseReleased(e);
	}

	public void mouseDragged(MouseEvent e) {
		buttonImage.mouseDragged(e.getX() - buttonImage.getX(), e.getY() - buttonImage.getY());
		gameImage.mouseDragged(e);
	}

	public void mouseMoved(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) buttonImage.mouseMoved(e.getX() - buttonImage.getX(), e.getY() - buttonImage.getY());
		//gameImage.mouseMoved(e);
	}

	/**
	 * Returns the width of the container image.
	 * @return the width of this image
	 */
	public int getWidth() {
		return borderImage.getWidth();
	}

	/**
	 * Returns the height of the container image.
	 * @return the height of this image
	 */
	public int getHeight() {
		return borderImage.getHeight();
	}

	public void repaintButton() {
		buttonImage.redraw();

		Graphics2D g = thisImage.createGraphics();
		g.drawImage(buttonImage.getImage(), buttonImage.getX(), buttonImage.getY(), null);

		parent.repaint();
	}
	
	public void repaintBoard() {
		gameImage.redraw();
		
		Graphics2D g = thisImage.createGraphics();
		g.drawImage(gameImage.getImage(), gameImage.getX(), gameImage.getY(), null);

		parent.repaint();
	}

	public BoardImage getBoardImage() {
		return gameImage;
	}

	public ButtonImage getButtonImage() {
		return buttonImage;
	}
}