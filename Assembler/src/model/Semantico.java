package model;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Spliterator;

public class Semantico implements Constants {

	// Aux bytes set.
	private static final int _FF = 0xFF;
	private static final int _1F = 0x1F;
	private static final int _3F = 0x3F;
	private static final int _20 = 0x20;
	private static final int _5F = 0x5F;
	private static final int _40 = 0x40;
	private static final int _7F = 0x7F;
	private static final int _60 = 0x60;
	private static final int _9F = 0x9F;
	private static final int _80 = 0x80;
	private static final int _BF = 0xBF;
	private static final int _A0 = 0xA0;
	private static final int _DF = 0xDF;
	private static final int _C0 = 0xC0;
	private static final int _E0 = 0xE0;
	private static final int _07 = 0x07;
	private static final int _F8 = 0xF8;
	private static final int _F9 = 0xF9;
	private static final int _01 = 0x01;
	private static final int _FA = 0xFA;
	private static final int _02 = 0x02;
	private static final int _FB = 0xFB;
	private static final int _03 = 0x03;
	private static final int _FC = 0xFC;
	private static final int _04 = 0x04;
	private static final int _FD = 0xFD;
	private static final int _05 = 0x05;
	private static final int _FE = 0xFE;
	private static final int _06 = 0x06;
	private static final int _E7 = 0xE7;
	private static final int _08 = 0x08;
	private static final int _EF = 0xEF;
	private static final int _10 = 0x10;
	private static final int _F7 = 0xF7;
	private static final int _18 = 0x18;

	// Instruction flow set
	// no_hd__with_hd
	private static final int AC_AC__ROM_AC__RET = 11;
	private static final int AC_REG__ROM_REG__DRRAM_AC = 12;
	private static final int AC_RAM__ROM_RAM__AC_DRAM = 13;
	private static final int AC_OUT__JMP__PUSHREG = 14;
	private static final int REG_AC__JMPC__POPREG = 15;
	private static final int RAM_AC__JMPZ__PUSHA = 16;
	private static final int IN_AC__CALL__POPA = 17;
	private static final int DONT_USE = 18;

	// Instruction set
	private static final int FIRST_DUMMY_BYTE = 0xABCD;
	private static final int SECOND_DUMMY_BYTE = 0x16384;
	private static final int ADD = 1;
	private static final int SUB = 2;
	private static final int AND = 3;
	private static final int OR = 4;
	private static final int XOR = 5;
	private static final int NOT = 6;
	private static final int MOV = 7;
	private static final int INC = 8;

	// Action set
	private static final int PROGRAM_START = 50;
	private static final int INSTRUCTION_START = 51;
	private static final int PROGRAM_END = 52;
	private static final int HD = 10;
	private static final int IN_OUT_FLAG = 35;
	private static final int RAM_RECORD = 36;
	private static final int ROM_PREPARE = 37;
	private static final int AUX_RECORD = 38;
	private static final int BYTE_RECORD = 40;
	private static final int REND = 41;
	private static final int LABEL_RECORD = 43;
	private static final int LABEL_TRANSLATE = 42;
	
	// Register binds
	private static final int REG_B = 31;
	private static final int REG_C = 32;
	private static final int REG_D = 33;
	private static final int REG_E = 34;

	// IN/OUT Ports
	private static final String IN0 = "IN0";
	private static final String IN1 = "IN1";
	private static final String IN2 = "IN2";
	private static final String IN3 = "IN3";
	private static final String OUT0 = "OUT0";
	private static final String OUT1 = "OUT1";
	private static final String OUT2 = "OUT2";
	private static final String OUT3 = "OUT3";
	
	// Java helpers
	private static final String HEXA_PREFIX = "0x";

	private Stack stack;
	private HashMap<String, Integer> labels;
	private HashMap<String, List<Integer>> labelsBuffer;
	private int stackIndex;
	private int generatedByte;
	private int generatedByteAux;

	public Semantico() {
		super();
		this.stack = new Stack();
		this.labels = new HashMap<String, Integer>();
		this.labelsBuffer = new HashMap<String, List<Integer>>();
	}

