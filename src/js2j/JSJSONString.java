package js2j;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JSJSONString extends JSString {
	
	public JSJSONString(JSStatement jsStatement) {
		this.setParent(jsStatement.getParent());
		JSStatement builtJSStatement = new JSStatement(JSJSONString.encode(jsStatement.toString()));
		builtJSStatement.setParent(jsStatement.getParent());
		this.setValue(builtJSStatement);
//		getValueType();
		((JSFunction)this.getParent(JSFunction.class)).addPackage(new JSPackage("org.json.simple.JSONObject"));
		((JSFunction)this.getParent(JSFunction.class)).addPackage(new JSPackage("org.json.simple.JSONValue"));
	}
	
	static String encode(String value) {
		return value.replace("\"", "\\\""); 
	}
	
	@Override 
	public String getType() {
		return "JSONObject";
	}
	
	public String getValueType() {
		String jsJSONStringDecoded = JSJSONString.decode(this.getValue().toString());
		Matcher m = Pattern.compile("\"\\w\":(\\d+)").matcher(jsJSONStringDecoded);
		if(m.groupCount() > 0)
			return "long"; // If return int throw an ClassCastException when calling get method from a JSONObject
		m = Pattern.compile("\"\\w\":\"(.*)\"").matcher(jsJSONStringDecoded);
		System.out.println(jsJSONStringDecoded);
		if(m.groupCount() > 0)
			return "String";
		return "";
	}
	
	@Override
	public String toString() {
		return "(JSONObject) JSONValue.parse(\""+this.getValue()+"\")";
	}
	
	public static String decode(String value) {
		return value.substring(1, value.length()-1).replace("\\\"", "\"");
	}
	
}
