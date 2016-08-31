package js2j.operators;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import js2j.JSStatement;
import js2j.JSValue;

public abstract class JSOperator extends JSStatement {
		
	public JSOperator() {}
	
	public JSOperator(JSStatement leftOperand, JSStatement operator, JSValue rightOperand) {
		this.setLeftOperand(leftOperand);
		this.setOperator(operator);
		this.setRightOperand(rightOperand);
	}
	
	public JSOperator(JSStatement jsStatement) {
		
	}

	public JSValue getRightOperand() {
		return rightOperand;
	}

	public void setRightOperand(JSValue rightOperand) {
		this.rightOperand = rightOperand;
	}
	
	private JSValue rightOperand;

	public JSStatement getLeftOperand() {
		return leftOperand;
	}

	public void setLeftOperand(JSStatement leftOperand) {
		this.leftOperand = leftOperand;
	}
	
	private JSStatement leftOperand;

	public JSStatement getOperator() {
		return operator;
	}

	public void setOperator(JSStatement operator) {
		this.operator = operator;
	}

	private JSStatement operator;
	
	public Pattern getPattern() {
		return pattern;
	}

	public void setPattern(Pattern pattern) {
		this.pattern = pattern;
	}
	
	private Pattern pattern;
	
	void extractOperator() {		
		Matcher m = this.getPattern().matcher(this.getJsStatement().toString());
		while(m.find())
			this.setOperator(new JSStatement(m.group(2)));
	}
	
	public void addOperand(JSStatement jsStatement) {
		if(this.getLeftOperand() == null)
			this.setLeftOperand(jsStatement);
		else if (this.getRightOperand() == null)
			this.setRightOperand(new JSValue(jsStatement));
	}

	void extractOperands() {
		ArrayList<String> operands = new ArrayList<String>();
		Matcher m = this.getPattern().matcher(this.getJsStatement().toString());
		while(m.find()) {
			operands.add(m.group(1));
			operands.add(m.group(3));
		}
	
		for(String operandAsString: operands) {
			JSStatement builtJSStatement = new JSStatement(operandAsString.trim(),this.getJsStatement().getLineNumber());
			builtJSStatement.setParent(this);
			if(!builtJSStatement.build()) {
				//TODO
			}
		}
	}
}
