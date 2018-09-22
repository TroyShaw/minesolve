package nz.co.troyshaw.minesweeper.gui.gameImages;

import java.awt.image.BufferedImage;

public abstract class GameImage {

	private int x, y;
	
	public abstract void reskin();
	
	public abstract void redraw();
	
	public abstract BufferedImage getImage();
	
	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
}
