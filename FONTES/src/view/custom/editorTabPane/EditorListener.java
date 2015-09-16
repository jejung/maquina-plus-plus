/**
 * 
 */
package view.custom.editorTabPane;

/**
 * 
 * @author Jean Jung
 */
public interface EditorListener {
	
	public void editorChanged(EditorEvent evt);
	
	public void editorSaved(EditorEvent evt);
}
