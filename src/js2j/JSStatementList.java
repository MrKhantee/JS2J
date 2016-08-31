package js2j;

import java.util.ArrayList;

public abstract class JSStatementList extends JSValue {
	
	public JSStatementList() {}
	
	public JSStatementList(ArrayList<JSStatement> jsStatements) {
		this.setJsStatements(jsStatements);
	}
	
	public int size() {
		return jsStatements.size();
	}
	
	public void add(JSStatement jsStatement) {
		this.jsStatements.add(jsStatement);
	}
	
	public JSStatement get(int i) {
		return this.jsStatements.get(i);
	}
	
	public ArrayList<JSStatement> getJSStatements() {
		return this.jsStatements;
	}
	
	void setJsStatements(ArrayList<JSStatement> jsStatements) {
		this.jsStatements = jsStatements;
	}
	
	private ArrayList<JSStatement> jsStatements = new ArrayList<JSStatement>();

//	@Override
//	public String toString() {
//		return StringUtils.join(getJSStatements(), ".");
//	}
	
}
