package minesweeper.gui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Map;

import javax.swing.JPanel;

import minesweeper.controller.GuiController;
import minesweeper.game.Piece;
import minesweeper.images.ImageData;

/**
 * Boards have a 2d array of Piece objects that represent what to draw at that square.
 * Each square has a pressed and a depressed image.
 * For certain squares the image is identical, for others it changes (question marks, normal squares, etc).
 * The draw operation simply draws 
 * @author Troy Shaw
 *
 */
public class MinePanel extends JPanel{

	/**
	 * Size in pixels of the width/ height of a square.
	 */
	private int squareSize = ImageData.squareSize;

	/**
	 * The number of squares the board is.
	 */
	private int width, height;

	/**
	 * Image of each square type.
	 */
	private Map<Piece, BufferedImage> images = ImageData.getBoardPieces();
	/**
	 * Image of the two squares which have depressed images. (blank and question-mark)
	 */
	private Map<Piece, BufferedImage> depressedImages = ImageData.getDepressedBoardPieces();


	private BufferedImage boardImage, singleclickImage, multiclickImage;

	private ClickMode clickMode;
	private int xClick, yClick;
	private boolean leftClicked, rightClicked, isListening;

	/**
	 * True after a click release, false after a click press.
	 */
	private boolean dirty;

	/**
	 * True if mouse is on the mine panel, false otherwise.
	 */
	private boolean onScreen;

	private GuiController controller;

	public MinePanel(int width, int height) {
		MouseController m = new MouseController();
		addMouseListener(m);
		addMouseMotionListener(m);

		//create clickImage and 
		singleclickImage = new BufferedImage(squareSize, squareSize, BufferedImage.TYPE_INT_RGB);
		multiclickImage = new BufferedImage(3 * squareSize, 3 * squareSize, BufferedImage.TYPE_INT_RGB);

		resetBoard(width, height);
	}

	/**
	 * Resets the board to the current size.
	 */
	public void resetBoard() {
		Graphics2D g2d = boardImage.createGraphics();
		BufferedImage i = images.get(Piece.blank);

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				g2d.drawImage(i, x * squareSize, y * squareSize, null);
			}
		}

		isListening = true;
		dirty = false;
		leftClicked = false;
		rightClicked = false;
		clickMode = ClickMode.none;
	}

	/**
	 * Resets the board to the given dimensions.
	 * @param width
	 * @param height
	 */
	public void resetBoard(int width, int height) {
		this.width = width;
		this.height = height;
		
		boardImage = new BufferedImage(width * squareSize, height * squareSize, BufferedImage.TYPE_INT_RGB);
		
		resetBoard();
	}

	/**
	 * Disables the board from receiving mouse events.
	 */
	public void disableBoard() {
		isListening = false;
	}

	public void registerController(GuiController controller) {
		this.controller = controller;
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);

		Graphics2D g2d = (Graphics2D) g;

		g2d.drawImage(boardImage, 0, 0, null);


		if (onScreen) {
			switch (clickMode) {
			case left:
				g2d.drawImage(singleclickImage, xClick * squareSize, yClick * squareSize, null);
				break;
			case both: 
				g2d.drawImage(multiclickImage, (xClick - 1) * squareSize, (yClick - 1) * squareSize, null);
				break;
			default: 
				break;
			}
		}

	}

	private void manageDepress(int xClick, int yClick) {
		this.xClick = xClick;
		this.yClick = yClick;

		//get the data we are to draw
		Graphics2D g2d;

		switch (clickMode) {
		case left:
			g2d = singleclickImage.createGraphics();
			g2d.drawImage(depressedImages.get(controller.getPiece(xClick, yClick)), 0, 0, null);
			break;
		case both:
			g2d = multiclickImage.createGraphics();
			Piece[][] p = controller.getSurroundingPieces(xClick, yClick);
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					g2d.drawImage(depressedImages.get(p[i][j]), i * squareSize, j * squareSize, null);
				}
			}
			break;
		default: 
			break;
		}

		repaint();
	}

	private class MouseController extends MouseAdapter {

		@Override
		public void mousePressed(MouseEvent e) {
			if (!isListening) return;

			dirty = false;
			onScreen =  (e.getX() >= 0 && e.getX() < width * squareSize && e.getY() >= 0 && e.getY() < height * squareSize);

			boolean left = e.getButton() == MouseEvent.BUTTON1;
			boolean right = e.getButton() == MouseEvent.BUTTON3;

			xClick = e.getX() / squareSize;
			yClick = e.getY() / squareSize;

			//double press mode
			if ((left && rightClicked) || (right && leftClicked)) {
				//do double press
				clickMode = ClickMode.both;
				manageDepress(xClick, yClick);
			} else if ((left && (!leftClicked && !rightClicked))) {
				//do single press
				clickMode = ClickMode.left;
				manageDepress(xClick, yClick);
			} else if (right && !leftClicked) {
				//toggle square
				controller.rightClick(xClick, yClick);
				repaint();
			}

			if (left) leftClicked = true;
			else if (right) rightClicked = true;
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			if (!isListening) return;

			//can only click if dirty is not true
			boolean left = e.getButton() == MouseEvent.BUTTON1;
			boolean right = e.getButton() == MouseEvent.BUTTON3;

			if (!dirty) {
				int xClick = e.getX() / squareSize;
				int yClick = e.getY() / squareSize;

				switch (clickMode) {
				case both: 
					controller.bothClick(xClick, yClick); 
					break;
				case left:
					controller.leftClick(xClick, yClick); 
					break;
				default:
					break;
				}

				clickMode = ClickMode.none;
				dirty = true;

				repaint();
			}

			if (left) leftClicked = false;
			else if (right) rightClicked = false;
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			if (!isListening) return;
			if (e.getX() / squareSize == xClick && e.getY() / squareSize == yClick) return;

			manageDepress(e.getX() / squareSize, e.getY() / squareSize);

			repaint();
		}

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
	}

	private enum ClickMode {none, left, both}


	/**
	 * Draws the given piece at the given board square location.
	 * Doesn't redraw.
	 * 
	 * @param x
	 * @param y
	 * @param piece
	 */
	public void drawPiece(int x, int y, Piece piece) {
		Graphics2D g2d = boardImage.createGraphics();

		g2d.drawImage(images.get(piece), x * squareSize, y * squareSize, null);
	}
}