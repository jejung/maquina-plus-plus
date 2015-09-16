/**
 * 
 */
package view.custom.editorTabPane;

import javax.swing.event.UndoableEditEvent;
import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEdit;

/**
 * A undoManage to assembly programs. This is like any other common undoManager but 
 * this ignores the style alterations. At the time, this undo manager only works with pt_br locale.
 * A general undoManager structure is needed to make this works in both, since {@link UndoableEdit} 
 * to this class. This take a long time and was defined that don't will be done till this program is 
 * being used on other countries. 
 * 
 * @author Jean Jung
 * 
 */
public class ProgramUndoManager extends UndoManager {

	/**
	 * JDK 1.1 serialVersionUID
	 */
	private static final long serialVersionUID = 5668019034055537585L;

	/**
	 * Creates a default {@link UndoManager} that ignores style alterations.
	 */
	public ProgramUndoManager() {
	}
	
	@Override
	public void undoableEditHappened(UndoableEditEvent e) {
		
		if (!e.getEdit().getPresentationName().equalsIgnoreCase("alteração de estilo")) {
			
			super.undoableEditHappened(e);
		}
	}
}
