// Set first char of string to upper case

package js2j;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.text.WordUtils;

import js2j.operators.JSComparison;

public class JSTypeOf extends JSComparison {
	
	public JSTypeOf(JSStatement jsStatement) {
		this.setJsStatement(jsStatement);
		this.setParent(jsStatement.getParent());
		this.setOperator(new JSStatement("instanceof"));
		this.setOperands(jsStatement);
		if(getParent() instanceof JSIf) {
			System.out.println("****"+getParent().getClass());
			((JSIf) getParent()).setJSIfStatement(this);
		}
	}
	
	private static Pattern	c = Pattern.compile("\"(\\w+)\"\\s*([\\!=]=)\\s*typeof\\s*(\\w+)");
	
	private void setOperands(JSStatement jsStatement) {
		Matcher m = c.matcher(jsStatement.toString());
		// Set the operator of this JSComparison
		while(m.find()) {
			if(m.group(3).contains("!"))
				this.setNot(true);
			this.setRightOperand(new JSValue(new JSStatement(WordUtils.capitalize(m.group(1)))));
			this.setLeftOperand(new JSValue(new JSStatement(m.group(3))));
		}
	}
	
	
	public boolean isNot() {
		return isNot;
	}


	private void setNot(boolean isNot) {
		this.isNot = isNot;
	}

	private boolean isNot;
	
	@Override
	public String toString() {
		if(isNot())
			return "!("+ this.getLeftOperand() +" "+ this.getOperator() +" "+ this.getRightOperand() +")";
		return this.getLeftOperand() +" "+ this.getOperator() +" "+ this.getRightOperand();	
	}
	
}
