package minesweeper.gui.gameImages;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import minesweeper.controller.GuiController;
import minesweeper.images.ImageData;

public class ButtonImage extends GameImage {

	private boolean mousePressed, mouseContained, clickedInside;
	private State state;

	private BufferedImage buttonImage;

	private Rectangle bounds;

	private MainImage parent;
	private GuiController controller;

	public ButtonImage(MainImage parent) {
		this.parent = parent;
		
		bounds = new Rectangle(getX(), getY(), ImageData.faceSize, ImageData.faceSize);

		buttonImage = new BufferedImage(ImageData.faceSize, ImageData.faceSize, ImageData.buttonDead.getType());
		state = State.nonDead;
	}
	
	public void registerController(GuiController controller) {
		this.controller = controller;
	}

	@Override
	public void redraw() {
		Graphics2D g = buttonImage.createGraphics();
		BufferedImage i;
		
		if (clickedInside && mouseContained) i = ImageData.buttonDepress;
		else if (state == State.dead) i = ImageData.buttonDead;
		else if (state == State.win) i = ImageData.buttonWin;
		else if (clickedInside && !mouseContained) i = ImageData.buttonNonDead;
		else if (mousePressed) i = ImageData.buttonFieldClick;
		else i = ImageData.buttonNonDead;

		g.drawImage(i, 0, 0, null);
	}

	@Override
	public BufferedImage getImage() {
		return buttonImage;
	}

	@Override
	public void reskin() {
		//empty since directly access variables
	}

	/**
	 * Signals that the mouse has moved.
	 * @param x
	 * @param y
	 */
	public void mouseMoved(int x, int y) {
//		if (mouseContained != bounds.contains(x, y)) {
//			mouseContained = !mouseContained;
//			parent.repaintButton();
//		}
	}

	public void mouseDragged(int x, int y) {
		if (mouseContained != bounds.contains(x, y)) {
			mouseContained = !mouseContained;
			parent.repaintButton();
		}
	}

	public void mousePressed(int x, int y) {
		mousePressed = true;
		
		clickedInside = bounds.contains(x, y);
		mouseContained = clickedInside;

		parent.repaintButton();
	}

	public void mouseReleased(int x, int y) {
		if (clickedInside && bounds.contains(x, y)) {
			controller.newGame();
		}
		
		mousePressed = false;
		clickedInside = false;
		mouseContained = false;
		
		parent.repaintButton();
	}

	/**
	 * Sets button to the dead state and redraws button image.
	 */
	public void setDead() {
		changeState(State.dead);
	}

	/**
	 * Sets button to the won state and redraws button image.
	 */
	public void setWon() {
		changeState(State.win);
	}

	/**
	 * Sets button to its default state (start new game and after release on field).
	 */
	public void setDefault() {
		changeState(State.nonDead);
	}

	/**
	 * Internal method that changes the state and then repaints.
	 * @param state
	 */
	private void changeState(State state) {
		this.state = state;
		redraw();
	}

	/**
	 * Enum represents the current state of the button.
	 * @author Troy Shaw
	 *
	 */
	private enum State {nonDead, dead, win, depress}
}