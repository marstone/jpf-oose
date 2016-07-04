package cn.edu.sjtu.stap.jpf.oose.object;

import java.util.Map;

import gov.nasa.jpf.symbc.numeric.ConstraintExpressionVisitor;
import gov.nasa.jpf.symbc.numeric.Expression;

/**
 * 
 * @author marstone
 * @since 2016/05
 */
public class SymbolicObject extends Expression {

	
	public SymbolicObject(String varName, String clazz) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public int compareTo(Expression o) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String stringPC() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void getVarsVals(Map<String, Object> varsVals) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void accept(ConstraintExpressionVisitor visitor) {
		// TODO Auto-generated method stub
		
	}

}
