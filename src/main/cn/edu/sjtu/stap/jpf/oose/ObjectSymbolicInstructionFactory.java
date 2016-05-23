package cn.edu.sjtu.stap.jpf.oose;

import gov.nasa.jpf.Config;
import gov.nasa.jpf.symbc.SymbolicInstructionFactory;
import gov.nasa.jpf.symbc.bytecode.NEW;
import gov.nasa.jpf.vm.Instruction;

public class ObjectSymbolicInstructionFactory extends SymbolicInstructionFactory {

	public ObjectSymbolicInstructionFactory(Config conf) {
		super(conf);
		// TODO Auto-generated constructor stub
	}
	public Instruction iadd() {
		return (new cn.edu.sjtu.stap.jpf.oose.bytecode.IADD());
	}
	  
	public Instruction new_(String clsName) {
		return (new cn.edu.sjtu.stap.jpf.oose.bytecode.NEW(clsName));		
	}

}