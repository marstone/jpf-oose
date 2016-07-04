package cn.edu.sjtu.stap.jpf.oose;

import java.util.Map;
import java.util.Vector;

import cn.edu.sjtu.stap.jpf.oose.bytecode.INVOKESTATIC;
import gov.nasa.jpf.Config;
import gov.nasa.jpf.JPF;
import gov.nasa.jpf.jvm.bytecode.ARETURN;
import gov.nasa.jpf.jvm.bytecode.DRETURN;
import gov.nasa.jpf.jvm.bytecode.FRETURN;
import gov.nasa.jpf.jvm.bytecode.IRETURN;
import gov.nasa.jpf.jvm.bytecode.JVMInvokeInstruction;
import gov.nasa.jpf.jvm.bytecode.JVMReturnInstruction;
import gov.nasa.jpf.jvm.bytecode.LRETURN;
import gov.nasa.jpf.symbc.SymbolicInstructionFactory;
import gov.nasa.jpf.symbc.SymbolicListener;
import gov.nasa.jpf.symbc.bytecode.BytecodeUtils;
import gov.nasa.jpf.symbc.concolic.PCAnalyzer;
import gov.nasa.jpf.symbc.numeric.Expression;
import gov.nasa.jpf.symbc.numeric.IntegerConstant;
import gov.nasa.jpf.symbc.numeric.IntegerExpression;
import gov.nasa.jpf.symbc.numeric.PCChoiceGenerator;
import gov.nasa.jpf.symbc.numeric.PathCondition;
import gov.nasa.jpf.symbc.numeric.RealConstant;
import gov.nasa.jpf.symbc.numeric.RealExpression;
import gov.nasa.jpf.symbc.numeric.SymbolicConstraintsGeneral;
import gov.nasa.jpf.symbc.numeric.SymbolicInteger;
import gov.nasa.jpf.util.Pair;
import gov.nasa.jpf.vm.ChoiceGenerator;
import gov.nasa.jpf.vm.ClassInfo;
import gov.nasa.jpf.vm.DynamicElementInfo;
import gov.nasa.jpf.vm.Instruction;
import gov.nasa.jpf.vm.LocalVarInfo;
import gov.nasa.jpf.vm.MethodInfo;
import gov.nasa.jpf.vm.StackFrame;
import gov.nasa.jpf.vm.ThreadInfo;
import gov.nasa.jpf.vm.Types;
import gov.nasa.jpf.vm.VM;

public class ObjectSymbolicListener extends SymbolicListener {

	
	public ObjectSymbolicListener(Config conf, JPF jpf) {
		super(conf, jpf);
		System.out.println("Object-Oriented Symbolic Listener created ...");
	}

