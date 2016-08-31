package js2j;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JSJSONObjectElement extends JSVariable {
	
	public JSJSONObjectElement() {}
	
	public JSJSONObjectElement(String name, JSStatement value) {
		super("JSONObjectElement",name,value);
	}
	
	public JSJSONObjectElement(JSStatement jsStatement) {
		super(extractName(jsStatement));
		System.out.println(getName()+"oooo"+jsStatement.getParent().getClass());
		this.setJsStatement(jsStatement);
		this.setParent(jsStatement.getParent(JSJSONObject.class));
		System.out.println("JSJSONObjectElement Parent: "+jsStatement.getParent(JSJSONObject.class));
		((JSJSONObject) getParent()).addVariable(this);
		JSStatement builtJSStatement = new JSStatement(extractValue(),jsStatement.getLineNumber());
		builtJSStatement.setParent(this);
		builtJSStatement.build();
	}
	
	private static Pattern p = Pattern.compile("\\}?,?[\"|'](.*)[\"|']:(.*)");

	private static String extractName(JSStatement jsStatement) {
		Matcher m = p.matcher(jsStatement.toString());
		while(m.find()) {
			return m.group(1); 
		}
		return null;
	}
	
	private String extractValue() {
		Matcher m = p.matcher(this.getJsStatement().toString());
		while(m.find())
			return m.group(2); 
		return null;
	}
	
}
