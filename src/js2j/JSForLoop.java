package js2j;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import js2j.operators.JSAssignment;

public class JSForLoop extends JSBlock {
	
	public JSForLoop(JSStatement jsStatement) {
		super();
		this.setJsStatement(jsStatement);
		System.out.println("FOR PARENT: "+jsStatement.getParent().getClass());
		// Add JSForLoop to its parent JSBlock
		((JSBlock) jsStatement.getParent(JSBlock.class)).add(this);
		this.setParent(jsStatement.getParent());
		this.extractParameters();
		
		if(jsStatement.toString().indexOf('{') < 0) {
			this.setOneLineBlock(true);
		}
	}
	
	public JSStatement getJSFirstStatement() {
		return this.getJSForLoopStatementList().getJSFirstStatement();
	}
	
	public void addJSForLoopStatement(JSStatement jsStatement) {
//		jsStatement.setParent(this); >> StackOverflow Error
		this.getJSForLoopStatementList().add(jsStatement);
	}
	
	public JSForLoopJSStatementList getJSForLoopStatementList() {
		return this.jsStatementList;
	}
	
	private void setJSForLoopJSStatementList(JSForLoopJSStatementList jsStatementList) {
		this.jsStatementList = jsStatementList;
	}
	
	JSForLoopJSStatementList jsStatementList;
	
	private void extractParameters() {
		setJSForLoopJSStatementList(new JSForLoopJSStatementList());
		String statement1 = this.getJsStatement().toString().substring(getJsStatement().toString().indexOf('(')+1);
		JSStatement jsStatement = new JSStatement(statement1,getJsStatement().getLineNumber());
//		System.out.println("JSForLoop1Statement: "+getParent());
		jsStatement.setParent(this);
		jsStatement.build();
	}
	
	private static Pattern	i = Pattern.compile("\\w+[+|-]+\\d?\\)\\{?(\\w.*)");
	
	public static String getFirstStatementInForLoop(JSStatement jsStatement) {
		Matcher m = i.matcher(jsStatement.toString());
		while(m.find())
			return m.group(1);
		return null;
	}

	@Override
	public boolean hasVariable(String variableName) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public JSStatement getVariableAs(String variableName) {
		if(getJSFirstStatement() instanceof JSAssignment) {
			if( ( ( JSAssignment ) getJSFirstStatement() ).getLeftOperand().toString().equals( variableName ) )
				return this.getJSFirstStatement();
		}
		if(getJSFirstStatement() instanceof JSVariable) {
			if( ( ( JSVariable ) getJSFirstStatement() ).getName().equals( variableName ) )
				return getJSFirstStatement();
		}
		return null;
	}
	
	@Override
	public String toString() {
		String jsForLoopCode = "";
		jsForLoopCode += "for("+getJSForLoopStatementList()+") {\n";
		for(JSStatement jsStatement: getJSStatements()) {
			jsForLoopCode += "\t\t\t"+jsStatement+"\n";
		}
		return jsForLoopCode += "\t\t}";
	}
}
