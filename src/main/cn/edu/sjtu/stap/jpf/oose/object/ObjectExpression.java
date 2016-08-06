package cn.edu.sjtu.stap.jpf.oose.object;

import gov.nasa.jpf.symbc.bytecode.BytecodeUtils;
import gov.nasa.jpf.symbc.bytecode.BytecodeUtils.VarType;
import gov.nasa.jpf.symbc.numeric.ConstraintExpressionVisitor;
import gov.nasa.jpf.symbc.numeric.Expression;
import gov.nasa.jpf.symbc.numeric.IntegerExpression;
import gov.nasa.jpf.symbc.numeric.SymbolicInteger;
import gov.nasa.jpf.vm.ClassInfo;
import gov.nasa.jpf.vm.ElementInfo;
import gov.nasa.jpf.vm.FieldInfo;

import java.util.Map;

public class ObjectExpression  extends Expression {

	
	public ObjectExpression(String varName, ClassInfo ci, ElementInfo ei) {
		String clazz = ci.getName();
		System.out.println("[Create ObjectExpression]: " + clazz + ", ci:" + ci);
		for(FieldInfo fi : ci.getDeclaredInstanceFields()) {
			if(fi.getType().equalsIgnoreCase("int") || fi.getType().equalsIgnoreCase("long")) {
				String name = varName + "." + fi.getName();
				IntegerExpression sym_v = new SymbolicInteger(BytecodeUtils.varName(name, VarType.INT));
				ei.setFieldAttr(fi, sym_v);
				// expressionMap.put(name, sym_v);
				// sf.setOperandAttr(stackIdx, sym_v);
				// outputString = outputString.concat(" " + sym_v + ",");
			}
		}
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
