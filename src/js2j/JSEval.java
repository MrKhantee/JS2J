package js2j;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JSEval extends JSFunction {
	
	public JSEval(JSStatement jsStatement) {
		this.setJsStatement(jsStatement);
		this.setParent(jsStatement.getParent());
		this.setName("eval");
		String argument = extractArgument();
		
		System.out.println("JSEval: "+argument);
		System.out.println("JSEval: "+getParent().getClass());
		System.out.println("JSEval: "+((JSVariable)getVariable(argument)).getClass());
		System.out.println("JSEval: "+((JSVariable)getVariable(argument)).getValue().getType());
		JSStatement jsEvalFunctionCall = new JSStatement(getName().toUpperCase()+"."+this.getName()+"("+((JSVariable)getVariable(argument)).getName()+")");
		jsEvalFunctionCall.setType("String");
		getParent().setValue(jsEvalFunctionCall);
		
		this.buildRunnable();
	}
	
	private static Pattern	a = Pattern.compile("\\((.*)\\)");

	private String extractArgument() {
		Matcher m = a.matcher(getJsStatement().toString());
		while(m.find())
			return m.group(1);
		return null;
	}
	
	@Override
	public String toString() {
		String jsEvalCode = "";
		jsEvalCode += "\tpublic static String "+getName()+ "(String s) {\n";
		jsEvalCode += "\t\tswitch(s) {\n";
		// New case : location.hostname
		jsEvalCode += "\t\t\tcase \"location.hostname\":\n";
		jsEvalCode += "\t\t\t\treturn \"youtube-mp3.org\";\n";
		// Default case
		jsEvalCode += "\t\t\tdefault:\n";
		jsEvalCode += "\t\t\t\treturn \"\";\n";
		jsEvalCode += "\t\t}\n";

		return jsEvalCode += "\t}\n";
	}
	
}
