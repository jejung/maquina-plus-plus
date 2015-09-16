package view.custom.editorTabPane;

import java.awt.event.ActionEvent;

/**
 * 
 * @author Jean
 *
 */
public class EditorEvent extends ActionEvent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2293496359280976103L;

	/**
	 * 
	 * @param source
	 * @param id
	 * @param command
	 */
	public EditorEvent(Object source, int id, String command) {
		super(source, id, command);
	}
}
