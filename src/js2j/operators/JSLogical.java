package js2j.operators;

import java.util.regex.Pattern;

import js2j.*;

public class JSLogical extends JSOperator {
	
	private static Pattern o = Pattern.compile("(.*)(\\|{2}|&{2}|\\!)(.*)");
	
	public JSLogical(JSStatement jsStatement) {
		this.setJsStatement(jsStatement);
		this.setParent(jsStatement.getParent());
		
		if(isJSIfStatement())
			((JSIf) getParent()).setJSIfStatement(this);
		
		this.setPattern(o);
		this.extractOperator();
		this.extractOperands();
	}
		
//	private void extractOperators() {		
//		Matcher m = o.matcher(this.getJsStatement().toString());
//		while(m.find())
//			this.addOperator(new JSStatement(m.group(1)));
//	}
//	
//	private void extractOperands() {
//		String[] elements = this.getJsStatement().toString().split("[\\|{2}|&{2}|\\!]");
//		for(String elementAsString: elements) {
//			if(elementAsString.length()>0) {
//				JSStatement builtJSStatement = new JSStatement(elementAsString,this.getJsStatement().getLineNumber());
//				builtJSStatement.setParent(this);
//				if(!builtJSStatement.build()) {
//					//TODO
//				}
//			}
//		}
//	}
//	
//	public void addOperator(JSStatement operator) {
//		this.getOperators().add(operator);
//	}
//	
//	public ArrayList<JSStatement> getOperators() {
//		return operators;
//	}
//
//	public void setOperators(ArrayList<JSStatement> operators) {
//		this.operators = operators;
//	}	
//	
//	private ArrayList<JSStatement> operators = new ArrayList<JSStatement>();
//	
//	@Override
//	public String toString() {
//		String jsComputation = "";
//		int i=0;
//		for(JSStatement jsStatement: this.getJSStatements()) {
//			jsComputation += jsStatement;
//			if(i<getOperators().size())
//				jsComputation += this.getOperators().get(i);
//			i++;
//		}
//		return jsComputation;
//	}
	
	public String toString() {
		return this.getLeftOperand() +""+ this.getOperator() +""+ this.getRightOperand();
	}
	
}
