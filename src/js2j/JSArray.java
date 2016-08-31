// Extract word from string : http://stackoverflow.com/questions/19741189/extract-word-from-string-that-have-combination-of-letter-and-number-in-java

package js2j;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class JSArray extends JSValue {
	
	private int dimension = 1;
	
	public static boolean isMultidimensional(String variableArrayAsString) {
		if(variableArrayAsString == null)
			return false;
		return variableArrayAsString.matches("\\[(\\[)+(.*)\\](\\])+");
	}
	
	public static List<String> parse(String variableArrayAsString) {
		List<String> tmp = new ArrayList<String>();
		Pattern p = Pattern.compile("(\\[?([0-9],?)*\\]?)");
		Matcher m = p.matcher(variableArrayAsString);
		while (m.find()) {
			if(!m.group().matches("[\\[|\\]]")
					&& m.group().length() > 0)
				tmp.add(m.group());
		}
		return tmp;
	}
	
//	private ArrayList<String> builder = new ArrayList<String>();
	
//	public String buildAsString(Object o) {
//		if(JSArray.class.isAssignableFrom(o.getClass()))
//			buildAsString(((JSArray) o).getValue());
//		if(o instanceof JSVariable)
//			builder.add((String) ((JSVariable) o).getValue().toString());
//		else if(o instanceof String)
//			builder.add((String) o);
//		return ((getDimension() > 1) ? "{" : "") + StringUtils.join(builder, ',').replace("[", "{").replace("]", "}") + ((getDimension() > 1) ? "}" : "");
//	}

	public int getDimension() {
		return dimension;
	}

	private void setDimension(int i) {
		this.dimension = i;
	}
	
	protected void incrementDimension() {
		this.setDimension(getDimension()+1);
	}

	public void addJSValue(JSValue jsValue) {
		this.getValues().add(jsValue);
	}
	
	public ArrayList<JSValue> getValues() {
		return values;
	}

	public void setArrayCells(ArrayList<JSValue> values) {
		this.values = values;
	}
	
	private ArrayList<JSValue> values = new ArrayList<JSValue>();

}
