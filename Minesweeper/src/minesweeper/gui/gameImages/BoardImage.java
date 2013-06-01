package minesweeper.gui.gameImages;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Map;

import minesweeper.controller.GuiController;
import minesweeper.game.Piece;
import minesweeper.game.Square;
import minesweeper.images.ImageData;

public class BoardImage extends GameImage {
	private int width, height;
	
	private Map<Piece, BufferedImage> depressedImages = ImageData.getDepressedBoardPieces();
	
	private BufferedImage returnImage, boardImage, singleClickImage, multiClickImage;
	
	private Rectangle bounds;
	private int squareSize = ImageData.squareSize;
	
	private GuiController controller;
	
	private MainImage parent;
	
	//mouse stuff
	private ClickMode clickMode;
	private boolean leftClicked, rightClicked, dirty, isListening, onScreen;
	private int xClick, yClick;
	
	public BoardImage(MainImage parent, int width, int height) {
		this.parent = parent;
		
		bounds = new Rectangle(0, 0, width * squareSize, height * squareSize);
		
		singleClickImage = new BufferedImage(squareSize, squareSize, ImageData.bottomLeftCorner.getType());
		multiClickImage = new BufferedImage(squareSize * 3, squareSize * 3, ImageData.bottomLeftCorner.getType());
		
		//sets variables, creates first image, then draws blank board
		resetBoard(width, height);
	}
	
	public void registerController(GuiController controller) {
		this.controller = controller;
	}
	
	/**
	 * Resets the board by redrawing all empty squares to the internal image, at the current size.
	 */
	public void resetBoard() {
		Graphics2D g2d = boardImage.createGraphics();
		BufferedImage i = ImageData.getBoardPieces().get(Piece.blank);

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
	 * Sets the boards size to the given params, creates a new image at the given size, then draws a blank board at new size.
	 * @param x
	 * @param y
	 */
	public void resetBoard(int width, int height) {
		this.width = width;
		this.height = height;
		bounds.setSize(width * squareSize, height * squareSize);
		boardImage = new BufferedImage(squareSize * width, squareSize * height, ImageData.bottomLeftCorner.getType());
		returnImage = new BufferedImage(squareSize * width, squareSize * height, ImageData.bottomLeftCorner.getType());
		
		resetBoard();
	}
	
	@Override
	public void redraw() {	
		Graphics2D g = returnImage.createGraphics();

		g.drawImage(boardImage, 0, 0, null);

		if (onScreen) {
			switch (clickMode) {
			case left:
				g.drawImage(singleClickImage, xClick * squareSize, yClick * squareSize, null);
				break;
			case both: 
				g.drawImage(multiClickImage, (xClick - 1) * squareSize, (yClick - 1) * squareSize, null);
				break;
			default: 
				break;
			}
		}
	}
	
	@Override
	public void reskin() {
		Graphics2D g = boardImage.createGraphics();
		Square[][] p = controller.getPieces();
		
		for (int i = 0; i < p.length; i++) {
			for (int j = 0; j < p[i].length; j++) {
				g.drawImage(ImageData.getBoardPieces().get(p[i][j].getPiece()), i * squareSize, j * squareSize, null);
			}
		}
	}
	
	public void manageDepress(int x, int y) {
		this.xClick = x;
		this.yClick = y;

		//get the data we are to draw
		Graphics2D g2d;

		switch (clickMode) {
		case left:
			g2d = singleClickImage.createGraphics();
			g2d.drawImage(depressedImages.get(controller.getPiece(xClick, yClick)), 0, 0, null);
			break;
		case both:
			g2d = multiClickImage.createGraphics();
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

		parent.repaintBoard();
	}
	
	public void mousePressed(MouseEvent e) {
		if (!isListening) return;

		dirty = false;
		onScreen =  bounds.contains(e.getX() - getX(), e.getY() - getY());

		boolean left = e.getButton() == MouseEvent.BUTTON1;
		boolean right = e.getButton() == MouseEvent.BUTTON3;

		xClick = (e.getX() - getX()) / squareSize;
		yClick = (e.getY() - getY()) / squareSize;

		
		
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
			parent.repaintBoard();
		}

		if (left) leftClicked = true;
		else if (right) rightClicked = true;
	}

	public void mouseReleased(MouseEvent e) {
		if (!isListening) return;

		//can only click if dirty is not true
		boolean left = e.getButton() == MouseEvent.BUTTON1;
		boolean right = e.getButton() == MouseEvent.BUTTON3;

		if (!dirty) {
			int xClick = (e.getX() - getX()) / squareSize;
			int yClick = (e.getY() - getY()) / squareSize;

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

			parent.repaintBoard();
		}

		if (left) leftClicked = false;
		else if (right) rightClicked = false;
	}


	public void mouseDragged(MouseEvent e) {
		if (!isListening) return;
		
		int x = (e.getX() - getX()) / squareSize;
		int y = (e.getY() - getY()) / squareSize;
		if (x == xClick && y == yClick) return;

		manageDepress(x, y);

		parent.repaintBoard();
	}

	
	/**
	 * Signals that the mouse has entered this image.
	 * @param e
	 */
	public void mouseEntered(MouseEvent e) {
		onScreen = true;
		//repaint();
	}

	/**
	 * Signals that the mouse has exited this image.
	 * @param e
	 */
	public void mouseExited(MouseEvent e) {
		onScreen = false;
		//repaint();
	}


	@Override
	public BufferedImage getImage() {
		return returnImage;
	}
	
	public void drawPiece(int x, int y, Piece piece) {
		Graphics2D g2d = boardImage.createGraphics();

		g2d.drawImage(ImageData.getBoardPieces().get(piece), x * squareSize, y * squareSize, null);
	}
	
	public void disableBoard() {
		isListening = false;
	}
	
	private enum ClickMode {none, left, both}
}