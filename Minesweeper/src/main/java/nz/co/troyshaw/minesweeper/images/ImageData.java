package nz.co.troyshaw.minesweeper.images;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import nz.co.troyshaw.minesweeper.game.Piece;

/**
 * This class is responsible for handling image processing.
 * This class provides an interface to grab image data.
 * It also provides a way to load new images at runtime to change the skin of the game.
 *
 * @author Troy Shaw
 */
public class ImageData {

	public static final int spriteSheetWidth = 144;
	public static final int spriteSheetHeight = 122;

	public static final int squareSize = 16;
	public static final int faceSize = 25;

	public static final int timeDigitWidth = 11;
	public static final int timeDigitHeight = 21;

	public static final int timePanelWidth = 41;
	public static final int timePanelHeight = 25;
	
	public static final int borderComponentWidth = 12;
	public static final int borderComponentHeight = 11;

	/**
	 * The directory the images are contained in within the jar file.
	 */
	private static String DIRECTORY = "/Images/";

	/**
	 * The default images location.
	 */
	private static String DEFAULT_IMAGE = "cloneskin.bmp";

	/**
	 * The timers images. 
	 * Contains images for each number of the timer panel.
	 * Contains numbers 0-9, with number n being indexed at position n.
	 */
	public static BufferedImage[] timeImages = new BufferedImage[10];

	/**
	 * Image for the dash-time image.
	 */
	public static BufferedImage timeDashImage;

	/**
	 * Images for the various button modes for the restart button.
	 */
	public static BufferedImage buttonNonDead, buttonFieldClick, buttonDead, buttonWin, buttonDepress;

	/**
	 * Images for the border images.
	 */
	public static BufferedImage topLeftCorner;
	public static BufferedImage topRightCorner;
	public static BufferedImage bottomLeftCorner;
	public static BufferedImage bottomRightCorner;
	
	public static BufferedImage leftMiddle;
	public static BufferedImage rightMiddle;
	
	public static BufferedImage topStrip;
	public static BufferedImage middleStrip;
	public static BufferedImage bottomStrip;
	
	public static BufferedImage topLeftStrip;
	public static BufferedImage topRightStrip;
	public static BufferedImage bottomLeftStrip;
	public static BufferedImage bottomRightStrip;

	/**
	 * The adjacent-to-bomb square images. <br>
	 * These contain squares that convey information that the adjacent square has 0 to 8 adjacent mines.<br>
	 * The size of this array is 9 and a square with n adjacent mines is at array position n. <br>
	 */
	public static BufferedImage[] nonBombImages = new BufferedImage[9];

	/**
	 * Image for the time panel. The time panel is a black rectangle that houses the timer.
	 */
	public static BufferedImage timePanel;

	/**
	 * The background color for pixels.
	 */
	public static Color backgroundColor;

	private static Map<Piece, BufferedImage> boardImages = new HashMap<Piece, BufferedImage>();
	private static Map<Piece, BufferedImage> depressedBoardImages = new HashMap<Piece, BufferedImage>();


	/**
	 * Loads the default spritesheet.
	 */
	public static void loadDefaultImages() {
		try {
			splitImage(ImageIO.read(ImageIO.class.getResource(DIRECTORY + DEFAULT_IMAGE)));
		} catch (IOException e) {
			throw new Error("default image caused an IO error");
		}
	}

	/**
	 * Splits the image in the supplied path and sets the images used by the game. <p>
	 * The image must be of size (x, y) or InvalidDimensionException will be thrown.
	 *
	 * @param file the image file to load
	 * @throws InvalidDimensionException
	 * @throws NotImageException
	 * @throws IOException
	 */
	public static void loadImages(File file) 
			throws InvalidDimensionException, NotImageException, IOException {

		BufferedImage spriteSheet = ImageIO.read(file);

		//check null
		if (spriteSheet == null) throw new NotImageException();

		//check dimensions
		if (spriteSheet.getWidth() != spriteSheetWidth || spriteSheet.getHeight() != spriteSheetHeight)
			throw new InvalidDimensionException("Image dimensions must be "
					+ spriteSheetWidth + " by " + spriteSheetHeight);

		splitImage(spriteSheet);
	}