	public void executeAction(int action, Token token) throws SemanticError {
		System.out.println("Ação #" + action + ", Token: " + token);

		// bitmask
		// ula reg flow
		// 000 00 000

		switch (action) {
		// 50 - Init stack and put two dummy bytes for program.
		case PROGRAM_START: {
			this.stackIndex = 1;
			this.stack.ensureCapacity(2);
			this.stack.add(0, FIRST_DUMMY_BYTE);
			this.stack.add(1, SECOND_DUMMY_BYTE);
			break;
		}
		// 51 - Cleanup bytes generation.
		case INSTRUCTION_START: {
			this.generatedByte = 0;
			this.generatedByteAux = 0;
			break;
		}
		// 52 - Parse the label's that are not parsed
		case PROGRAM_END: {
			this.stack.set(1, this.stackIndex - 1);

			for (String label : this.labelsBuffer.keySet()) {

				if (this.labels.containsKey(label)) {

					int comando = this.labels.get(label) - 3;
					
					for (Integer labelIndex : this.labelsBuffer.get(label)) {
						
						this.stack.add(labelIndex, comando >>> _08);
						this.stack.add(labelIndex + 1, comando & _FF);
					}
					
				} else {

					throw new SemanticError("Label does not exist: " + label, token.getPosition());
				}
			}
			break;
		}
		// ULA Operations
		// ADD - 000 xx xxx
		case ADD: {
			this.generatedByte &= _1F; // 000 11 111
			break;
		}
		// SUB - 001 xx xxx
		case SUB: {
			this.generatedByte &= _3F; // 001 11 111
			this.generatedByte |= _20; // 001 00 000
			break;
		}
		// AND - 010 xx xxx
		case AND: {
			this.generatedByte &= _5F; // 010 11 111
			this.generatedByte |= _40; // 010 00 000
			break;
		}
		// OR - 011 xx xxx
		case OR: {
			this.generatedByte &= _7F; // 011 11 111
			this.generatedByte |= _60; // 011 00 000
			break;
		}
		// XOR - 100 xx xxx
		case XOR: {
			this.generatedByte &= _9F; // 100 11 111
			this.generatedByte |= _80; // 100 00 000
			break;
		}
		// NOT - 101 xx xxx
		case NOT: {
			this.generatedByte &= _BF; // 101 11 111
			this.generatedByte |= _A0; // 101 00 000
			break;
		}
		// MOV - 110 xx xxx
		case MOV: {
			this.generatedByte &= _DF; // 110 11 111
			this.generatedByte |= _C0; // 110 00 000
			break;
		}
		// INC - 111 xx xxx
		case INC: {
			this.generatedByte &= _FF; // 111 11 111
			this.generatedByte |= _E0; // 111 00 000
			break;
		}

		// High decoder
		case HD: {
			this.stackIndex++;
			this.stack.ensureCapacity(this.stackIndex + 1);
			this.stack.add(this.stackIndex, _07); // 000 00 111
			break;
		}

		// Instruction flow
		// NO HD | HD | HDn
		// AC -> AC | ROM -> AC | RET - xxx xx 000
		case AC_AC__ROM_AC__RET: {
			this.generatedByte &= _F8;
			break;
		}
		// AC -> REG | ROM -> REG | DRAM -> AC - xxx xx 001
		case AC_REG__ROM_REG__DRRAM_AC: {
			this.generatedByte &= _F9; // 111 11 001
			this.generatedByte |= _01; // 000 00 001
			break;
		}
		// AC -> RAM | ROM -> RAM | AC -> DRAM - xxx xx 010
		case AC_RAM__ROM_RAM__AC_DRAM: {
			this.generatedByte &= _FA; // 111 11 010
			this.generatedByte |= _02; // 000 00 010
			break;
		}
		// AC -> OUT | JMP | PUSH - xxx xx 011
		case AC_OUT__JMP__PUSHREG: {
			this.generatedByte &= _FB; // 111 11 011
			this.generatedByte |= _03; // 000 00 011
			break;
		}
		// REG -> AC | JMPC | POP - xxx xx 100
		case REG_AC__JMPC__POPREG: {
			this.generatedByte &= _FC; // 111 11 100
			this.generatedByte |= _04; // 000 00 100
			break;
		}
		// RAM -> AC | JMPZ - xxx xx 101
		case RAM_AC__JMPZ__PUSHA: {
			this.generatedByte &= _FD; // 111 11 101
			this.generatedByte |= _05; // 000 00 101
			break;
		}
		// IN -> AC | CALL - xxx xx 110
		case IN_AC__CALL__POPA: {
			this.generatedByte &= _FE; // 111 11 110
			this.generatedByte |= _06; // 000 00 110
			break;
		}
		// Allocated for HD instrunctions.
		case DONT_USE: {
			this.generatedByte &= _FF; // 111 11 111
			this.generatedByte |= _07; // 000 00 111
			break;
		}

		// Register selection
		// B - xxx 00 xxx
		case REG_B: {
			this.generatedByte &= _E7; // 111 00 111
			break;
		}
		// C - xxx 01 xxx
		case REG_C: {
			this.generatedByte &= _EF; // 111 01 111
			this.generatedByte |= _08; // 000 01 000
			break;
		}
		// D - xxx 10 xxx
		case REG_D: {
			this.generatedByte &= _F7; // 111 10 111
			this.generatedByte |= _10; // 000 10 000
			break;
		}
		// E - xxx 11 xxx
		case REG_E: {
			this.generatedByte &= _FF; // 111 11 111
			this.generatedByte |= _18; // 000 11 000
			break;
		}

		// IN/OUT Flags
		case IN_OUT_FLAG: {
			String lex = token.getLexeme();

			if (IN0.equalsIgnoreCase(lex) || OUT0.equalsIgnoreCase(lex)) {
				
				this.generatedByte &= _E7; // 111 00 111
				
			} else if (IN1.equalsIgnoreCase(lex) || OUT1.equalsIgnoreCase(lex)) {
				
				this.generatedByte &= _EF; // 111 01 111
				this.generatedByte |= _08; // 000 01 000
				
			} else if (IN2.equalsIgnoreCase(lex) || OUT2.equalsIgnoreCase(lex)) {
				
				this.generatedByte &= _F7; // 111 10 111
				this.generatedByte |= _10; // 000 10 000
				
			} else if (IN3.equalsIgnoreCase(lex) || OUT3.equalsIgnoreCase(lex)) {
				
				this.generatedByte &= _FF; // 111 11 111
				this.generatedByte |= _18; // 000 11 000
			}
			
			break;
		}
		
		// RAM Record
		case RAM_RECORD:
		{
			String value = token.getLexeme().substring(1, 3);
			this.generatedByte = Integer.decode(HEXA_PREFIX + value);
			this.stackIndex++;
			this.stack.ensureCapacity(this.stackIndex + 1);
			this.stack.add(this.stackIndex, this.generatedByte);
			this.generatedByte = 0;
			break;
		}
		
		// ROM data
		case ROM_PREPARE:
		{
			String value = token.getLexeme();
			this.generatedByteAux = Integer.decode(HEXA_PREFIX + value);
			break;
		}
		case AUX_RECORD:
		{
			this.stackIndex++;
			this.stack.ensureCapacity(this.stackIndex + 1);
			this.stack.add(this.stackIndex, this.generatedByteAux);
			this.generatedByteAux = 0;
			break;
		}
		
		// Stack the byte
		case BYTE_RECORD:
		{
			this.stackIndex++;
			this.stack.ensureCapacity(this.stackIndex + 1);
			this.stack.add(this.stackIndex, this.generatedByte);
			this.generatedByte = 0;
			break;
		}
		
		// REND - Address ROM - 16Bit
		case REND:
		{
			String lexeme = token.getLexeme();
			String value = lexeme.substring(1, 3);
			this.generatedByte = Integer.decode(HEXA_PREFIX + value);
			this.stackIndex++;
			this.stack.ensureCapacity(this.stackIndex + 1);
			this.stack.add(this.stackIndex, this.generatedByte);
			value = lexeme.substring(3, 5);
			this.generatedByte = Integer.decode(HEXA_PREFIX + value);
			this.stackIndex++;
			this.stack.ensureCapacity(this.stackIndex + 1);
			this.stack.add(this.stackIndex, this.generatedByte);
			break;
		}
		
		// Label operations
		case LABEL_TRANSLATE:
		{
			String label = token.getLexeme().toUpperCase();
			
			// Same label are called from different places in the code. We have to guard all this places.
			List<Integer> places = null;
			
			if (this.labelsBuffer.containsKey(label)) {
				
				places = this.labelsBuffer.get(label);
				
			} else {
				
				places = new ArrayList<Integer>(5);
			}
			
			places.add(this.stackIndex + 1);
			
			labelsBuffer.put(label, places);
			this.stackIndex += 2;
			this.stack.ensureCapacity(this.stackIndex + 1);
			break;
		}
		case LABEL_RECORD:
		{
			String lexeme = token.getLexeme();
			String label = lexeme.substring(0, lexeme.length() -1).toUpperCase();
			
			if (!this.labels.containsKey(label)) {
				
				this.labels.put(label, this.stackIndex + 2);
				
			} else {
				
				throw new SemanticError("Duplicated label: " + label, token.getPosition());
			}
			break;
		}
		default:
			break;
		}
	}

