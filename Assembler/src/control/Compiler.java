package control;

import java.io.File;
import java.io.IOException;
import java.util.List;

import util.EntireFileReader;

import model.LexicalError;
import model.Lexico;
import model.SemanticError;
import model.Semantico;
import model.Sintatico;
import model.SyntaticError;

/**
 * Compiler of M+++ programs source.
 * 
 * @author Jean Jung 
 *
 */
public class Compiler {
	
	private Lexico lexico;
	private Semantico semantico;
	private Sintatico sintatico;
	
	/**
	 * Creates the normal compiler.
	 */
	public Compiler() {
		super();
	}
	
	/**
	 * Compile the code and return the stack of commands.
	 * 
	 * @param txt
	 * @return
	 * @throws SemanticError 
	 * @throws SyntaticError 
	 * @throws LexicalError 
	 * @throws IOException 
	 * @throws AnalysisException
	 */
	public List<Integer> compile(File file) 
			throws LexicalError, SyntaticError, SemanticError, IOException 
	{
		
		String txt = ""; 
		
		@SuppressWarnings("resource") // closed inside readFileContent
		EntireFileReader efr = new EntireFileReader(file);
		
		txt = efr.readFileContent();
		
		this.setSintatico(new Sintatico());
		this.setSemantico(new Semantico());
		this.setLexico(new Lexico(txt));
		
		this.getSintatico().parse(this.getLexico(), this.getSemantico());
		
		return this.getSemantico().getStack();
	}

	/**
	 * @return the lexical analyzer
	 */
	public Lexico getLexico() {
		return lexico;
	}

	/**
	 * @param lexico the lexical analyzer to set
	 */
	public void setLexico(Lexico lexico) {
		this.lexico = lexico;
	}

	/**
	 * @return the semantic analyzer
	 */
	public Semantico getSemantico() {
		return semantico;
	}

	/**
	 * @param semantico the semantic analyzer to set
	 */
	public void setSemantico(Semantico semantico) {
		this.semantico = semantico;
	}

	/**
	 * @return the sintatic analyzer
	 */
	public Sintatico getSintatico() {
		return sintatico;
	}

	/**
	 * @param sintatico the sintatic analyzer to set
	 */
	public void setSintatico(Sintatico sintatico) {
		this.sintatico = sintatico;
	}
}
