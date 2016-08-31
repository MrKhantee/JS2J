package js2j;

public class JSParameter extends JSVariable {
	
	public JSParameter(String name) {
		super(name);
		setValue(new JSStatement(name));
		// Type will be determined when Function will be called
	}
	
	@Override
	public String toString() {
		if(getType() != null) {
			if(getType().equals("JSONObject")) {
				System.out.println("PACKAGE ADD"+((JSFunction)getParent(JSFunction.class)));
				JSFunction jsFunction = ((JSFunction)getParent(JSFunction.class));
				if(jsFunction != null)
					jsFunction.addPackage(new JSPackage("org.json.simple.JSONObject"));
				JSJSONObjectElement jsJSONObjectElement = ((JSJSONObjectElement)getParent(JSJSONObjectElement.class));
				if(jsJSONObjectElement.getValue() != null)
					((JSFunction) jsJSONObjectElement.getValue()).addPackage(new JSPackage("org.json.simple.JSONObject"));
			}
		}
		return getType() +" "+ getName();	
	}
	
}
