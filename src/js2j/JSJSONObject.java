package js2j;

public class JSJSONObject extends JSBlock {
		
	public JSJSONObject(JSStatement jsStatement) {
		super();
		this.setFirstJSStatement(jsStatement);
		this.setName(extractName(jsStatement.toString()));
		JSScript.jsVariables.put(getName(), this);
		// Register the Assignable JSBlock
		System.out.println("JSJSONObject: "+getName());
	}
	
	protected static String extractName(String sttmnt) {
		int eq = sttmnt.indexOf('='),
				var = sttmnt.indexOf("var");
		// Check if var is present before =
		if(eq > 0 && var < 0) 
			return sttmnt.substring(0, eq);
		else if(eq > 0 && var > 0)
			return sttmnt.substring(var+4, eq);
		return null;
	}

	@Override
	public boolean hasVariable(String variableName) {
		return getVariableAs(variableName) != null;
	}

	@Override
	public JSStatement getVariableAs(String variableName) {
		if(this.getVariables().containsKey(variableName))
			return getVariables().get(variableName);
		return null;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private String name;


}
