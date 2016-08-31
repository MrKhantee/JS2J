package js2j;

public class JSCatch extends JSBlock {
	public JSCatch(JSStatement jsStatement) {
		this.setFirstJSStatement(jsStatement);
		this.setParent(jsStatement.getParent());
		((JSBlock) getParent()).add(this);
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
		return "catch(Exception e) {}\n";
	}
}
