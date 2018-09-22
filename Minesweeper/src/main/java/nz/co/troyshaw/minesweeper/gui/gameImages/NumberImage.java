package nz.co.troyshaw.minesweeper.gui.gameImages;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import nz.co.troyshaw.minesweeper.images.ImageData;

/**
 * Represents an image that can show a positive or negative number.
 *
 * @author Troy Shaw
 */
public class NumberImage extends GameImage {

	private BufferedImage numberImage;
	private int value;
	
	/**
	 * Creates a new number image with an initial value of 0.
	 */
	public NumberImage() {
		this(0);
	}
	
	/**
	 * Creates a new number image with an initial value set to the given parameter.
	 * @param initValue the initial value
	 */
	public NumberImage(int initValue) {
		numberImage = new BufferedImage(ImageData.timePanelWidth, ImageData.timePanelHeight, ImageData.timePanel.getType());
		numberImage.createGraphics().drawImage(ImageData.timePanel, 0, 0, null);
		
		setValue(initValue);
	}
	
	@Override
	public void reskin() {
		numberImage.createGraphics().drawImage(ImageData.timePanel, 0, 0, null);
		setValue(value);
	}
	
	/**
	 * Returns the number image.
	 * @return
	 */
	@Override
	public BufferedImage getImage() {
		return numberImage;
	}
	
	/**
	 * Sets the value in the return image to the supplied parameter. <p>
	 * If <code>value</code> is negative, a negative sign is shown in hundreds digit, and only tens and ones digit is shown.
	 * @param value the value we wish to show
	 */
	public void setValue(int value) {
		this.value = value;
		
		int ones = Math.abs(value) % 10;
		int tens = Math.abs(value) / 10 % 10;
		int hundreds = Math.abs(value) / 100 % 10;
		
		Graphics2D g = numberImage.createGraphics();
		
		if (value >= 0) g.drawImage(ImageData.timeImages[hundreds], 2, 2, null);
		else g.drawImage(ImageData.timeDashImage, 2, 2, null);
		g.drawImage(ImageData.timeImages[tens], 15, 2, null);
		g.drawImage(ImageData.timeImages[ones], 28, 2, null);
	}
	
	public int getValue() {
		return value;
	}

	@Override
	public void redraw() {
		// TODO Auto-generated method stub
	}
}