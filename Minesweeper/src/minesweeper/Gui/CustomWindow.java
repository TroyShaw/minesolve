package minesweeper.Gui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FocusTraversalPolicy;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 * Represents the window that appears when the user clicks the custom game menu option. <p>
 * 
 * Opens a window that lets the user select width/ height/ num mines, and then either select or cancel the selection. <p>
 *
 * If the selection is logically invalid (more mines then squares available, etc) no game will be started.
 *
 * @author Troy Shaw
 */
public class CustomWindow extends JDialog {

	private JFrame parent;

	private JTextField width, height, numMines;
	private JLabel widthL, heightL, numMinesL;
	private JButton confirm, cancel;

	private boolean confirmed = false;

	/**
	 * The width entered by the user. Defaults to -1.
	 */
	private int widthInt = -1;
	/**
	 * The height entered by the user. Defaults to -1.
	 */
	private int heightInt = -1;
	
	/**
	 * The number of mines entered by the user. Defaults to -1.
	 */
	private int numMinesInt = -1;

	public CustomWindow(JFrame parent) {
		super(parent, "Custom Field", true);
		
		this.parent = parent;
		
		setFocusTraversalPolicy(new FocusPolicy());

		width = new JTextField(6);
		height = new JTextField(6);
		numMines = new JTextField(6);

		widthL = new JLabel("Width:");
		heightL = new JLabel("Height:");
		numMinesL = new JLabel("Mines:");

		confirm = new JButton("Confirm");
		cancel = new JButton("Cancel");

		this.setLayout(new GridLayout(3, 2, 5, 10));

		add(widthL);
		add(width);
		add(confirm);
		add(heightL);
		add(height);
		add(cancel);
		add(numMinesL);
		add(numMines);

		confirm.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				confirmed = true;
				dispose();
			}
		});

		cancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

		pack();
		center();
		setVisible(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	/**
	 * Utility method to center the window.
	 */
	private void center() {
		Point parentPos = parent.getLocationOnScreen();

		Dimension parentSize = parent.getSize();
		Dimension size = getPreferredSize();

		// point of left corner is "parentPos + (parentSize - thisSize /2)"
		int x = parentPos.x + (parentSize.width - size.width) / 2;
		int y = parentPos.y + (parentSize.height - size.height) / 2;

		setLocation(x,y);
	}

	/**
	 * Returns true if the user clicked confirm to close the dialog. <p>
	 * If the user clicked cancel, clicked the 'x', or by any other means, this will be false.
	 * 
	 * @return true if they clicked <i>confirm</i>, false otherwise
	 */
	public boolean confirmed() {
		return confirmed;
	}

	/**
	 * Validates the data entered by the user as integers. <p>
	 * This will return true iff all values entered by the user are integers. <p>
	 * @return
	 */
	public boolean validateConfirm() {
		try {
			widthInt = Integer.parseInt(width.getText());
			heightInt = Integer.parseInt(height.getText());
			numMinesInt = Integer.parseInt(numMines.getText());

			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	/**
	 * Returns the board width entered by the user. <p>
	 * If the user cancelled, this will be -1.
	 * @return the boards width, or -1
	 */
	public int getBoardWidth() {
		return widthInt;
	}

	/**
	 * Returns the board height entered by the user. <p>
	 * If the user cancelled, this will be -1.
	 * @return the boards height, or -1
	 */
	public int getBoardHeight() {
		return heightInt;
	}

	/**
	 * Returns the number of mines entered by the user. <p>
	 * If the user cancelled, this will be -1.
	 * @return the number of mines, or -1
	 */
	public int getNumMines() {
		return numMinesInt;
	}
	
	/**
	 * Custom class to enforce focus rules for use of tabbing. <p>
	 * 
	 * Focus is in cyclic order <i>[width, height, mines, confirm, cancel]</i>.
	 *
	 * @author Troy Shaw
	 */
	private class FocusPolicy extends FocusTraversalPolicy {
		@Override
		public Component getComponentAfter(Container aContainer, Component aComponent) {
			if (aComponent == width) return height;
			else if (aComponent == height) return numMines;
			else if (aComponent == numMines) return confirm;
			else if (aComponent == confirm) return cancel;
			else return width;
		}

		@Override
		public Component getComponentBefore(Container aContainer, Component aComponent) {
			if (aComponent == width) return cancel;
			else if (aComponent == height) return width;
			else if (aComponent == numMines) return height;
			else if (aComponent == confirm) return numMines;
			else return confirm;
		}

		@Override
		public Component getFirstComponent(Container aContainer) {
			return width;
		}

		@Override
		public Component getLastComponent(Container aContainer) {
			return cancel;
		}

		@Override
		public Component getDefaultComponent(Container aContainer) {
			return width;
		}
	}
}