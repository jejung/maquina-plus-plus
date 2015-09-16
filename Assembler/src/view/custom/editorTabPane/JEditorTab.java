package view.custom.editorTabPane;

import java.awt.BorderLayout;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import util.EntireFileReader;
import view.custom.messages.MessageUtils;

/**
 * A M+++ Program editor that holds all the styles to keywords 
 * and give some methods to facilitate the development of this 
 * IDE. 
 * 
 * @author Jean Jung.
 */
public class JEditorTab 
	extends JPanel {
	
	/**
	 * JDK 1.1 serialVersionUID.
	 */
	private static final long serialVersionUID = -3319867634410077075L;
	private JProgramEditor editor;
	private File file;

	/**
	 * Construct an empty program editor.
	 */
	public JEditorTab() {
		this.setLayout(new BorderLayout());
		this.setEditor(new JProgramEditor());
		this.createEditor();
	}

	/**
	 * Construct the program editor and read the file contents on it.
	 * 
	 * @param file The file that have the source to be read.
	 */
	public JEditorTab(File file) {
		this();
		this.setFile(file);
		this.loadFileContent();
		this.createEditor();
	}
	
	/**
	 * Delete the associated file and its compilations generation.
	 * @return {@code true} if success, {@code false} otherwise.
	 */
	public boolean deleteFile() {
	
		File f = this.getFile();
		
		if (f != null) {
			
			// deletes the generated source file first, if exists.
			File src = new File(f.getAbsolutePath().replaceAll(".mmmf", ".mmmp"));
			
			if (src.exists())
				src.delete();
			
			return f.delete();
		}
			
		return false;
	}
	
	/**
	 * Creates and place the editor. Some common operations are make here, like 
	 * the call to {@link JProgramEditor#startSavingEdits()} to prevent that the original content
	 * will not be undone by the {@link ProgramUndoManager}.
	 */
	private void createEditor() {
		
		JProgramEditor editor = this.getEditor();
		editor.startSavingEdits();
		JScrollPane inner = new JScrollPane(editor);
		this.add(inner, BorderLayout.CENTER);
	}
	
	/**
	 * Save the content of the editor into the file associated with this tab, if any, otherwise nothing will be done.
	 */
	public void saveToFile() {
		
		File f = this.getFile();
		
		try {
		
			if (!f.exists()) 
				f.createNewFile();
			
			FileWriter fw = new FileWriter(this.getFile(), false);
			fw.write(this.getEditor().getText().trim());
			fw.close();
			
		} catch (IOException e) {
			
			e.printStackTrace();
			MessageUtils.showErrorMessage("Problemas ao salvar o arquivo: " + e.getMessage());
		}
	}
	
	/**
	 * Load the content of the file associated with this tab, if any, otherwise an empty text
	 * will be set on the editor.
	 */
	public void loadFileContent() {
		
		String fileContent = this.readFile();
		
		if (fileContent == null)
			fileContent = "";
		
		this.getEditor().setText(fileContent);
	}
	
	/**
	 * Check if the text on the editor is the of the file;
	 * @return
	 */
	public boolean checkSaved() {
		
		String fileContent = this.readFile();
		
		if (fileContent != null)
			return fileContent.equals(this.getEditor().getText().intern());
		
		return false;
	}
	
	/**
	 * Get the text that are in the file associated with this tab, if any, otherwise {@code null} is returned.
	 * @return
	 */
	public String readFile() {
		
		File f = this.getFile();
		
		if (f != null && f.isFile() && f.exists() && f.canRead()) {
			
			try {
				
				@SuppressWarnings("resource")// closed inside method
				EntireFileReader efr = new EntireFileReader(f);
				
				return efr.readFileContent();
				
			} catch (IOException e) {
				
				e.printStackTrace();
				MessageUtils.showErrorMessage("Erro inesperado ao tentar ler arquivo: " + this.getFile().getAbsolutePath());
			}
		}
		
		return null;
	}

	/**
	 * @return the editor
	 */
	public JProgramEditor getEditor() {
		return editor;
	}

	/**
	 * @param editor the editor to set
	 */
	public void setEditor(JProgramEditor editor) {
		this.editor = editor;
	}

	/**
	 * @return the serialVersionUID
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	 * @return the file
	 */
	public File getFile() {
		return file;
	}

	/**
	 * @param file the file to set
	 */
	public void setFile(File file) {
		this.file = file;
	}
}
