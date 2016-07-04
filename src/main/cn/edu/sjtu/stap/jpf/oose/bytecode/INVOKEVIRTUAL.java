package cn.edu.sjtu.stap.jpf.oose.bytecode;

import gov.nasa.jpf.symbc.numeric.Expression;
import gov.nasa.jpf.vm.ClassInfo;
import gov.nasa.jpf.vm.Instruction;
import gov.nasa.jpf.vm.MethodInfo;
import gov.nasa.jpf.vm.StackFrame;
import gov.nasa.jpf.vm.ThreadInfo;

public class INVOKEVIRTUAL extends gov.nasa.jpf.jvm.bytecode.INVOKEVIRTUAL {
	
	public INVOKEVIRTUAL(String clsName, String methodName, String methodSignature) {
	    super(clsName, methodName, methodSignature);
	}
	
	
	@Override
	public Instruction execute( ThreadInfo th) {
		
		int objRef = th.getCalleeThis(getArgSize());
		
	    if (objRef == -1) {
	      lastObj = -1;
	      return th.createAndThrowException("java.lang.NullPointerException", "Calling '" + mname + "' on null object");
	    }
	    
		StackFrame sf = th.getModifiableTopFrame();
		// get the first parameter (this)
		Expression sym_this = (Expression) sf.getOperandAttr(this.argSize - 1);

		if(null != sym_this) {
			// 
			System.out.println("symbolic object found.");
		}
		
	    MethodInfo mi = getInvokedMethod(th, objRef);

	    if (mi == null) {
	      ClassInfo ci = th.getClassInfo(objRef);
	      String clsName = (ci != null) ? ci.getName() : "?UNKNOWN?";
	      return th.createAndThrowException("java.lang.NoSuchMethodError", clsName + '.' + mname);
	    }
	    
	    ObjectBytecodeUtils.InstructionOrSuper nextInstr = ObjectBytecodeUtils.execute(this,  th);
        if (nextInstr.callSuper) {
            return super.execute(th);
        } else {
            return nextInstr.inst;
        }
    }
}