	public List<Integer> getStack() {
		return stack;
	}

	public HashMap<String, Integer> getLabels() {
		return labels;
	}

	public HashMap<String, List<Integer>> getLabelsBuffer() {
		return labelsBuffer;
	}
	
	private static class Stack extends AbstractList<Integer>{
		
		private Integer[] data;
		
		public Stack() {
			this.data = new Integer[1];
		}
		
		@Override
		public Spliterator<Integer> spliterator(){
			return super.spliterator();
		}
		
		@Override
		public Integer set(int index, Integer element) {
			Integer atPlace = this.data[index];
			this.data[index] = element;
			return atPlace;
		}
		
		/**
		 * Expands the array size to ensure this capacity.
		 * @param count
		 */
		public void ensureCapacity(int count) {

			if (count >= this.data.length) {
				
				Integer[] copy = new Integer[this.data.length + (count - this.data.length)];
				
				System.arraycopy(this.data, 0, copy, 0, this.data.length);
				
				this.data = copy;
			}
		}
		
		@Override
		public void add(int index, Integer element) {
			
			this.ensureCapacity(index + 1);
			
			this.data[index] = element;
		}
		
		@Override
		public boolean remove(Object o) {
			return super.remove(o);
		}

		@Override
		public Integer get(int index) {
			return this.data[index];
		}

		@Override
		public int size() {
			return this.data.length;
		}
	
	}
}
