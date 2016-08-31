package js2j.operators;

import java.util.regex.Pattern;

import js2j.JSStatement;

public class JSBitToBit extends JSOperator {
	
	// ([\w|&|\^|\|]+)(&|\^|\|)([\w|&|\^|\|]+)	
	private static Pattern o = Pattern.compile("(\\w+)(&|\\^|\\|)(\\w+)");
	
	public JSBitToBit(JSStatement jsStatement) {
		this.setJsStatement(jsStatement);
		this.setParent(jsStatement.getParent());
		if(getParent() instanceof JSRotationBit)
			((JSRotationBit) getParent()).addOperand(this);
		
		this.setPattern(o);
		this.extractOperator();
		this.extractOperands();		
	}
	
	public String toString() {
		return this.getLeftOperand() +""+ this.getOperator() +""+ this.getRightOperand();
	}
	
}
