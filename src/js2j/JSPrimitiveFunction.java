package js2j;

public class JSPrimitiveFunction extends JSValue {
	
	public JSPrimitiveFunction(JSStatement jsStatement) {
		this.setJsStatement(jsStatement);
		this.setParent(jsStatement.getParent());
	}
	
	public String format() {
		String jsVariableType = null,
				s = this.getJsStatement().toString();
		JSPrimitiveFunctionEnum jsPrimitiveFunction = JSPrimitiveFunctionEnum.containsJSPrimitiveFunction(s); 
		if(s.indexOf('.') > 0) {
			String jsVariableString = s.substring(0, s.indexOf('.'));
			JSFunction jsF;
			if((jsF = (JSFunction) getParent(JSFunction.class)) != null) {
				JSVariable jsVariable = (JSVariable) jsF.getVariableAs(jsVariableString);
				jsVariableType = ((JSVariable)jsVariable).getType();
				return s.replace(s, jsVariableString+"."+jsPrimitiveFunction.getToJava(jsVariableType));
			}
		}
		return s.replace(jsPrimitiveFunction.name(), jsPrimitiveFunction.getToJava(jsVariableType));
	}
	
	@Override
	public String toString() {
		return format();
	}
	
}
