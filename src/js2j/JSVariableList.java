// Regexp, if not preceded : http://www.regular-expressions.info/lookaround.html

package js2j;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

public class JSVariableList extends JSStatementList {

	public JSVariableList() {}

	public JSVariableList(JSStatement jsStatement) {
		this.setJsStatement(jsStatement);
		this.setParent(jsStatement.getParent());		
		// Add JSVariableList to the first JSBlock parent;
		((JSBlock) getParent(JSBlock.class)).add(this);
				
//		System.out.println("Variable List parent: "+((JSFunction)getParent()).getName());
				
		int var = jsStatement.toString().indexOf("var");
		String variableListAsString = jsStatement.toString().substring(var+4);
		
		Iterator<Entry<String,String>> it = split(variableListAsString).entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry<String,String> pairs = (Map.Entry<String,String>)it.next();
	        
	        JSStatement builtJSStatement = new JSStatement("var "+pairs.getKey()+"="+pairs.getValue());
	        builtJSStatement.setParent(this);
	        
	        JSVariable jsVariable;
	        
	        // Add the current variable to this JSVariableList
	        add(jsVariable = new JSVariable(builtJSStatement));
	        
	        if(getParent() == null)
	        	JSScript.jsVariables.put(jsVariable.getName(),jsVariable);
	        
	        // Set the parent of the variable
	        it.remove(); // avoids a ConcurrentModificationException
	    }
	}
	
	public static LinkedHashMap<String,String> split(String jsStatementAsString) {
		int b = 0, sb = 0, cb = 0;
		ArrayList<Integer> cutPoints = new ArrayList<Integer>();
		LinkedHashMap<String,String> variableList = new LinkedHashMap<String,String>();

		for(int i=0; i<jsStatementAsString.length(); i++) {
			switch(jsStatementAsString.charAt(i)) {
				case '(': b++; break;
				case ')': b--; break;
				case '[': sb++; break;
				case ']': sb--; break;
				case '{': cb++; break;
				case '}': cb--; break;
				case ',': 
					if(b == 0 && sb == 0 && cb == 0)
						cutPoints.add(i);
					break;
				default: break;
			}
		}
		
		System.out.println(cutPoints.size());
		String[] variables = new String[cutPoints.size()+1];

		if(cutPoints.size() > 0) {
			for(int i = 0; i<=cutPoints.size(); i++) {
				if(i == 0)
					variables[i] = jsStatementAsString.substring(0, cutPoints.get(i));
				else if(i == cutPoints.size())
					variables[i] = jsStatementAsString.substring(cutPoints.get(i-1)+1);
				else
					variables[i] = jsStatementAsString.substring(cutPoints.get(i-1)+1, cutPoints.get(i));
			}
		}
		for(String variable: variables) {
			int eqPos = variable.indexOf('=');
			if(eqPos > 0)
				variableList.put(variable.substring(0, eqPos),variable.substring(eqPos+1));
			else
				variableList.put(variable,null);
		}
		return variableList;
	}
	
	public boolean areSameVariableType() {
		String type = ((JSVariable) this.getJSStatements().get(0)).getValue().getType();
		for(JSStatement jsStatement: getJSStatements()) {
			JSValue jsValue = ((JSVariable) jsStatement).getValue();
			if(jsValue == null)
				continue;
			if(!jsValue.getType().equals(type))
				return false;
		}
		return true;
	}
	
	public JSStatement getVariableAs(String variableName) {
		for(JSStatement jsStatement: getJSStatements()) {
			JSVariable jsVariable = (JSVariable) jsStatement;
			if(jsVariable.getName().equals(variableName)) {
				return jsVariable;
			}
		}
		return null;
	}
	
	@Override
	public String toString() {
		if(areSameVariableType()) {
			String type = ((JSVariable) getJSStatements().get(0)).getValue().getType();
			String variableList = (StringUtils.join(getJSStatements(), ",\n\t\t"));
			return type +" "+variableList + "; ";
		} else {
			String variableList = "";
			for(JSStatement jsStatement: getJSStatements()) {
				JSVariable jsVariable = (JSVariable) jsStatement;
				/*
				 * [JSVariable] getType() use for dynamic binding type
				 */
//				System.out.println(jsVariable.getName()+"-!-"+jsVariable.getType());
				if(jsVariable.getType() != null)
					variableList += "\t\t"+jsVariable.getType() +" "+jsVariable+ "; \n";
				else
					variableList += "\t\t"+jsVariable.getValue().getType() +" "+jsVariable+ "; \n";
			}
			return variableList;
		}
	}
}
