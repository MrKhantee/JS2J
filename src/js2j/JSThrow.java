package js2j;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JSThrow extends JSStatement {
	
	public JSThrow(JSStatement jsStatement) {
		this.setJsStatement(jsStatement);
		this.setParent(jsStatement.getParent());
		((JSBlock) getParent()).add(this);
		this.setMessage(this.extractMessage());
	}
	
	private static Pattern m = Pattern.compile("throw Error\\(\"(.*)\"\\)");
	
	public String extractMessage() {
		Matcher ma = m.matcher(this.getJsStatement().toString());
		while(ma.find())
			return ma.group(1);
		return null;
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	private String message;
	
	@Override
	public String toString() {
		return "throw new IllegalArgumentException(\""+ this.getMessage() +"\");";
	}
	
}
