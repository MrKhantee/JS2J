package js2j;

import org.apache.commons.lang3.StringUtils;

public class JSDoubleArray extends JSArray {

	public JSDoubleArray(JSStatement jsStatement) {
		setParent(jsStatement.getParent());
		
		System.out.println("DOUBLEARRAY:"+jsStatement);
		
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
				if(JSArray.isDoubleArray(arrayAsString)) {
					JSStatement builtJSStatement = new JSStatement(arrayAsString);
					builtJSStatement.setParent(this);
					new JSDoubleArray(builtJSStatement);
				}
			}
		}
	}
	
	@Override
	public String getType() {
		return "double"+StringUtils.repeat("[]", getDimension());
	}
	
	@Override
	public String toString() {
		if(getDimension() > 1)
			return "{"+StringUtils.join(getValues(), ",")+"}";
		return "{"+getValue()+"}";
	}
}