	@Override
	public void instructionExecuted(VM vm, ThreadInfo currentThread, Instruction nextInstruction, Instruction executedInstruction) {

		if (!vm.getSystemState().isIgnored()) {
			Instruction insn = executedInstruction;
		//	SystemState ss = vm.getSystemState();
			ThreadInfo ti = currentThread;
			Config conf = vm.getConfig();

			if (insn instanceof JVMInvokeInstruction) {
				JVMInvokeInstruction md = (JVMInvokeInstruction) insn;
				String methodName = md.getInvokedMethodName();
				int numberOfArgs = md.getArgumentValues(ti).length;

				MethodInfo mi = md.getInvokedMethod();
				ClassInfo ci = mi.getClassInfo();
				String className = ci.getName();

				StackFrame sf = ti.getTopFrame();
				String shortName = methodName;
				String longName = mi.getLongName();
				if (methodName.contains("("))
					shortName = methodName.substring(0,methodName.indexOf("("));


				if(!mi.equals(sf.getMethodInfo()))
					return;

				if ((BytecodeUtils.isClassSymbolic(conf, className, mi, methodName))
						|| BytecodeUtils.isMethodSymbolic(conf, mi.getFullName(), numberOfArgs, null)){

							

					MethodSummary methodSummary = new MethodSummary();

					methodSummary.setMethodName(className + "." + shortName);
					Object [] argValues = md.getArgumentValues(ti);
					String argValuesStr = "";
					for (int i=0; i<argValues.length; i++){
						argValuesStr = argValuesStr + argValues[i];
						if ((i+1) < argValues.length)
							argValuesStr = argValuesStr + ",";
					}
					methodSummary.setArgValues(argValuesStr);
					byte [] argTypes = mi.getArgumentTypes();
					String argTypesStr = "";
					for (int i=0; i<argTypes.length; i++){
						argTypesStr = argTypesStr + argTypes[i];
						if ((i+1) < argTypes.length)
							argTypesStr = argTypesStr + ",";
					}
					methodSummary.setArgTypes(argTypesStr);

					//get the symbolic values (changed from constructing them here)
					String symValuesStr = "";
					String symVarNameStr = "";


					LocalVarInfo[] argsInfo = mi.getArgumentLocalVars();

					if(argsInfo == null)
						throw new RuntimeException("ERROR: you need to turn debug option on");

					int sfIndex=1; //do not consider implicit param "this"
					int namesIndex=1;
					if (md instanceof INVOKESTATIC) {
						sfIndex=0; // no "this" for static
						namesIndex =0;
					}

					for(int i=0; i < numberOfArgs; i++){
						Expression expLocal = (Expression)sf.getLocalAttr(sfIndex);
						if (expLocal != null) // symbolic
							symVarNameStr = expLocal.toString();
						else
							symVarNameStr = argsInfo[namesIndex].getName() + "_CONCRETE" + ",";
						// TODO: what happens if the argument is an array?
						symValuesStr = symValuesStr + symVarNameStr + ",";
						sfIndex++;namesIndex++;
						if(argTypes[i] == Types.T_LONG || argTypes[i] == Types.T_DOUBLE)
							sfIndex++;

					}

					// get rid of last ","
					if (symValuesStr.endsWith(",")) {
						symValuesStr = symValuesStr.substring(0,symValuesStr.length()-1);
					}
					methodSummary.setSymValues(symValuesStr);

					this.setCurrentMethodName(longName);
					this.getAllSummaries().put(longName,methodSummary);
				}
			}else if (insn instanceof JVMReturnInstruction){
				MethodInfo mi = insn.getMethodInfo();
				ClassInfo ci = mi.getClassInfo();
				if (null != ci){
					String className = ci.getName();
					String methodName = mi.getName();
					String longName = mi.getLongName();
					int numberOfArgs = mi.getNumberOfArguments();
					
					if (((BytecodeUtils.isClassSymbolic(conf, className, mi, methodName))
							|| BytecodeUtils.isMethodSymbolic(conf, mi.getFullName(), numberOfArgs, null))){
					
						ChoiceGenerator <?>cg = vm.getChoiceGenerator();
						if (!(cg instanceof PCChoiceGenerator)){
							ChoiceGenerator <?> prev_cg = cg.getPreviousChoiceGenerator();
							while (!((prev_cg == null) || (prev_cg instanceof PCChoiceGenerator))) {
								prev_cg = prev_cg.getPreviousChoiceGenerator();
							}
							cg = prev_cg;
						}
						if ((cg instanceof PCChoiceGenerator) &&(
								(PCChoiceGenerator) cg).getCurrentPC() != null){
							PathCondition pc = ((PCChoiceGenerator) cg).getCurrentPC();
							//pc.solve(); //we only solve the pc
							if (SymbolicInstructionFactory.concolicMode) { //TODO: cleaner
								SymbolicConstraintsGeneral solver = new SymbolicConstraintsGeneral();
								PCAnalyzer pa = new PCAnalyzer();
								pa.solve(pc,solver);
							}
							else
								pc.solve();

							if (!PathCondition.flagSolved) {
							  return;
							}

							//after the following statement is executed, the pc loses its solution

							String pcString = pc.toString();//pc.stringPC();
							Pair<String,String> pcPair = null;

							String returnString = "";


							Expression result = null;

							if (insn instanceof IRETURN){
								IRETURN ireturn = (IRETURN)insn;
								int returnValue = ireturn.getReturnValue();
								IntegerExpression returnAttr = (IntegerExpression) ireturn.getReturnAttr(ti);
								if (returnAttr != null){
									returnString = "Return Value: " + String.valueOf(returnAttr.solution());
									result = returnAttr;
								}else{ // concrete
									returnString = "Return Value: " + String.valueOf(returnValue);
									result = new IntegerConstant(returnValue);
								}
							}
							else if (insn instanceof LRETURN) {
								LRETURN lreturn = (LRETURN)insn;
								long returnValue = lreturn.getReturnValue();
								IntegerExpression returnAttr = (IntegerExpression) lreturn.getReturnAttr(ti);
								if (returnAttr != null){
									returnString = "Return Value: " + String.valueOf(returnAttr.solution());
									result = returnAttr;
								}else{ // concrete
									returnString = "Return Value: " + String.valueOf(returnValue);
									result = new IntegerConstant((int)returnValue);
								}
							}
							else if (insn instanceof DRETURN) {
								DRETURN dreturn = (DRETURN)insn;
								double returnValue = dreturn.getReturnValue();
								RealExpression returnAttr = (RealExpression) dreturn.getReturnAttr(ti);
								if (returnAttr != null){
									returnString = "Return Value: " + String.valueOf(returnAttr.solution());
									result = returnAttr;
								}else{ // concrete
									returnString = "Return Value: " + String.valueOf(returnValue);
									result = new RealConstant(returnValue);
								}
							}
							else if (insn instanceof FRETURN) {
								
								FRETURN freturn = (FRETURN)insn;
								double returnValue = freturn.getReturnValue();
								RealExpression returnAttr = (RealExpression) freturn.getReturnAttr(ti);
								if (returnAttr != null){
									returnString = "Return Value: " + String.valueOf(returnAttr.solution());
									result = returnAttr;
								}else{ // concrete
									returnString = "Return Value: " + String.valueOf(returnValue);
									result = new RealConstant(returnValue);
								}

							}
							else if (insn instanceof ARETURN){
								ARETURN areturn = (ARETURN)insn;
								IntegerExpression returnAttr = (IntegerExpression) areturn.getReturnAttr(ti);
								if (returnAttr != null){
									returnString = "Return Value: " + String.valueOf(returnAttr.solution());
									result = returnAttr;
								}
								else {// concrete
									DynamicElementInfo val = (DynamicElementInfo)areturn.getReturnValue(ti);
									
									//System.out.println("string "+val.asString());
									returnString = "Return Value: " + val.asString();
									//DynamicElementInfo val = (DynamicElementInfo)areturn.getReturnValue(ti);
									String tmp = val.asString();
									tmp = tmp.substring(tmp.lastIndexOf('.')+1);
									result = new SymbolicInteger(tmp);
									
								}
								
							}
							
							else //other types of return
								returnString = "Return Value: --";
							//pc.solve();
							// not clear why this part is necessary
/*
							if (SymbolicInstructionFactory.concolicMode) { //TODO: cleaner
								SymbolicConstraintsGeneral solver = new SymbolicConstraintsGeneral();
								PCAnalyzer pa = new PCAnalyzer();
								pa.solve(pc,solver);
							}
							else
								pc.solve();
*/


							pcString = pc.toString();
							pcPair = new Pair<String,String>(pcString,returnString);
							MethodSummary methodSummary = this.getAllSummaries().get(longName);
							Vector<Pair> pcs = methodSummary.getPathConditions();
							if ((!pcs.contains(pcPair)) && (pcString.contains("SYM"))) {
								methodSummary.addPathCondition(pcPair);
							}
							
							if(this.getAllSummaries().get(longName)!=null) // recursive call
								longName = longName;// + methodSummary.hashCode(); // differentiate the key for recursive calls
							this.getAllSummaries().put(longName,methodSummary);
							if (SymbolicInstructionFactory.debugMode) {
							    System.out.println("*************Summary***************");
							    System.out.println("PC is:"+pc.toString());
							    if(result!=null){
								System.out.println("Return is:  "+result);
								System.out.println("***********************************");
							    }
							}
						}
					}
				}
			}
		}
	
	}

	protected Map<String, MethodSummary> getAllSummaries() {
		return (Map<String, MethodSummary>)ReflectUtils.getPrivateField(SymbolicListener.class, null, this, "allSummaries");
	}


	protected String getCurrentMethodName() {
		return (String)ReflectUtils.getPrivateField(SymbolicListener.class, String.class, this, "currentMethodName");
	}

	protected void setCurrentMethodName(String methodName) {
		ReflectUtils.setPrivateField(SymbolicListener.class, this, "currentMethodName", methodName);
	}
	
	
	
	
}
