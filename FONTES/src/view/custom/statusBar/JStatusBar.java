package view.custom.statusBar;

import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

/**
 * A Status Bar simple representation used to display messages to the user.
 * @author jean
 *
 */
public class JStatusBar extends JPanel {

	/**
	 * JDK 1.1 defaultSerialVersionUID.
	 */
	private static final long serialVersionUID = -2150716298725283536L;

	/**
	 * Stores the label to show the messages.
	 */
	private JLabel lblMessage;

	/**
	 * Creates the Status Bar
	 */
	public JStatusBar() {
		super();
		
		this.setLblMessage(new JLabel("", JLabel.LEADING));
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.setBorder(new BevelBorder(BevelBorder.LOWERED));
		this.add(this.getLblMessage());
	}

	/**
	 * Sets the message shown on this component.
	 * @param txt
	 */
	public void setMessage(String txt) {
		
		this.getLblMessage().setText(txt);
	}
	
	/**
	 * @return The label that stores the messages.
	 */
	public JLabel getLblMessage() {
		return lblMessage;
	}

	/**
	 * Sets the label that stores the messages.
	 * @param lblMessage
	 */
	public void setLblMessage(JLabel lblMessage) {
		this.lblMessage = lblMessage;
	}
}
