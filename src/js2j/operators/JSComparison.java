// Except in regexp : http://stackoverflow.com/questions/9770860/using-regex-to-match-any-character-except
// Check if key present in JSONObject : http://stackoverflow.com/questions/1098040/checking-if-an-array-key-exists-in-a-javascript-object-or-array

package js2j.operators;

import java.util.regex.Pattern;

import js2j.JSForLoop;
import js2j.JSIf;
import js2j.JSStatement;
import js2j.JSVariable;

public class JSComparison extends JSOperator {
	
	private static Pattern	o = Pattern.compile("([^!=]*)(<=?|>=?|!?={1,3}|in)([^=]*)");
	
	public JSComparison() {}

	public JSComparison(JSStatement jsStatement) {
		super();
		this.setJsStatement(jsStatement);
		this.setParent(jsStatement.getParent());
		System.out.println("JSComparison Parent: "+getParent().getClass());
		
		// Set value of its parent
		if(getParent() != null)
			getParent().setValue(this);
		if(isJSIfStatement())
			((JSIf) getParent()).setJSIfStatement(this);
		if(isJSForLoopStatement()) {
			((JSForLoop) getParent(JSForLoop.class)).addJSForLoopStatement(this);	
			jsStatement.buildNext(getParent());
		}
		
		this.setPattern(o);
		this.extractOperands();
		this.extractOperator();
	}
		
	@Override
	public String getType() {
		return "boolean";
	}
	
	@Override
	public String toString() {
		if(getOperator().toString().equals("in")) 
			return this.getRightOperand()+".containsKey("+ getLeftOperand() +")";
		if(getOperator().toString().equals("!=="))
			return getLeftOperand()+" != "+getRightOperand();
		if(getOperator().toString().equals("==="))
			return getLeftOperand()+" == "+getRightOperand();
		if(getOperator().toString().equals("==")
				|| getOperator().toString().equals("===")) {
			JSVariable lJSVariable = (JSVariable) getVariable(this.getLeftOperand().toString());
			JSVariable rJSVariable = (JSVariable) getVariable(this.getRightOperand().toString());
			if(lJSVariable != null 
					&& rJSVariable != null) {
				if(lJSVariable.getType() != null
						&& rJSVariable.getType() != null) {
					if(lJSVariable.getType().equals("String")
							&& rJSVariable.getType().equals("String")) {
						return getLeftOperand()+".equals("+ getRightOperand() +")";
					}
				}
			}
		}
		return getLeftOperand()+" "+getOperator()+" "+getRightOperand();
	}

}
