package minesweeper.cui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

import minesweeper.Gui.gameImages.BoardImage;
import minesweeper.Gui.gameImages.ButtonImage;
import minesweeper.Gui.gameImages.MainImage;

public class GamePanel extends JPanel {

	private MainImage mainImage;
	
	public GamePanel(int mineX, int mineY, int numMines) {
		mainImage = new MainImage(this, mineX, mineY, numMines);
	
		initMouseListener();
		
		Dimension d = new Dimension(mainImage.getWidth(), mainImage.getHeight());
		setPreferredSize(d);
		setSize(d);
		setMinimumSize(d);
		setMaximumSize(d);
	}
	
	private void initMouseListener() {
		MouseAdapter a = new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
				mainImage.mousePressed(e);
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				mainImage.mouseReleased(e);
			}

			@Override
			public void mouseDragged(MouseEvent e) {
				mainImage.mouseDragged(e);
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				mainImage.mouseMoved(e);
			}
		};
		
		addMouseListener(a);
		addMouseMotionListener(a);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		
		g2d.drawImage(mainImage.getImage(), 0, 0, null);
	}
	
	public MainImage getMainImage() {
		return mainImage;
	}
	
	public BoardImage getBoardImage() {
		return mainImage.getBoardImage();
	}
	
	public ButtonImage getButtonImage() {
		return mainImage.getButtonImage();
	}
}
