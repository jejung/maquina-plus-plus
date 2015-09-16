/**
 * 
 */
package view.custom.editorTabPane;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.text.Style;

/**
 * Regra para gerar estilos conforme a regex passada. Utilizado no editor.
 * 
 * @author Jean Jung
 */
public class StyleRule implements Serializable {

	/**
	 * JDK 1.1 serialVersionUID
	 */
	private static final long serialVersionUID = 3652922291965915813L;
	private String rule;
	private Style style;
	private Pattern pattern;
	private Matcher matcher;
	private String target;
	private int begin;
	private int end;
	
	/**
	 * Cria uma regra com a regra(regex) passada.
	 * @param rule
	 * @param style
	 */
	public StyleRule(String rule, Style style) {
		super();
		this.rule = rule;
		this.style = style;
		this.setPattern(Pattern.compile(rule));
	}
	
	/**
	 * Cria uma regra com a regra(regex) passada.
	 * @param rule
	 * @param style
	 */
	public StyleRule(String rule, Style style, String target ) {
		this(rule,style);
		this.target = target;
	}
	
	/**
	 * Aplica a regra para definir o estilo do editor e onde ele come�a e termina.
	 * @return
	 */
	public boolean applyRule() {
		
		if (this.getTarget() != null) {
				
			if (this.getMatcher() == null) 
				this.loadMatcher();
			
			if (this.getMatcher().find()) {
				
				this.begin = this.getMatcher().start();
				this.end = this.getMatcher().end();
				return true;
				
			} else 
				this.setMatcher(null);
		}
		
		return false; 
	}
	
	/**
	 * Loads the matcher for the actual content
	 */
	private void loadMatcher() {
		
		this.setMatcher(this.getPattern().matcher(this.getTarget()));
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
		this.loadMatcher();
	}

	public int getBegin() {
		return begin;
	}

	public int getEnd() {
		return end;
	}

	public Pattern getPattern() {
		return pattern;
	}

	public void setPattern(Pattern pattern) {
		this.pattern = pattern;
	}

	public Matcher getMatcher() {
		return matcher;
	}

	public void setMatcher(Matcher matcher) {
		this.matcher = matcher;
	}

	public String getRule() {
		return rule;
	}

	public void setRule(String rule) {
		this.rule = rule;
	}

	public Style getStyle() {
		return style;
	}

	public void setStyle(Style style) {
		this.style = style;
	}
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
