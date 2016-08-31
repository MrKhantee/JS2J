package js2j.operators;

import js2j.JSBlock;
import js2j.JSForLoop;
import js2j.JSStatement;
import js2j.JSValue;

public class JSAssignment extends JSOperator {
	
	public JSAssignment() {}
	
	public JSAssignment(JSStatement jsStatement) {
		this.setJsStatement(jsStatement);
		this.setParent(jsStatement.getParent());
		String[] operands = extractOperands2();
		this.addOperand(new JSStatement(operands[0]));
		this.setOperator(new JSStatement("="));
		
		// Check if the variable if a parameter of a JSForLoop
		if(isJSForLoopStatement())
			((JSForLoop) getParent(JSForLoop.class)).addJSForLoopStatement(this);
		// Add this assignment to parent JSBlock
		else
			((JSBlock) getParent(JSBlock.class)).add(this);

		JSStatement jsBuiltStatement = new JSStatement(operands[1],jsStatement.getLineNumber());
		jsBuiltStatement.setParent(this);
		jsBuiltStatement.build();
	}

	public JSAssignment(JSStatement leftOperand, JSStatement operator, JSStatement rightOperand) {
		super(leftOperand,operator,new JSValue(rightOperand));
	}
	
	private String[] extractOperands2() {
		return getJsStatement().toString().split("=");
	}
	
	@Override
	public String toString() {
		return this.getLeftOperand()+""+this.getOperator()+""+this.getRightOperand()+"; ";
	}
	
}
