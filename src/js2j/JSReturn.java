package js2j;

public class JSReturn extends JSStatement {
	
	private JSFunction jsFunction;

	public JSReturn(JSStatement jsStatement) {
		this.setJsStatement(jsStatement);
		this.setParent(jsStatement.getParent());
		// Add JSStatement to the block;
		((JSBlock) getParent(JSBlock.class)).add(this);
		// Set the Return of the parent JSFunction
		JSFunction jsFunction = (JSFunction) getParent(JSFunction.class);
		jsFunction.setReturn(this);
		this.setJsFunction(extractJSFunction(jsFunction));
	}

	public JSFunction getJsFunction() {
		return jsFunction;
	}

	public void setJsFunction(JSFunction jsFunction) {
		this.jsFunction = jsFunction;
	}
	
	private JSFunction extractJSFunction(JSFunction jsFunction) {
		if(jsFunction == null)
			return JSScript.jsFunctions.get(0);
		return jsFunction;
	}
	
	@Override
	public String getType() {
//		System.out.println("JSReturn: &&&&&"+((JSFunction)getParent(JSFunction.class)).getName());
		if(getValue() instanceof JSVariable
				&& ((JSVariable) getValue()).getValue() != null) /// WEIRD WHY CHECK NULL
			return ((JSVariable) getValue()).getValue().getType();
		return getValue().getType();
	}
	
	@Override
	public String toString() {
		if(getValue() instanceof JSVariable)
			return "return "+ "("+ ((JSVariable) getValue()).getValue().getType() +")"+ ((JSVariable) getValue()).getName() +"; ";
		return "return "+getValue()+"; ";
	}
}
