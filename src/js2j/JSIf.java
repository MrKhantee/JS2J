package js2j;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import js2j.utils.StringUtil;

public class JSIf extends JSBlock {
		
	public JSIf(JSStatement jsStatement) {
		super(jsStatement);
		this.setJsStatement(jsStatement);
		this.setFirstJSStatement(jsStatement);
		this.setParent(jsStatement.getParent());
		
		System.out.println("°°°"+getParent(JSBlock.class).getClass());
		((JSBlock) getParent(JSBlock.class)).add(this);
		
		String jsConditionStatements = extractConditionStatement(jsStatement);
		System.out.println("IFCONDITION:"+jsConditionStatements);
		JSStatement builtJSStatement = new JSStatement(jsConditionStatements,jsStatement.getLineNumber());
		builtJSStatement.setParent(this);
		builtJSStatement.build();
		
		if(jsStatement.toString().indexOf('{') < 0) {
			this.setOneLineBlock(true);
		}
		System.out.println("FIRST IN IF: "+this.getFirstJSStatement());
		// Goto the first JSStatement in the If block
		builtJSStatement = new JSStatement(this.getFirstStatementInIf(),jsStatement.getLineNumber());
		builtJSStatement.setParent(this);
		builtJSStatement.build();
	}
	
	private static Pattern	i = Pattern.compile("if\\((.*)\\)\\{?(\\w.*)");
	
	private String extractConditionStatement(JSStatement jsStatement) {
//		setJSIfJSStatementList(new JSIfJSStatementList(getNbLogicalOperators(jsStatement)+1));
//		System.out.println("JSIF SIZE:"+this.getJSIfStatementList().size());
		Matcher m = i.matcher(jsStatement.toString());
		while(m.find())
			return m.group(1);
		return null;
	}
	
	public String getFirstStatementInIf() {
		Matcher m = i.matcher(this.getFirstJSStatement().toString());
		while(m.find())
			return m.group(2);
		return null;
	}
	
	public JSStatement getJSIfStatement() {
		return this.jsIfStatement;
	}
	
	public void setJSIfStatement(JSStatement jsIfStatement) {
		// Remove the first null element
//		this.getJSIfStatementList().getJSStatements().remove(0);
//		this.getJSIfStatementList().add(jsStatement);
		this.jsIfStatement = jsIfStatement;
	}
	
	private JSStatement jsIfStatement;

	@Override
	public boolean hasVariable(String variableName) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public JSStatement getVariableAs(String variableName) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override 
	public String toString() {
		if(getJSIfStatement().isFunctionCall()) {
			String functionName = StringUtil.extractName(this.getJSIfStatement());
			JSFunction jsFunction = JSScript.jsFunctions.get(functionName);
			String[] functionParameters = StringUtil.extractParameters(getJSIfStatement());
			ArrayList<String> dynamicBindingTypes = new ArrayList<String>();
			for(String parameter: functionParameters) {
				JSStatement jsParameter = new JSStatement(parameter);
				jsParameter.setParent(getParent());
				dynamicBindingTypes.add(jsParameter.typeOf());
			}
			int i = 0;
			for(JSParameter jsParameter: jsFunction.getParameters()) {
				jsParameter.setType(dynamicBindingTypes.get(i));
				i++;
			}
			// Build a Java Runnable of the JSFunction
			jsFunction.buildRunnable();
		}
		String jsIfCode = "if("+ getJSIfStatement() +") {\n";
		for(JSStatement jsStatement: getJSStatements()) {
			jsIfCode += "\t\t\t"+jsStatement+"\n";
		}
		return jsIfCode +"\t\t}";
	}

}
