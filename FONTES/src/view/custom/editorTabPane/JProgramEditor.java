/**
 * 
 */
package view.custom.editorTabPane;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

import model.ScannerConstants;

/**
 * An assembly program editor that highlights the reserved key words, memory addresses and hexadecimal values.  
 * 
 * @author Jean Jung
 */
public class JProgramEditor extends JTextPane {

	/**
	 *  JDK 1.1 serialVersionUID
	 */
	private static final long serialVersionUID = -5590642064096732270L;
	/**
	 * A table with the styles that have to be applied on the text.
	 */
	private StyleRuleTable ruleTable;
	/**
	 * A list with the listeners of this editor. 
	 */
	private List<EditorListener> listeners;
	/**
	 * The undo manager that is used to undo and redo actions.
	 */
	private ProgramUndoManager undoManager;
	/**
	 * The color of commented lines.
	 */
	private static final Color COMMENTARY_COLOR = new Color(143,188,143); // sea green
	/**
	 * The regex to match commented lines.  
	 */
	private static final String COMMENTARY_REGEX = "(?:/\\*(?:[^*]|(?:\\*+[^*/]))*\\*+/)|(?://.*)";
	/**
	 * The color of ROM addresses on the font.
	 */
	private static final Color ROM_ADDRESS_COLOR = new Color(128,128,0); // olive
	/**
	 * The regex to match ROM addresses.  
	 */
	private static final String ROM_ADDRESS_REGEX = "#[0-9A-F][0-9A-F][0-9A-F][0-9A-F]";
	/**
	 * The color of RAM addresses on the font.
	 */
	private static final Color RAM_ADDRESS_COLOR = new Color(0, 149, 182); // ligth blue
	/**
	 * The regex to match RAM addresses.  
	 */
	private static final String STATIC_RAM_ADDRESS = "#[0-9A-F][0-9A-F]";
	/**
	 * The regex to match dynamic RAM addresses
	 */
	private static final String DYNAMIC_RAM_ADDRESS = "#[A-E]";
	/**
	 * The color of hexa values.
	 */
	private static final Color HEXA_VALUE_COLOR = new Color(139,69,19); // sadle brown
	/**
	 * The regex to match hexa values.
	 */
	private static final String HEXA_VALUE_REGEX = "[0-9A-F][0-9A-F][\\W\\s]";
	/**
	 * The color of special keys on the font.
	 */
	private static final Color SPECIAL_CASES_COLOR = new Color(78, 42, 90); //purple
	
	/**
	 * Creates a default M+++ program editor.
	 */
	public JProgramEditor() {
		this.loadDeaultRuleTable();
		this.createListeners();
	}
	
	/**
	 * Create and inner the listeners used to control the style application and change features.
	 */
	private void createListeners() {
		
		this.setListeners(new ArrayList<EditorListener>());
		
		this.getStyledDocument().addDocumentListener(new DocumentListener() {
			
			private final Runnable applyer = new Runnable() {
				
												@Override
												public void run() {
													try {
														applyRules();
													} catch (BadLocationException e) {
														e.printStackTrace();
													}
												}
											};
			public void change() {
				SwingUtilities.invokeLater(applyer);
				fireEditorChanged();
			}
											
			@Override
			public void removeUpdate(DocumentEvent e) {
				this.change();
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				this.change();
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
			}
		});
	}
	
	/**
	 * Says to all listeners that this editor suffered an change.
	 */
	private void fireEditorChanged() {
		
		EditorEvent evt = new EditorEvent(this, 0, "changed"); 
		
		for (EditorListener listener : this.getListeners()) {
			
			listener.editorChanged(evt);
		}
	}
	
	/**
	 * Apply the rules currently on the {@link JProgramEditor#ruleTable} to the text on the editor.
	 * All the styles will be set where they {@link StyleRule} indicate.
	 * @throws BadLocationException when something was wrong on this method. If this exception is raised the
	 * we sure make anything wrong.	
	 */
	public void applyRules() 
			throws BadLocationException {
		
		StyledDocument doc = this.getStyledDocument();
		
		Style s = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);
		
		String txt = doc.getText(0, doc.getLength());
		doc.setCharacterAttributes(0, doc.getLength(), s, true);
		
