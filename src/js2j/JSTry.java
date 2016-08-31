package js2j;

public class JSTry extends JSBlock {
	
	public JSTry(JSStatement jsStatement) {
		super();
		this.setFirstJSStatement(jsStatement);
		this.setParent(jsStatement.getParent());
		// Add the try JSCatch JSBlock to its parent
		((JSBlock) getParent(JSBlock.class)).add(this);
	}

	@Override
	public boolean hasVariable(String variableName) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public JSStatement getVariableAs(String variableName) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String toString() {
		String jsTryCode = "try {\n";
		for(JSStatement jsStatement: getJSStatements()) {
			jsTryCode += "\t\t"+jsStatement;
		   	if(jsStatement instanceof JSVariable) 
		   		jsTryCode += "; ";
		   	jsTryCode += "\n";
		}
		return jsTryCode+"} ";
	}
	
}
