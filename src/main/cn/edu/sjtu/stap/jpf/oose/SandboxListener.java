package cn.edu.sjtu.stap.jpf.oose;

import gov.nasa.jpf.PropertyListenerAdapter;
import gov.nasa.jpf.vm.Instruction;
import gov.nasa.jpf.vm.ThreadInfo;
import gov.nasa.jpf.vm.VM;

public class SandboxListener extends PropertyListenerAdapter {

	
	public static void main(String args[]) {
		System.out.println("hi");
	}
	
	@Override
	public void instructionExecuted(VM vm, ThreadInfo currentThread, Instruction nextInstruction, Instruction executedInstruction) {
		System.out.println(nextInstruction);	
	}
	
	
}
