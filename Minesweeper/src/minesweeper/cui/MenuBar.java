package minesweeper.cui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.KeyStroke;

import minesweeper.controller.GuiController;
import minesweeper.game.*;

public class MenuBar extends JMenuBar implements ActionListener {

	//communication object
	private GuiController controller;
	
	//state objects
	private JMenu gameMenu, extrasMenu, helpMenu;
	
	private JMenuItem newGame, beginner, intermediate, expert, custom, questioned, exit;
	private JMenuItem stats;
	private JMenuItem defaultSkin, loadSkin, solve;
	private JMenuItem help, about;
	
	/**
	 * Previously selected difficulty setting. Will be either beginner, intermediate, expert, custom.
	 */
	private JMenuItem lastSelected;
	
	
	
	public MenuBar() {
		//instantiate all
		gameMenu = new JMenu("Game");
		extrasMenu = new JMenu("Extras");
		helpMenu = new JMenu("Help");
		
		newGame = new JMenuItem("New Game");
		
		beginner = new JRadioButtonMenuItem("Beginner", true);
		intermediate = new JRadioButtonMenuItem("Intermediate");
		expert = new JRadioButtonMenuItem("Expert");
		custom = new JRadioButtonMenuItem("Custom");
		
		lastSelected = beginner;
		
		questioned = new JCheckBoxMenuItem("Question marks");
		
		exit = new JMenuItem("Exit");
		
		stats = new JMenuItem("Statistics");
		
		defaultSkin = new JMenuItem("Default skin");
		loadSkin = new JMenuItem("Load skin...");
		solve = new JMenuItem("Solve");
		
		help = new JMenuItem("Help");
		about = new JMenuItem("About");
		
		//hotkeys
		newGame.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0));
		solve.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0));
		
		//put jradio into a group
		ButtonGroup g = new ButtonGroup();
		
		g.add(beginner);
		g.add(intermediate);
		g.add(expert);
		g.add(custom);
		
		//add items to menus
		gameMenu.add(newGame);
		gameMenu.addSeparator();
		gameMenu.add(beginner);
		gameMenu.add(intermediate);
		gameMenu.add(expert);
		gameMenu.add(custom);
		gameMenu.addSeparator();
		gameMenu.add(questioned);
		gameMenu.addSeparator();
		gameMenu.add(exit);
		
		extrasMenu.add(stats);
		extrasMenu.addSeparator();
		extrasMenu.add(defaultSkin);
		extrasMenu.add(loadSkin);
		extrasMenu.addSeparator();
		extrasMenu.add(solve);
		
		helpMenu.add(help);
		helpMenu.add(about);
		
		//add listener
		newGame.addActionListener(this);
		beginner.addActionListener(this);
		intermediate.addActionListener(this);
		expert.addActionListener(this);
		custom.addActionListener(this);
		questioned.addActionListener(this);
		exit.addActionListener(this);
		stats.addActionListener(this);
		defaultSkin.addActionListener(this);
		loadSkin.addActionListener(this);
		solve.addActionListener(this);
		help.addActionListener(this);
		about.addActionListener(this);
		
		add(gameMenu);
		add(extrasMenu);
		add(helpMenu);
	}

	/**
	 * Registers the controller with this MenuBar.
	 * @param controller
	 */
	public void registerController(GuiController controller) {
		this.controller = controller;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		
		if (o == newGame) {
			controller.newGame();
		} else if (o == beginner) {
			lastSelected = beginner;
			controller.newGame(GameConstants.EASY_WIDTH, GameConstants.EASY_HEIGHT, GameConstants.EASY_MINES);
		} else if (o == intermediate) {
			lastSelected = intermediate;
			controller.newGame(GameConstants.MEDIUM_WIDTH, GameConstants.MEDIUM_HEIGHT, GameConstants.MEDIUM_MINES);
		} else if (o == expert) {
			lastSelected = expert;
			controller.newGame(GameConstants.HARD_WIDTH, GameConstants.HARD_HEIGHT, GameConstants.HARD_MINES);
		} else if (o == custom) {
			CustomWindow w = new CustomWindow((JFrame) getTopLevelAncestor());
			
			if (w.confirmed() && w.validateConfirm()) {
				lastSelected = custom;
				controller.newGame(w.getBoardWidth(), w.getBoardHeight(), w.getNumMines());
			} else {
				lastSelected.setSelected(true);
			}
		} else if (o == questioned) {
			Minesweeper.questionMode = questioned.isSelected();
		} else if (o == exit) {
			controller.quit();
		} else if (o == stats) {
			controller.displayStats();
		} else if (o == defaultSkin) {
			controller.setDefaultSkin();
		} else if (o == loadSkin) {
			JFileChooser fc = new JFileChooser();

			if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
				controller.loadSkin(fc.getSelectedFile());
			}
		} else if (o == solve) {
			controller.solve();
		} else if (o == help) {
			controller.displayHelp();
		} else if (o == about) {
			controller.displayAbout();
		}
	}
}