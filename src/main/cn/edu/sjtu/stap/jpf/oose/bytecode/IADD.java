package cn.edu.sjtu.stap.jpf.oose.bytecode;

import gov.nasa.jpf.vm.Instruction;
import gov.nasa.jpf.vm.ThreadInfo;

/**
 * @author marstone
 * @since 2016/05/23
 **/
public class IADD extends gov.nasa.jpf.symbc.bytecode.IADD {

	@Override
	public Instruction execute (ThreadInfo th) {
		// System.out.println("cn.edu.sjtu.stap.jpf.oose.bytecode.IADD executed.");
		return super.execute(th);
	}
}
