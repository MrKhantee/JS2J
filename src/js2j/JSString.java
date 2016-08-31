package js2j;

public class JSString extends JSValue {
	
	public JSString() {}
	
	public JSString(JSStatement jsStatement) {
		super(new JSStatement(format(jsStatement.toString())));
//		System.out.println(">>>"+this);
//		System.out.println("PAR STRINg:"+this+"  "+jsStatement.getParent().getClass());
//		System.out.println("çççç:"+format(jsStatement.toString()));
		setParent(jsStatement.getParent());
		// Set value of its parent
		if(getParent() != null) {
			getParent().setValue(this);
		}
	}
	
	public static String format(String value) {
		return value.substring(1, value.length()-1);
	}
	
	@Override 
	public String getType() {
		return "String";
	}
	
	@Override
	public String toString() {
		return "\""+getJsStatement()+"\"";
	}
}
