package js2j;

import org.apache.commons.lang3.StringUtils;

public class JSStringArray extends JSArray {

	public JSStringArray(JSStatement jsStatement) {		
		if(!JSArray.isMultidimensional(jsStatement.toString())) {
			String arrayElements = jsStatement.toString().substring(1, jsStatement.toString().length()-1);
			JSStatement builtJSStatement = new JSStatement(arrayElements);
			setValue(builtJSStatement);
			// If its parent is a cell array then add a new cell
			if(getParent() instanceof JSArray)
				((JSArray) getParent()).addJSValue(this);
		} else {
			((JSArray) getTopParent()).incrementDimension();
			for(String arrayAsString: parse(jsStatement.toString())) {
				if(JSValue.isStringArray(arrayAsString)) {
					JSStatement builtJSStatement = new JSStatement(arrayAsString);
					builtJSStatement.setParent(this);
					new JSStringArray(builtJSStatement);
				}
			}
		}
	}
	
	@Override
	public String getType() {
		return "String"+StringUtils.repeat("[]", getDimension());
	}
	
	@Override
	public String toString() {
		if(getDimension() > 1)
			return "{"+StringUtils.join(getValues(), ",")+"}";
		return "{"+getValue()+"}";
	}

}
