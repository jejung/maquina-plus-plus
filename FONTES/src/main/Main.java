package main;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

import view.IDEScreen;
import view.InitialConfigScreen;

public class Main {

	/**
	 * @param args
	 * @throws UnsupportedLookAndFeelException 
	 */
	public static void main(String[] args) throws UnsupportedLookAndFeelException {
		
		
		// A better LookAndFeel.
		UIManager.setLookAndFeel(new NimbusLookAndFeel());
		
		InitialConfigScreen config = new InitialConfigScreen();
		
		config.pack();
		config.setLocationRelativeTo(null);
		config.setVisible(true);
		
		IDEScreen view = new IDEScreen();
		view.setWorkSpace(config.getWorkSpace());
		view.pack();
		view.setLocationRelativeTo(null);
		view.setVisible(true);
	}

}
