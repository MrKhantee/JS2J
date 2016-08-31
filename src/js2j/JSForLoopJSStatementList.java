package js2j;

import org.apache.commons.lang3.StringUtils;

public class JSForLoopJSStatementList extends JSStatementList {
	
	public JSForLoopJSStatementList() {
	}
	
	public JSStatement getJSFirstStatement() {
		return this.get(0);
	}
	
	@Override
	public String toString() {
		return StringUtils.join(getJSStatements(), "; ");
	}
}
