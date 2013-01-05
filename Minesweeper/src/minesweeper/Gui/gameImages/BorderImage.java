package minesweeper.Gui.gameImages;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import minesweeper.images.ImageData;

public class BorderImage extends GameImage {

	//height of the info panel that contains the counters and button
	private int infoPanelHeight = 33;

	private int width, height;
	private BufferedImage borderImage;

	private BufferedImage topLeftCorner = ImageData.topLeftCorner;
	private BufferedImage topRightCorner = ImageData.topRightCorner;
	private BufferedImage bottomLeftCorner = ImageData.bottomLeftCorner;
	private BufferedImage bottomRightCorner = ImageData.bottomRightCorner;

	private BufferedImage leftMiddle = ImageData.leftMiddle;
	private BufferedImage rightMiddle = ImageData.rightMiddle;

	private BufferedImage topStrip = ImageData.topStrip;
	private BufferedImage middleStrip = ImageData.middleStrip;
	private BufferedImage bottomStrip = ImageData.bottomStrip;

	private BufferedImage topLeftStrip = ImageData.topLeftStrip;
	private BufferedImage topRightStrip = ImageData.topRightStrip;
	private BufferedImage bottomLeftStrip = ImageData.bottomLeftStrip;
	private BufferedImage bottomRightStrip = ImageData.bottomRightStrip;

	public BorderImage(int width, int height) {
		this.width = width;
		this.height = height;

		borderImage = new BufferedImage(getWidth(), getHeight(), bottomLeftCorner.getType());
		redraw();
	}

	public void resetSize(int width, int height) {
		this.width = width;
		this.height = height;
		borderImage = new BufferedImage(getWidth(), getHeight(), bottomLeftCorner.getType());
	}
	
	@Override
	public void redraw() {
		Graphics2D g = borderImage.createGraphics();

		g.drawImage(ImageData.topLeftCorner, 0, 0, null);
		g.drawImage(ImageData.topRightCorner, getWidth() - topRightCorner.getWidth(), 0, null);
		g.drawImage(ImageData.bottomLeftCorner, 0, getHeight() - bottomLeftCorner.getHeight(), null);
		g.drawImage(ImageData.bottomRightCorner, getWidth() - bottomRightCorner.getWidth(), getHeight() - bottomRightCorner.getHeight(), null);

		for (int i = topLeftCorner.getWidth(); i < getWidth() - topRightCorner.getWidth(); i++) {
			g.drawImage(topStrip, i, 0, null);
			g.drawImage(middleStrip, i, topLeftCorner.getHeight() + infoPanelHeight, null);
			g.drawImage(bottomStrip, i, getHeight() - bottomRightCorner.getHeight(), null);
		}

		for (int y = 0; y < infoPanelHeight; y++) {
			g.drawImage(topLeftStrip, 0, y + topLeftCorner.getHeight(), null);
			g.drawImage(topRightStrip, getWidth() - topRightCorner.getWidth(), y + topRightCorner.getHeight(), null);
		}
		
		g.drawImage(leftMiddle, 0, topRightCorner.getHeight() + infoPanelHeight, null);
		g.drawImage(rightMiddle, getWidth() - rightMiddle.getWidth(), topRightCorner.getHeight() + infoPanelHeight, null);
		
		for (int y = 0; y < height * ImageData.squareSize; y++) {
			g.drawImage(bottomLeftStrip, 0, y + topLeftCorner.getHeight() + infoPanelHeight + leftMiddle.getHeight(), null);
			g.drawImage(bottomRightStrip, getWidth() - topRightCorner.getWidth(), y + topLeftCorner.getHeight() + infoPanelHeight + leftMiddle.getHeight(), null);
		}
		
		g.setColor(ImageData.backgroundColor);
		g.fillRect(topRightCorner.getWidth(), topRightCorner.getHeight(), getWidth() - 2 * topRightCorner.getWidth(), infoPanelHeight);
	}
	
	@Override
	public void reskin() {
		topLeftCorner = ImageData.topLeftCorner;
		topRightCorner = ImageData.topRightCorner;
		bottomLeftCorner = ImageData.bottomLeftCorner;
		bottomRightCorner = ImageData.bottomRightCorner;

		leftMiddle = ImageData.leftMiddle;
		rightMiddle = ImageData.rightMiddle;

		topStrip = ImageData.topStrip;
		middleStrip = ImageData.middleStrip;
		bottomStrip = ImageData.bottomStrip;

		topLeftStrip = ImageData.topLeftStrip;
		topRightStrip = ImageData.topRightStrip;
		bottomLeftStrip = ImageData.bottomLeftStrip;
		bottomRightStrip = ImageData.bottomRightStrip;
	}

	@Override
	public BufferedImage getImage() {
		return borderImage;
	}

	public int getWidth() {
		return width * ImageData.squareSize + 2 * ImageData.borderComponentWidth;
	}

	public int getHeight() {
		return topLeftCorner.getHeight() + infoPanelHeight + leftMiddle.getHeight() + height * ImageData.squareSize + bottomRightCorner.getHeight();
	}
	
	public int getInfoPanelHeight() {
		return infoPanelHeight;
	}
}