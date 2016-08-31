package js2j.operators;

import js2j.JSStatement;
import js2j.JSStatementList;

public class JSAssignmentList extends JSStatementList {
	
	public JSAssignmentList(JSStatement jsStatement) {
		this.setJsStatement(jsStatement);
		this.setParent(jsStatement.getParent());
		this.extractAssignments();
	}

	private void extractAssignments() {
		String[] jsAssignments = this.getJsStatement().toString().split(",");
		for(String jsAssignment: jsAssignments) {
			JSStatement jsBuiltStatement = new JSStatement(jsAssignment,this.getJsStatement().getLineNumber());
			jsBuiltStatement.setParent(this);
			jsBuiltStatement.build();
		}
		// Goto next JSStatement in JSScript
		this.getJsStatement().buildNext(getParent());
	}
}
