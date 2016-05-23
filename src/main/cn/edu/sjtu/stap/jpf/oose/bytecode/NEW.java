package cn.edu.sjtu.stap.jpf.oose.bytecode;

import gov.nasa.jpf.vm.Instruction;
import gov.nasa.jpf.vm.ThreadInfo;

/**
 * @author marstone
 * @since 2016/05/23, xx's birthday
 */
public class NEW extends gov.nasa.jpf.symbc.bytecode.NEW {

	public NEW(String clsName) {
		super(clsName);
	}


	@Override
	public Instruction execute (ThreadInfo th) {
		System.out.println("cn.edu.sjtu.stap.jpf.oose.bytecode.NEW executed.");
		return super.execute(th);
	}
	
}
