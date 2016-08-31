// Check if JSON String : http://stackoverflow.com/questions/15124367/check-whether-the-string-is-a-valid-json-string-or-not
// Check if integer : http://stackoverflow.com/questions/16331423/whats-the-java-regular-expression-for-an-only-integer-numbers-string

package js2j;

import js2j.operators.JSAssignment;

public class JSVariable extends JSAssignment {
	
	private String type;
	protected String name;
		
	public JSVariable() {}
		
	public JSVariable(String name) {
		this.setType(null);
		this.setName(new JSStatement(name));
	}
	
	public JSVariable(String type, String name) {
		this.setType(type);
		this.setName(new JSStatement(name));
	}
	
	// Call superclass because JSStatement has a value => JSAssignment
	public JSVariable(String type, String name, JSStatement value) {
		super(new JSStatement(name),new JSStatement("="),value);
		this.setType(type);
		JSBlock parent;
		if((parent = (JSBlock) this.getParent(JSBlock.class)) == null) 
			JSScript.jsVariables.put(name,this.getValue());
		else
			parent.addVariable(this);
	}
	
	public String getName() {
		return super.getLeftOperand().toString();
	}
	
	public void setName(JSStatement name) {
		super.setLeftOperand(name);
	}
	
	public JSValue getValue() {
		return super.getRightOperand();
	}
		
	public void setValue(JSValue value) {
		super.setRightOperand(value);
	}

	/*
	 * (non-Javadoc)
	 * Used to bind dynamically the types of the variable
	 * while building all the elements associated to this 
	 * JSVariable. 
	 * 
	 * @see js2j.JSStatement#getType()
	 */
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public String getModifiers() {
		return "public static ";
	}
	
	public JSVariable(JSStatement jsStatement) {
		this.setParent(jsStatement.getParent());
//		System.out.println("JVAR PARENT: "+getParent().getClass());
		
		int var = jsStatement.toString().indexOf("var");
		String variableAsString = jsStatement.toString().substring(var+4);
		int eq = variableAsString.indexOf('=');
		JSStatement name = new JSStatement(variableAsString.substring(0, eq).trim()),
				value = new JSStatement(variableAsString.substring(eq+1, variableAsString.length()).trim());
		value.setParent(this);
		
//		if(jsStatement != null) //TODO Why to check if jsStatement == null
//			System.out.println("JSVariable parent: "+jsStatement.getParent());
		// Check if it's a String
		
		JSValue jsValue = null;
		if(value.toString().equals("null"))
			jsValue = null;
        // Check if it's a double array
		else if(JSValue.isDoubleArray(value.toString()))
        	jsValue = new JSDoubleArray(value);
        else if(JSValue.isDouble(value.toString()))
        	jsValue = new JSDouble(value);
        else if(JSValue.isString(value.toString()))
        	jsValue = new JSString(value);
        // Check if it's a char
        else if(JSValue.isCharArray(value.toString()))
        	jsValue = new JSCharArray(value);
        else if(JSValue.isInt(value.toString())) 
        	jsValue = new JSInt(value);
        // Check if it's an array of int
        else if(JSValue.isIntArray(value.toString()))
        	jsValue = new JSIntArray(value);
        // Check if it's an array of String
        else if(JSValue.isStringArray(value.toString()))
        	jsValue = new JSStringArray(value);
        // Check if it's a JSON String
        else if(JSValue.isJSONString(value.toString()))
        	jsValue = new JSJSONString(value);
        // Check if it's a Compact Statement
        else if(JSValue.isCompactStatement(value.toString()))
        	jsValue = new JSCompactStatement(value);
        // Check if it's a 
        else if(JSValue.isFunctionCall(value.toString()))
        	jsValue = new JSFunctionCall(value);
        setName(name);
        setValue(jsValue);
        JSScript.jsVariables.put(name.toString(),this);
	}
	
	@Override
	public String toString() {
		
		if(getParent() instanceof JSVariableList)
			return getName() +"="+getValue();
		/*
		 * [JSVariable] getType() use for dynamic binding type
		 */
		if(getType() != null)
			return getType() +" "+ getName() +"="+ getValue()+"; ";
		return getValue().getType() +" "+ getName() +"="+ getValue();
	}
}
