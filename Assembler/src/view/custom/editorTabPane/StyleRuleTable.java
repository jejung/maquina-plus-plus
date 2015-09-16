package view.custom.editorTabPane;

import java.util.ArrayList;

import javax.swing.text.BadLocationException;

/**
 * A table of {@link StyleRule} to be applied on a {@link JProgramEditor}. 
 * 
 * @author jean
 *
 */
public class StyleRuleTable extends ArrayList<StyleRule> {

	/**
	 * JDK 1.1 serialVersionUID
	 */
	private static final long serialVersionUID = -6190166810060196913L;
	
	/**
	 * The text to be scanned.
	 */
	private String txt;
	/**
	 * The last applied rule.
	 */
	private StyleRule rule;
	
	/**
	 * Prepares this {@link StyleRuleTable} to scan the given text.
	 * @param txt
	 */
	public void prepare(String txt) {
		
		this.txt = txt;
		this.rule = null;
	}
	
	/**
	 * Verifies if a {@link StyleRule } is applied to the given text.
	 * If exists, the it will be stored on rule property and can be obtained
	 * on {@link StyleRuleTable#getRule()} method.
	 * @return True if exists a {@link StyleRule} applied to the text, false otherwise
	 * @throws BadLocationException
	 */
	public boolean hasNextStyle() 
			throws BadLocationException {
	
		
		StyleRule[] rules = this.toArray(new StyleRule[] {});
		StyleRule minRule = null;
			
		// Find the first rule that is applied to this text
		for (StyleRule rule : rules) {
			
			rule.setTarget(this.txt);
			
			if (rule.applyRule()) {
				
				if (minRule == null) 
					minRule = rule;
				else if (minRule.getBegin() > rule.getBegin())
					minRule = rule;
			}
		}
		
		if (minRule != null) {
			
			this.txt = this.txt.substring(minRule.getEnd());
			this.rule = minRule;
		}
		
		return minRule != null;
	}

	/**
	 * 
	 * @return The last rule applied to the text.
	 */
	public StyleRule getRule() {
		return rule;
	}
}
