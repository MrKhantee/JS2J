package js2j.operators;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import js2j.JSStatement;
import js2j.JSValue;

public class JSIncrementation extends JSOperator {
	
	public JSIncrementation(JSStatement jsStatement) {
		super(extractOperands(jsStatement)[0],new JSStatement(""),new JSValue(extractOperands(jsStatement)[1]));
		this.setJsStatement(jsStatement);
		this.setParent(jsStatement.getParent());
		
//		if(isJSForLoopStatement()) {
//			((JSForLoop) getParent()).addJSForLoopStatement(this);
//			JSStatement builtJSStatement = new JSStatement(JSForLoop.getFirstStatementInForLoop(jsStatement),jsStatement.getLineNumber());
//			System.out.println("INCREMENT:"+builtJSStatement);
//			builtJSStatement.setParent(getParent());
//			builtJSStatement.build();
//		}
	}
	
	private static Pattern p = Pattern.compile("(.*)(\\+\\+|--)");
	
	private static JSStatement[] extractOperands(JSStatement jsStatement) {
		Matcher m = p.matcher(jsStatement.toString());
		String operand0 = null, operand1 = null;
		while(m.find())
			operand0 = m.group(1);
		m = p.matcher(jsStatement.toString());
		while(m.find())
			operand1 = m.group(2);
		System.out.println("ASSIGNLOPERAND: "+operand0);
		System.out.println("ASSIGNROPERAND: "+operand1);		
		
		return new JSStatement[]{new JSStatement(operand0,jsStatement.getLineNumber()),new JSStatement(operand1,jsStatement.getLineNumber())};
	}
	
	@Override
	public String toString() {
		return getLeftOperand()+""+getRightOperand()+"";
	}
}