		for (StyleRule rule : this.getRuleTable()) {
			
			rule.setTarget(txt);
			
			while (rule.applyRule()) {
				
				doc.setCharacterAttributes(rule.getBegin(), rule.getEnd() - rule.getBegin(), rule.getStyle(), true);
			}
		}
	}
	
	/**
	 * Deletes the text, the same of if the user pressed the delete key.
	 * @throws BadLocationException When something was wrong.
	 */
	public void delete() throws BadLocationException {
		
		Caret caret = this.getCaret();
		
		int start = caret.getDot();
		int end = start + 1;
		
		if (caret.getMark() != start)
			end = caret.getMark();
		
		this.getStyledDocument().remove(start, end);
	}
	
	/**
	 * Loads the default {@link StyleRule} with basics {@link StyleRule}s inside.
	 */
	private void loadDeaultRuleTable() {
		
		this.setRuleTable(new StyleRuleTable());
		
		Style def = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);
		
		Style reg = this.addStyle("regular", def);
		StyleConstants.setFontFamily(reg, "SansSerif");
		
		Style s = this.addStyle("hexa value", reg);
		StyleConstants.setForeground(s, HEXA_VALUE_COLOR);
		this.addRule(new StyleRule(HEXA_VALUE_REGEX, s));
		
		s = this.addStyle("ram", reg);
		StyleConstants.setForeground(s, RAM_ADDRESS_COLOR);
		this.addRule(new StyleRule(STATIC_RAM_ADDRESS, s));
		this.addRule(new StyleRule(DYNAMIC_RAM_ADDRESS, s));
		
		s = this.addStyle("rom", reg);
		StyleConstants.setForeground(s, ROM_ADDRESS_COLOR); 
		this.addRule(new StyleRule(ROM_ADDRESS_REGEX, s));
		
		s = this.addStyle("comment", reg);
		StyleConstants.setForeground(s, COMMENTARY_COLOR); 
		this.addRule(new StyleRule(COMMENTARY_REGEX, s));
		
		s = this.addStyle("keys", reg);
		StyleConstants.setBold(s, true);
		StyleConstants.setForeground(s, SPECIAL_CASES_COLOR);
		this.addRule(new StyleRule(specialCasesRegex(), s));
	}
	
	/**
	 * Creates a regular expression with the keyWords of the assembly language commands 
	 * supported by M+++.
	 * @return the regex.
	 */
	private String specialCasesRegex() {
		
		String regex = "";
		
		for (String key : ScannerConstants.SPECIAL_CASES_KEYS) {
			
			if (regex.isEmpty()){
				
				regex = key;
			}else {
				
				regex += "|" + key + "\\W";
			}
		}
		
		return regex;
	}
	
	/**
	 * Verify if this editor can undo the last edit.
	 * @return {@code true} if can undo, {@code false} otherwise.
	 */
	public boolean canUndo() {
		
		return this.getUndoManager().canUndo();
	}
	
	/**
	 * Undo the last edit.
	 * @return {@code true} if success.
	 */
	public boolean undo() {
		
		if (!this.canUndo()) 
			return false; 
		
		this.getUndoManager().undo();
		
		return true;
	}
	
	/**
	 * Verify if this editor can redo the last undo.
	 * @return {@code true} if can redo, {@code false} otherwise.
	 */
	public boolean canRedo() {
		
		return this.getUndoManager().canRedo();
	}
	
	/**
	 * Redo the last undo.
	 * @return {@code true} if success.
	 */
	public boolean redo() {
		
		if (!this.canRedo()) 
			return false;
		
		this.getUndoManager().redo();
		
		return true;
	}
	
	/**
	 * Says to this editor that he must save the last edit.
	 */
	public void startSavingEdits() {
		
		this.setUndoManager(new ProgramUndoManager());
		this.getStyledDocument().addUndoableEditListener(this.getUndoManager());
	}
	
	/**
	 * Says to this editor that he can not save the last edit.
	 */
	public void stopSavingEdits() {
		
		this.getStyledDocument().removeUndoableEditListener(this.getUndoManager());
		this.setUndoManager(null);
	}
	
	/**
	 * Remove this rule from the rule, to stop styling the text to this rule.
	 * @param rule
	 */
	public void removeRule(StyleRule rule) {
		
		this.getRuleTable().remove(rule);
	}
	
	/**
	 * Add a rule for style the text according with this rule.
	 * @param rule
	 */
	public void addRule(StyleRule rule) {
		
		this.getRuleTable().add(rule);
	}
	
	/**
	 * Remove the given listener of this listeners.
	 * @param listener
	 */
	public void removeEditorListener(EditorListener listener) {
		
		this.getListeners().remove(listener);
	}
	
	/**
	 * Add a listener to editor updates.
	 * @param listener
	 */
	public void addEditorListener(EditorListener listener) {
		
		this.getListeners().add(listener);
	}

	/**
	 * @return the ruleTable
	 */
	public StyleRuleTable getRuleTable() {
		return ruleTable;
	}

	/**
	 * @param ruleTable the ruleTable to set
	 */
	public void setRuleTable(StyleRuleTable ruleTable) {
		this.ruleTable = ruleTable;
	}

	/**
	 * @return the listeners
	 */
	public List<EditorListener> getListeners() {
		return listeners;
	}

	/**
	 * @param listeners the listeners to set
	 */
	public void setListeners(List<EditorListener> listeners) {
		this.listeners = listeners;
	}

	/**
	 * @return the undoManager
	 */
	public ProgramUndoManager getUndoManager() {
		return undoManager;
	}

	/**
	 * @param undoManager the undoManager to set
	 */
	public void setUndoManager(ProgramUndoManager undoManager) {
		this.undoManager = undoManager;
	}

	/**
	 * @return the serialVersionUID
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
