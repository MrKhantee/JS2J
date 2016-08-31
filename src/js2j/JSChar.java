package js2j;

public class JSChar extends JSVariable {
	public JSChar(String name, String value) {
		super("char",name,new JSStatement(value));
	}
	
	@Override
	public String toString() {
		return getModifiers() + getType() +" "+ getName() +" = "+ getValue() +"; ";
	}
}
