package js2j;

public class JSDouble extends JSValue {
	
	public JSDouble(JSStatement jsStatement) {
		super(jsStatement);
	}
	
	@Override
	public String getType() {
		return "double";
	}
		
	@Override
	public String toString() {
		return this.getJsStatement()+"";
//		if(getParent(JSBlock.class) != null)
//			return getType() +" "+ getName() +" = "+ getValue() +"; ";
//		return getModifiers() + " "+ getType() +" "+ getName() +" = "+ getValue() +"; ";
	}
	
}
