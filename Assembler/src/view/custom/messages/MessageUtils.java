package view.custom.messages;

import javax.swing.JOptionPane;

public class MessageUtils {
	
	/**
	 * Shows an question message to grab an value of the user.
	 * 
	 * @param message
	 * @return the object with the user input.
	 */
	public static Object showInputDialog(String message) {
		
		return JOptionPane.showInputDialog(null, message, "Entrada", JOptionPane.QUESTION_MESSAGE);
	}
	
	/**
	 * Shows to user an warning that indicates anything is wrong and let him a choice.
	 * @param message
	 * @return the user option choice.
	 */
	public static int showConfirmWarning(String message) {
		
		return JOptionPane.showConfirmDialog(null, message, "Aviso", JOptionPane.YES_NO_OPTION);
	}
	
	/**
	 * Shows an question message and return the value that user input converted to an String.
	 * Same of: <br> 
	 * {@code (String) MessageUtils.showInputDialog("Input any value");}
	 * 
	 * @param message
	 * @return the user input converted to string.
	 */
	public static String showInputDialogAsString(String message) {
		
		Object input = MessageUtils.showInputDialog(message);
		
		return input == null ? "" : input.toString();
	}

	/**
	 * Shows an error message on the screen to let the user know if anything was wrong.
	 * @param message
	 */
	public static void showErrorMessage(String message) {
		
		JOptionPane.showMessageDialog(null, message, "Erro", JOptionPane.ERROR_MESSAGE);
	}
}
