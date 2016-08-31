package js2j;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JSValue extends JSStatement {
	
	public JSValue() {
	}
	
	public JSValue(JSStatement jsStatement) {
		this.setJsStatement(jsStatement);
	}
	
	public void setValue(JSStatement jsStatement) {
		this.setJsStatement(jsStatement);
	}
	
	public JSStatement getValue() {
		return this.getJsStatement();
	}
		
	public static boolean isString(String variableAsString) {
		if(variableAsString == null)
			return false;
		return variableAsString.matches("\"(.*)\"");
	}
	
	public static boolean isCharArray(String variableAsString) {
		if(variableAsString == null)
			return false;
		return variableAsString.matches("\\[(\\[?('.',?)*\\]?,?)+\\]");
	}
	
	public static boolean isInt(String variableAsString) {
		if(variableAsString == null)
			return false;
		return variableAsString.matches("^[-+]?\\d+$");
	}
	
	public static boolean isIntArray(String variableAsString) {
		if(variableAsString == null)
			return false;
		return variableAsString.matches("\\[(\\[?([0-9],?)+\\]?,?)+\\]");
	}
	
	public static boolean isStringArray(String variableAsString) {
		if(variableAsString == null)
			return false;
		return variableAsString.matches("\\[(\\[?(\".\",?)+\\]?,?)+\\]");
	}
	
	public static boolean isDouble(String variableAsString) {
		if(variableAsString == null)
			return false;
		return variableAsString.matches("^[-+]?\\d+\\.\\d+$");
	}
	
	public static boolean isDoubleArray(String variableAsString) {
		if(variableAsString == null)
			return false;
		return variableAsString.matches("\\[(\\[?([0-9]+\\.[0-9]+,?)+\\]?,?)+\\]");
	}
	
	public static boolean isArray(String variableAsString) {
		if(variableAsString == null)
			return false;
		return variableAsString.matches("\\w\\[(.*)\\]");
	}
	
	public static boolean isJSONString(String variableAsString) {
		JSONParser parser = new JSONParser();
		try { 
            parser.parse(variableAsString); 
		} catch (ParseException e) {
			return false;
		}
	    return true;
	}
	
	public static boolean isCompactStatement(String variableAsString) {
		return variableAsString.matches("=?(.*)\\[(.*)\\]\\((.*)\\)");
	}
	
	public static boolean isFunctionCall(String variableAsString) {
		if(variableAsString.indexOf('(') < 0)
			return false;
		String functionName = variableAsString.substring(0, variableAsString.indexOf('('));
		return JSScript.jsFunctions.containsKey(functionName);
	}
	
	@Override
	public String toString() {
		return getJsStatement()+"";
	}
	
}
