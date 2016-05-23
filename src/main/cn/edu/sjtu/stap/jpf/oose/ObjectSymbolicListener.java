package cn.edu.sjtu.stap.jpf.oose;

import gov.nasa.jpf.Config;
import gov.nasa.jpf.JPF;
import gov.nasa.jpf.symbc.SymbolicListener;

public class ObjectSymbolicListener extends SymbolicListener {

	public ObjectSymbolicListener(Config conf, JPF jpf) {
		super(conf, jpf);
		// TODO Auto-generated constructor stub
		System.out.println("Object-Oriented Symbolic Listener created ...");
	}

}
