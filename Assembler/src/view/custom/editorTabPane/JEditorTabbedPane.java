/**
 * 
 */
package view.custom.editorTabPane;

import java.io.File;

import javax.swing.JTabbedPane;

/**
 * Tabbedpane to organize the files in tab placement.
 * 
 * @author Jean Jung
 */
public class JEditorTabbedPane extends JTabbedPane {

	/**
	 * JDK 1.1 serialVersionUID
	 */
	private static final long serialVersionUID = -7247401375338420088L;

	/**
	 * Default constructor.
	 */
	public JEditorTabbedPane() {
	}
	
	/**
	 * Delete the file of the selected tab. It will be deleted from disk and cannot be undone. 
	 */
	public void deleteSelectedTab() {
		
		JEditorTab tab = (JEditorTab) this.getSelectedComponent();
		
		if (tab.deleteFile()) {
			
			this.removeTabAt(this.getSelectedIndex());
		}
	}
	
	/**
	 * Closes the selected tab.
	 */
	public void closeSelectedTab() {
		int i = this.getSelectedIndex();
		
		if (i > -1)
			this.removeTabAt(i);
	}
	
	/**
	 * Save the content of selected tab to its associated file.
	 */
	public void saveToFile() {
		
		JEditorTab tab = (JEditorTab) this.getSelectedComponent();
		
		if (tab != null)
			tab.saveToFile();
		
		this.changeTabTitle();
	}
	
	/**
	 * Alternate between with "*" and without "*" in the name of the tab when he is saved.
	 */
	private void changeTabTitle() {
		
		JEditorTab tab = (JEditorTab) this.getSelectedComponent();
		File file = tab.getFile();
		
		int index = this.getSelectedIndex();
		
		if (tab.checkSaved()) {
			
			this.setTitleAt(index, file.getName());
			
		} else {
			
			this.setTitleAt(index, "*" + file.getName());
		}
	}
	
	/**
	 * Check if all the tabs are saved.
	 * @return {@code true} if all are saved, {@code false} otherwise.
	 */
	public boolean checkAllSaved() {
		
		int c = this.getTabCount();
		
		for (int i = 0; i < c; i++) {
			
			JEditorTab tab = (JEditorTab) this.getComponentAt(i);
			
			if (tab != null && !tab.checkSaved())
				return false;
		}
		
		return true;
	}
	
	/**
	 * Adds a new tab that will have a file referenced with him.
	 * @param file the file to make the reference.
	 */
	public void addTab(File file) {
		
		JEditorTab newTab = new JEditorTab(file);
		newTab.getEditor().addEditorListener(new EditorListener() {
			
			@Override
			public void editorSaved(EditorEvent evt) {
				changeTabTitle();
			}
			
			@Override
			public void editorChanged(EditorEvent evt) {
				changeTabTitle();
			}
		});
		
		this.add(file.getName(), newTab);
		this.setSelectedComponent(newTab);
		this.changeTabTitle();
	}
	
	/**
	 * Check out the index of tab that retains the given file 
	 * @param file
	 * @return The index of the tab if found, otherwise return -1 if any tab have this file associated.
	 */
	public int indexOfTab(File file) {
		
		int c = this.getTabCount();
		
		for (int i = 0; i < c ; i++) {
			
			JEditorTab tab = (JEditorTab) this.getComponentAt(i);
			if (tab != null && file.equals(tab.getFile()))
				return i; 
		}
		
		return -1;
	}
}
