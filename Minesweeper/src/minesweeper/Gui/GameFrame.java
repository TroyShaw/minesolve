package minesweeper.Gui;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.UIManager;

import minesweeper.controller.GuiController;
import minesweeper.game.GameConstants;
import minesweeper.images.ImageData;

public class GameFrame extends JFrame {
	
	private GamePanel gamePanel;

	private MenuBar menuBar;
	
	private GuiController controller;

	public GameFrame() {
		super("Troysweeper");
		setNativeLAndF();	//needs to be called first to have any effect
		setIcon();

		//load images first so components have images to use
		ImageData.loadDefaultImages();

		//init objects
		initializeMenuBar();
		initializeComponents();
		initializeController();
		
		//add objects to this frame
		setupLayout();

		//initial draw

		
		//house keeping
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		pack();
		center();
		setVisible(true);
	}

	
	private void initializeMenuBar() {
		menuBar = new MenuBar();
		setJMenuBar(menuBar);
	}

	private void initializeComponents() {
		gamePanel = new GamePanel(GameConstants.EASY_WIDTH, GameConstants.EASY_HEIGHT, GameConstants.EASY_MINES);
	}
	
	private void initializeController() {
		controller = new GuiController(this, gamePanel);
		controller.registerMainImage(gamePanel.getMainImage());
		controller.registerBoardImage(gamePanel.getBoardImage());
		controller.registerButtonImage(gamePanel.getButtonImage());
		
		gamePanel.getMainImage().getBoardImage().registerController(controller);
		gamePanel.getMainImage().getButtonImage().registerController(controller);
		
		menuBar.registerController(controller);
		
		//after setting up relations, start a new game
		controller.newGame();
	}

	private void setupLayout() {
		//getContentPane().setLayout(null);
//		JPanel masterPanel = new JPanel();
//		masterPanel.setLayout(new BoxLayout(masterPanel, BoxLayout.Y_AXIS));
		
		//masterPanel.add(gamePanel);
		
		getContentPane().add(gamePanel);
		
		//getContentPane().setPreferredSize(gamePanel.getSize());
	}

	
	/**
	 * Sets the look and feel of the GUI to the current systems Look and feel.
	 */
	private void setNativeLAndF() {
		try {
			// Set System L&F
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			//do nothing. It will default to normal
		}
	}

	/**
	 * Method sets the icon to the minesweeper icon.
	 */
	private void setIcon() {
		Image icon = ImageData.getIconImage();

		setIconImage(icon);
	}

	/**
	 * Method centers the JFrame on the screen.
	 */
	private void center() {
		//set to center of screen
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

		// Determine the new location of the window
		int w = getSize().width;
		int h = getSize().height;
		int x = (dim.width - w) / 2;
		int y = (dim.height - h) / 2;

		// Move the window
		setLocation(x, y);
	}
}