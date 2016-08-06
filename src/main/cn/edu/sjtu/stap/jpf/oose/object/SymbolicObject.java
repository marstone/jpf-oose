package cn.edu.sjtu.stap.jpf.oose.object;

import gov.nasa.jpf.vm.ClassInfo;
import gov.nasa.jpf.vm.ElementInfo;

/**
 * 
 * @author marstone
 * @since 2016/05
 */
public class SymbolicObject extends ObjectExpression {

	public SymbolicObject(String varName, ClassInfo ci, ElementInfo ei) {
		super(varName, ci, ei);
	}
	
}