	private static void splitImage(BufferedImage spriteSheet) {
		BufferedImage[][] tempSquare = new BufferedImage[9][2];

		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 2; j++) {
				tempSquare[i][j] = spriteSheet.getSubimage(i * squareSize, j * squareSize, squareSize, squareSize);
			}
		}

		for (int i = 0; i < 9; i++) {
			nonBombImages[i] = spriteSheet.getSubimage(i * squareSize, 0, squareSize, squareSize);
		}

		for (int i = 0; i < 10; i++) {
			//add 1 to i * timeDigitWidth because there is a 1 pixel gap between images
			timeImages[i] = spriteSheet.getSubimage(i * (timeDigitWidth + 1), 33, timeDigitWidth, timeDigitHeight);
		}

		timeDashImage = spriteSheet.getSubimage(120, 33, timeDigitWidth, timeDigitHeight);

		timePanel = spriteSheet.getSubimage(28, 82, timePanelWidth, timePanelHeight);

		backgroundColor = new Color(spriteSheet.getRGB(70, 82));

		//border 
		topLeftCorner = spriteSheet.getSubimage(0, 82, 12, 11);
		topRightCorner = spriteSheet.getSubimage(15, 82, 12, 11);
		bottomLeftCorner = spriteSheet.getSubimage(0, 110, 12, 12);
		bottomRightCorner = spriteSheet.getSubimage(15, 110, 12, 12);
		
		leftMiddle = spriteSheet.getSubimage(0, 96, 12, 11);
		rightMiddle = spriteSheet.getSubimage(15, 96, 12, 11);
		
		topStrip = spriteSheet.getSubimage(13, 82, 1, 11);
		middleStrip = spriteSheet.getSubimage(13, 96, 1, 11);
		bottomStrip = spriteSheet.getSubimage(13, 110, 1, 12);
		
		topLeftStrip = spriteSheet.getSubimage(0, 94, 12, 1);
		topRightStrip = spriteSheet.getSubimage(15, 94, 12, 1);
		bottomLeftStrip = spriteSheet.getSubimage(0, 108, 12, 1);
		bottomRightStrip = spriteSheet.getSubimage(15, 108, 12, 1);
		
		boardImages.put(Piece.zero, tempSquare[0][0]);
		boardImages.put(Piece.one, tempSquare[1][0]);
		boardImages.put(Piece.two, tempSquare[2][0]);
		boardImages.put(Piece.three, tempSquare[3][0]);
		boardImages.put(Piece.four, tempSquare[4][0]);
		boardImages.put(Piece.five, tempSquare[5][0]);
		boardImages.put(Piece.six, tempSquare[6][0]);
		boardImages.put(Piece.seven, tempSquare[7][0]);
		boardImages.put(Piece.eight, tempSquare[8][0]);
		boardImages.put(Piece.blank, tempSquare[0][1]);
		boardImages.put(Piece.mine, tempSquare[2][1]);
		boardImages.put(Piece.flag, tempSquare[3][1]);
		boardImages.put(Piece.incorrectFlag, tempSquare[4][1]);
		boardImages.put(Piece.redMine, tempSquare[5][1]);
		boardImages.put(Piece.question, tempSquare[6][1]);

		depressedBoardImages.putAll(boardImages);
		depressedBoardImages.put(Piece.question, tempSquare[7][1]);
		depressedBoardImages.put(Piece.blank, tempSquare[1][1]);

		//load faces
		buttonNonDead = spriteSheet.getSubimage(0, 55, faceSize, faceSize);
		buttonFieldClick = spriteSheet.getSubimage(27, 55, faceSize, faceSize);
		buttonDead = spriteSheet.getSubimage(54, 55, faceSize, faceSize);
		buttonWin = spriteSheet.getSubimage(81, 55, faceSize, faceSize);
		buttonDepress= spriteSheet.getSubimage(108, 55, faceSize, faceSize);
	}

	public static Map<Piece, BufferedImage> getBoardPieces() {
		return boardImages;
	}

	public static Map<Piece, BufferedImage> getDepressedBoardPieces() {
		return depressedBoardImages;
	}

	/**
	 * Returns the game icon.
	 * @return the game image.
	 */
	public static Image getIconImage() {
		try {
			return ImageIO.read(ImageData.class.getResource("/images/minesweeper-icon.png"));
		} catch (Exception e) {
			throw new Error("Icon reading failed");
		}
	}
}