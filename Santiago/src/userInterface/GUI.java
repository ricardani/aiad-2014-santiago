package userInterface;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import logic.Game;
import userInterface.BoardPanel;

public class GUI implements ActionListener {

	Game game;
	private Timer timer;
	//final static JFrame menuFrame = new JFrame("Fanorona");
	static JFrame gameFrame = new JFrame("Fanorona");
	final static JTextField text = new JTextField();
	//final static JButton newGame = new JButton("New Game");

	final static JPanel menu = new JPanel();
	final static String PP_BUTTON = "Player vs Player";
	final static String CP_BUTTON = "Computer vs Player";
	final static String CC_BUTTON = "Computer vs Computer";
	private String clicked;

	public GUI() {
		game = new Game();

	}

	public void addComponentToPane(Container pane) {
	}

	/**
	 * Create the GUI and show it. For thread safety, this method should be
	 * invoked from the event dispatch thread.
	 */
	private void createAndShowGUI() {
		// Create and set up the window.
		gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                
                gameFrame.setLayout(new BorderLayout());

		final JPanel boardPanel = new BoardPanel(clicked);
		boardPanel.setLayout(new GridLayout(5, 9));

		timer = new Timer(100, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				boardPanel.repaint();
			}
		});
		timer.start();
                
		gameFrame.add(boardPanel, BorderLayout.CENTER);
		gameFrame.pack();
		gameFrame.setSize(1200, 800);
		gameFrame.setLocationRelativeTo(null);
		gameFrame.setVisible(true);

	}

	public void switchTurn() {
		

		changeText();

		gameFrame.repaint();
	}

	public void changeText() {
		
	}

	public String getClicked() {
		return clicked;
	}

	public static void start(String[] args) {

		/* Use an appropriate Look and Feel */
		try {
			// UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
		} catch (UnsupportedLookAndFeelException | IllegalAccessException | InstantiationException | ClassNotFoundException ex) {
		}
		/* Turn off metal's use of bold fonts */
		UIManager.put("swing.boldMetal", Boolean.FALSE);

		// Schedule a job for the event dispatch thread:
		// creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				GUI gui = new GUI();
				gui.createAndShowGUI();
			}
		});
	}

    @Override
    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
