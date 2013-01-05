package minesweeper.Gui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.SwingUtilities;

import minesweeper.Gui.gameImages.BoardImage;
import minesweeper.Gui.gameImages.ButtonImage;

public class MouseManager implements MouseMotionListener, MouseListener {

	private ButtonImage buttonImage;
	private BoardImage boardImage;
	
	private boolean left, right;
	
	private ClickMode clickMode;
	
	public MouseManager(ButtonImage buttonImage, BoardImage boardImage) {
		this.buttonImage = buttonImage;
		this.boardImage = boardImage;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		switch (ClickMode.getClickMode(e)) {
		case both:
			break;
		case left:
			break;
		case right:
			break;
		default:
			break;
		
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		
		switch (ClickMode.getClickMode(e)) {
		case both:
			break;
		case left:
			break;
		case right:
			break;
		default:
			break;
		
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		
		switch (ClickMode.getClickMode(e)) {
		case both:
			break;
		case left:
			break;
		case right:
			break;
		default:
			break;
		
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		
		switch (ClickMode.getClickMode(e)) {
		case both:
			break;
		case left:
			break;
		case right:
			break;
		default:
			break;
		
		}
	}

	@Override
	public void mouseExited(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		
		switch (ClickMode.getClickMode(e)) {
		case both:
			break;
		case left:
			break;
		case right:
			break;
		default:
			break;
		
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		
		switch (ClickMode.getClickMode(e)) {
		case both:
			break;
		case left:
			break;
		case right:
			break;
		default:
			break;
		
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub

	}


	private enum ClickMode {
		left, right, both;

		public static ClickMode getClickMode(MouseEvent e) {
			boolean leftClick = SwingUtilities.isLeftMouseButton(e);
			boolean rightClick = SwingUtilities.isRightMouseButton(e);
			
			if (leftClick && rightClick) return both;
			if (leftClick) return left;
			if (rightClick) return right;
			throw new Error("no button was clicked");
		}
	}
}
