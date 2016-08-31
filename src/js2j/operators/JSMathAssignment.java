package js2j.operators;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import js2j.JSBlock;
import js2j.JSStatement;

public class JSMathAssignment extends JSAssignment {
	
	public JSMathAssignment(JSStatement jsStatement) {
		super(extractOperands(jsStatement)[0],extractOperands(jsStatement)[1],extractOperands(jsStatement)[2]);
		this.setJsStatement(jsStatement);
		this.setParent(jsStatement.getParent());
		// Add JSMathAssignment to the first JSBlock parent;
		((JSBlock) getParent(JSBlock.class)).add(this);
	}
	
	private static Pattern p = Pattern.compile("(.*)([\\+|-|\\*|/|%]=)(.*)");
	
	private static JSStatement[] extractOperands(JSStatement jsStatement) {
		String[] operands = new String[2];
		String operator = null;
		Matcher m = p.matcher(jsStatement.toString());
		while(m.find())
			operands[0] = m.group(1); 
		m = p.matcher(jsStatement.toString());
		while(m.find())
			operator = m.group(2); 
		m = p.matcher(jsStatement.toString());
		while(m.find())
			operands[1] = m.group(3); 	
		return new JSStatement[]{new JSStatement(operands[0],jsStatement.getLineNumber()),new JSStatement(operator),new JSStatement(operands[1],jsStatement.getLineNumber())};
	}
	
	@Override
	public String toString() {
		return this.getLeftOperand()+""+this.getOperator()+""+this.getRightOperand()+"; ";
	}

}
