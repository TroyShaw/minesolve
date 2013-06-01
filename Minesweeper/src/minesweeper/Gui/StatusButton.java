package minesweeper.Gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;

import minesweeper.images.ImageData;

public class StatusButton extends JPanel {

	private State state;
	private Initiable initiater;
	private Map<State, BufferedImage> images;
	
	private boolean onScreen, mousePressed;
	
	/**
	 * Creates a new StatusButton.
	 */
	public StatusButton() {
		state = State.nonDead;
		
		getImages();
		setSize();
		registerListeners();
	}
	
	private void getImages() {
		images = new HashMap<State, BufferedImage>();
		
		images.put(State.dead, ImageData.buttonDead);
		images.put(State.depress, ImageData.buttonDepress);
		images.put(State.fieldClick, ImageData.buttonFieldClick);
		images.put(State.win, ImageData.buttonWin);
		images.put(State.nonDead, ImageData.buttonNonDead);
	}
	
	private void setSize() {
		Dimension d = new Dimension(ImageData.faceSize, ImageData.faceSize);
		setPreferredSize(d);
		setMaximumSize(d);
		setSize(d);
		setMinimumSize(d);
	}
	
	private void registerListeners() {
		MouseAdapter listener = new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				onScreen = true;
				repaint();
			}
			
			@Override 
			public void mouseExited(MouseEvent e) {
				onScreen = false;
				repaint();
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				mousePressed = true;
				repaint();
			}
			
			@Override
			public void mouseReleased(MouseEvent e) {
				mousePressed = false;
				
				if (onScreen) {
					state = State.nonDead;
					repaint();
					
					if (initiater != null) initiater.startNewGame();
				}
			}
		};
		
		addMouseListener(listener);
	}
	
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		
		if (mousePressed && onScreen) g.drawImage(images.get(State.depress), 0, 0, null);
		else g.drawImage(images.get(state), 0, 0, null);
	}
	
	/**
	 * Registers the listener that will be notified when this button is clicked.
	 * @param initiater
	 */
	public void registerInitiater(Initiable initiater) {
		this.initiater = initiater;
	}
	
	/**
	 * Sets button to the dead state.
	 */
	public void setDead() {
		changeState(State.dead);
	}
	
	/**
	 * Sets button to the won state.
	 */
	public void setWon() {
		changeState(State.win);
	}
	
	/**
	 * Sets button to the state when the user is clicking the field.
	 */
	public void setFieldclick() {
		changeState(State.fieldClick);
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
		repaint();
	}
	
	/**
	 * Enum represents the current state of the button.
	 * @author Troy Shaw
	 *
	 */
	private enum State {nonDead, fieldClick, dead, win, depress};
}