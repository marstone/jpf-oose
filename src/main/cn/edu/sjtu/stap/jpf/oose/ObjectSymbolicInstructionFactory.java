package cn.edu.sjtu.stap.jpf.oose;

import gov.nasa.jpf.Config;
import gov.nasa.jpf.symbc.SymbolicInstructionFactory;
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

	public Instruction invokestatic(String clsName, String methodName, String methodSignature) {
		// return super.invokestatic(clsName, methodName, methodSignature);
		return new cn.edu.sjtu.stap.jpf.oose.bytecode.INVOKESTATIC(clsName, methodName, methodSignature);
	}

	public Instruction invokevirtual(String clsName, String methodName, String methodSignature) {
		return new cn.edu.sjtu.stap.jpf.oose.bytecode.INVOKEVIRTUAL(clsName, methodName, methodSignature);
	}


	public Instruction getfield(String fieldName, String clsName, String fieldDescriptor){
		// return (filter.isPassing(ci) ?  new GETFIELD(fieldName, clsName, fieldDescriptor): super.getfield(fieldName, clsName, fieldDescriptor));
		return new cn.edu.sjtu.stap.jpf.oose.bytecode.GETFIELD(fieldName, clsName, fieldDescriptor);
	}
	
	/*
	public Instruction invokeinterface(String clsName, String methodName, String methodSignature) {
		return (filter.isPassing(ci) ? new INVOKEINTERFACE(clsName, methodName, methodSignature): super.invokeinterface(clsName, methodName, methodSignature));
	}
	
	public Instruction invokespecial(String clsName, String methodName, String methodSignature) {
		  return (filter.isPassing(ci) ? new INVOKESPECIAL(clsName, methodName, methodSignature): super.invokespecial(clsName, methodName, methodSignature));
	}
	*/

}