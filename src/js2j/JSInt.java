package js2j;

public class JSInt extends JSValue {
	
	public JSInt(JSStatement jsStatement) {
		super(jsStatement);
	}
	
	@Override
	public String getType() {
		return "int";
	}
		
	@Override
	public String toString() {
		return this.getJsStatement()+"";
//		if(getParent(JSBlock.class) != null)
//			return getType() +" "+ getName() +" = "+ getValue() +"; ";
//		return getModifiers() + " "+ getType() +" "+ getName() +" = "+ getValue() +"; ";
	}
	
}
