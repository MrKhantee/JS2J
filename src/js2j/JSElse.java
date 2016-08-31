package js2j;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JSElse extends JSBlock {
		
	public JSElse(JSStatement jsStatement) {
		super(jsStatement);
		this.setFirstJSStatement(jsStatement);
		this.setParent(jsStatement.getParent());
		
		System.out.println("°°°"+getParent(JSBlock.class).getClass());
		((JSBlock) getParent(JSBlock.class)).add(this);
		
		JSStatement builtJSStatement = new JSStatement(getFirstStatementInElse(jsStatement),jsStatement.getLineNumber());
		builtJSStatement.setParent(this);
		builtJSStatement.build();
		
		if(jsStatement.toString().indexOf('{') > 0) {
			
		} else {
			
		}

	}
	
	private static Pattern	e = Pattern.compile("else\\s*\\{?(.*)");
	
	public String getFirstStatementInElse(JSStatement jsStatement) {
		Matcher m = e.matcher(jsStatement.toString());
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
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override 
	public String toString() {
		String jsIfCode = "else {\n";
		for(JSStatement jsStatement: getJSStatements()) {
			jsIfCode += "\t\t\t"+jsStatement+"\n";
		}
		return jsIfCode +"\t\t}";
	}

}
