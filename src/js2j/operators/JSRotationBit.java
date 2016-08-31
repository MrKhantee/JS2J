package js2j.operators;

import java.util.regex.Pattern;

import js2j.JSReturn;
import js2j.JSStatement;

public class JSRotationBit extends JSOperator {
	
	// ([\w|&|\^|\|]+)(&|\^|\|)([\w|&|\^|\|]+)
	private static Pattern o = Pattern.compile("([\\w|&|\\^|\\|]+)([\\>|\\<]{2,3})([\\w|&|\\^|\\|]+)");
	
	public JSRotationBit(JSStatement jsStatement) {
		this.setJsStatement(jsStatement);
		this.setParent(jsStatement.getParent());
		if(getParent() instanceof JSReturn)
			((JSReturn) getParent()).setValue(this);
		
		this.setPattern(o);
		this.extractOperator();
		this.extractOperands();		
	}
	
	public String toString() {
		return this.getLeftOperand() +""+ this.getOperator() +""+ this.getRightOperand();
	}
	
}

