package js2j.operators;

import java.util.regex.Pattern;

import js2j.JSParameter;
import js2j.JSPrimitiveType;
import js2j.JSStatement;
import js2j.JSValue;

public class JSComputation extends JSOperator {

	private static Pattern o = Pattern.compile("(.*)(\\-|\\+|%|\\/|\\*)(.*)");
		
	public JSComputation(JSStatement jsStatement) {
		this.setJsStatement(format(jsStatement));
		if(jsStatement.toString().indexOf('(')==0
				&& jsStatement.toString().lastIndexOf(')')==jsStatement.toString().length()-1)
			this.setGrouped(true);
		this.setParent(jsStatement.getParent());
		
		// Set value of its parent
		if(getParent() instanceof JSAssignment) {
			((JSAssignment) getParent()).setRightOperand(new JSValue(this));
		} else if(getParent() instanceof JSComputation) {
			((JSComputation) getParent()).addOperand(this);
		} else {
			if(getParent() != null)
				getParent().setValue(this);
		}
		this.setPattern(o);
		this.extractOperator();
		this.extractOperands();
	}
	
	private void setGrouped(boolean isGrouped) {
		this.isGrouped = isGrouped;
	}
	
	private boolean isGrouped() {
		return this.isGrouped;
	}
	
	private boolean isGrouped = false;
	
	private static JSStatement format(JSStatement jsStatement) {
		if(jsStatement.toString().indexOf('(') == 0
				&& jsStatement.toString().lastIndexOf(')') == jsStatement.toString().length()-1) {
			JSStatement formattedJSStatement = new JSStatement(jsStatement.toString().substring(jsStatement.toString().indexOf('(')+1, jsStatement.toString().length()-1),jsStatement.getLineNumber());
			formattedJSStatement.setParent(jsStatement.getParent());
			return formattedJSStatement;
		}
		return jsStatement;
	}
	
	@Override
	public String toString() {
		if(isGrouped())
			return "("+this.getLeftOperand() +""+ this.getOperator() +""+ this.getRightOperand() +")";
		return this.getLeftOperand() +""+ this.getOperator() +""+ this.getRightOperand();
	}
	
	@Override
	public String getType() {
		int mainTypePowerIndex = 0;
		JSStatement[] operands = {this.getLeftOperand(), this.getRightOperand()};
		for(JSStatement operand: operands) {
			JSPrimitiveType jsPrimitiveType;
			JSStatement jsVariable = getVariable(operand.toString());
//			System.out.println("JSComputation--)): "+getVariable(jsStatement.toString()).getClass());
			if(jsVariable instanceof JSParameter) {
//				System.out.println("JSComputation-->>:"+((JSParameter) jsVariable).getType());
				if((jsPrimitiveType = JSPrimitiveType.asJSPrimitiveType(((JSParameter)jsVariable).getType())) != null) {
					if(jsPrimitiveType.getPowerIndex() > mainTypePowerIndex) {
						System.out.println("POWERINDEX:"+jsPrimitiveType.getPowerIndex());
						mainTypePowerIndex = jsPrimitiveType.getPowerIndex();
					}
				}
			}
		}
		if(JSPrimitiveType.asJSPrimitiveType(mainTypePowerIndex) != null)
			return JSPrimitiveType.asJSPrimitiveType(mainTypePowerIndex).name().toLowerCase();
		return "NULL";
	}
	
	
}
