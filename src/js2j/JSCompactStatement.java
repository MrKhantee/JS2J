// Check if enum exists: http://stackoverflow.com/questions/1167982/check-if-enum-exists-in-java

package js2j;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import js2j.operators.JSAssignment;
import js2j.operators.JSComparison;
import js2j.utils.StringUtil;

public class JSCompactStatement extends JSValue {
	
	public JSCompactStatement(JSStatement jsStatement) {
		this.setJsStatement(jsStatement);
		this.setParent(jsStatement.getParent());
		System.out.println("JSCompactStatement PARENT: "+getParent().getClass());
		
		/*
		 * PREFORMAT because is not working only with REGEXP when for instance
		 * L[b0I[P3](1,2)]
		 */
		String s = getJsStatement().toString();
		if(s.endsWith("]")) {
			String b = s.substring(0, s.indexOf('['));
			variables1.add(b);
			s = s.substring(s.indexOf('[')+1, s.lastIndexOf(']'));
		}
		parse(s);
		this.setUnzippedJSStatement(unZip());
		
		System.out.println("JSCOMPACTSTATEMENT parent: "+getParent().getClass());
		
		if(getParent() instanceof JSComparison) {
			((JSComparison) this.getParent()).setLeftOperand(this); 
		}
		
		else if(getParent() instanceof JSAssignment) {
			((JSAssignment) this.getParent()).setRightOperand(this); 
		}
		
		else if(getParent() instanceof JSAssignment) {
			if(((JSAssignment) getParent()).getLeftOperand() != null) {
				String leftOperand = ((JSAssignment) getParent()).getLeftOperand().toString();
				JSVariable jsVariable = (JSVariable) getVariable(leftOperand);
				String variableType = jsVariable.getValue().getType();
				if(this.getUnzippedJSStatement().toString().indexOf('(') > 0) {
					String functionName = this.getUnzippedJSStatement().toString().substring(0, this.getUnzippedJSStatement().toString().indexOf('('));
					if(JSScript.jsFunctions.containsKey(functionName)) {
						JSFunction jsFunction = JSScript.jsFunctions.get(functionName);
						String type = jsFunction.getReturn().getType(); // Put this in typeOf() method
						if(type != null) {
							if(JSPrimitiveType.asJSPrimitiveType(variableType).getPowerIndex() 
								< JSPrimitiveType.asJSPrimitiveType(type).getPowerIndex()) {
								jsVariable.setType(type);
								int argPos;
								if((argPos = StringUtil.hasAsArgument(jsVariable.getName(), this.getUnzippedJSStatement().toString())) > -1) {
	//								System.out.println("BONJOUR: "+argPos);
									jsFunction.getParameters().get(argPos).setType(type);
	//								System.out.println(jsFunction.getParameters().get(argPos));
									jsFunction.buildRunnable();
								}
							}
						}
					}
				}
			}
		}
	}
	
	public JSCompactStatement(String zippedString) {		
		String s = zippedString.toString();
		this.setJsStatement(new JSStatement(zippedString));
		if(s.endsWith("]")) {
			String b = s.substring(0, s.indexOf('['));
			variables1.add(b);
			s = s.substring(s.indexOf('[')+1, s.lastIndexOf(']'));
		}
		parse(s);
	}
	
	private ArrayList<String> variables1 = new ArrayList<String>(); // Variables before [
	private ArrayList<String> variables2 = new ArrayList<String>(); // Variables in [] or ()
	
	private static Pattern	v = Pattern.compile("^\\(?(\\w+)?\\[(.*?)\\]\\((.*?)\\)(?!\\))(.*)");
	
	public void parse(String s) {
		Matcher m = v.matcher(s);
		ArrayList<String> elements = new ArrayList<String>();
		while(m.find())
			for(int i=1;i<=m.groupCount();i++)
				elements.add(m.group(i));
		if(elements.size() > 0) {
			variables1.add(elements.get(0));
			variables2.add(elements.get(1));
			String thirdElement = elements.get(2);
			String fourthElement = elements.get(3);
			
			if(isSplittable(thirdElement))
				for(String argument: splitArguments(thirdElement)) {
					parse(argument);
				}
			if(fourthElement != null)
				parse(fourthElement);
		} else {
			variables2.add(s);
		}
	}
	
	public static boolean isSplittable(String s) {
		return s.indexOf(',') > 0;
	}
	
	public static String[] splitArguments(String arguments) {
		int b = 0, cutPoint = 0;
		for(int i=0; i<arguments.length(); i++) {
			if(arguments.charAt(i) == '(')
				b++;
			if(arguments.charAt(i) == ')')
				b--;
			if(arguments.charAt(i) == ','
					&& b == 0)
				cutPoint = i;
		}
		if(cutPoint > 0)
			return new String[] {
					arguments.substring(0, cutPoint),
					arguments.substring(cutPoint+1)	
			};
		return new String[] {arguments};
	}
	
	public JSStatement unZip() {
		String unzippedJSCompactStatement = getJsStatement().toString();
		
//		System.err.println("Unzipped0: "+unzippedJSCompactStatement);
		for(String variable: variables2) {
//			System.out.println("variables2: "+variable);

			JSPrimitiveFunctionEnum jsPrimitiveFunction;
			String realKey;
			
			if(variable.indexOf('.') > 0)
				continue;
			if((jsPrimitiveFunction = JSPrimitiveFunctionEnum.asJSPrimitiveFunction(realKey = getRealKey(variable))) != null) {
				unzippedJSCompactStatement = unzippedJSCompactStatement.replace("["+variable+"]", "."+jsPrimitiveFunction.getToJava(null));
				unzippedJSCompactStatement = JSPrimitiveFunctionEnum.translate(unzippedJSCompactStatement, jsPrimitiveFunction);
				if(getParent() instanceof JSVariable) {
					( ( JSVariable ) getParent() ).setType(jsPrimitiveFunction.getReturnType());
				}
			} else if(realKey == null)
				continue;
			else
				unzippedJSCompactStatement = unzippedJSCompactStatement.replace("["+variable+"]", "["+realKey+"]");
//			System.out.println("variables2: "+variable+">>>>>>"+realKey);
			
		}
		
		System.err.println("Unzipped1: "+unzippedJSCompactStatement);
		
		for(String variable: variables1) {
			JSStatement var = getVariable(variable);
			
//			System.out.println("variables1: "+ variable + ">>>>>>");
			
			if(!( var instanceof JSJSONObject ))
				continue;
			
			String key = extractKey(variable,unzippedJSCompactStatement);
			String[] arguments = extractArguments(variable,unzippedJSCompactStatement);
			
//			System.out.println("PARENT: "+parent+" "+arguments.length);
//			
//			System.out.println("KEY: "+key);
			
			JSJSONObjectElement jsJSONObjectElement = (JSJSONObjectElement) ((JSJSONObject) var).getVariableAs(key);
			JSFunction jsFunction = (JSFunction) jsJSONObjectElement.getValue();

			int i = 0;
			for(String argument: arguments) {
//				System.out.println("ARG: "+i+">"+argument);
				JSPrimitiveFunctionEnum jsPrimitiveFunction;
				
//				System.out.println("ARG TYPE: "+new JSStatement(argument).typeOf());
				String type = new JSStatement(argument).typeOf();
				
				// Check if there's a calling to a JSONObject element
				if(type != null) {
					if(type.equals("JSONObject")) {
						System.out.println("JSON DETECTED");
						if(argument.indexOf('[') > 0) {
							String jsJSONVariable = argument.substring(0, argument.indexOf('['));
							String jsJSONArgument = argument.substring(argument.indexOf('[')+1, argument.indexOf(']'));
							JSJSONString jsJSONString = (JSJSONString) ((JSVariable)getVariable(jsJSONVariable)).getValue();						
							String jJSONVariableCall = "("+ jsJSONString.getValueType() +")"+ jsJSONVariable+".get("+ jsJSONArgument +")";
							unzippedJSCompactStatement = unzippedJSCompactStatement.replace(argument, jJSONVariableCall);
							jsFunction.getParameters().get(i).setType(jsJSONString.getValueType());
						}
					} else {
						jsFunction.getParameters().get(i).setType(type);
					}
				}
				// Create temporary variable to get its type
				else if((jsPrimitiveFunction = JSPrimitiveFunctionEnum.containsJSPrimitiveFunction(argument)) != null) {
					String jsVariableType = null;
					if(argument.indexOf('.') > 0) {
						String jsVariableString = argument.substring(0, argument.indexOf('.'));
						
						JSFunction jsF;
						if((jsF = (JSFunction) getParent(JSFunction.class)) != null) {
							JSVariable jsVariable = (JSVariable) jsF.getVariableAs(jsVariableString);
//							System.out.println(">>>"+jsVariableString);
//							System.out.println("PR:"+jsF.getName());
//							System.out.println("PARAMS:"+jsF.getParameters());
//							System.out.println("PRIMITIVE:"+((JSVariable)jsVariable));
							if(jsVariable != null) {
								jsVariableType = ((JSVariable)jsVariable).getType();
								unzippedJSCompactStatement = unzippedJSCompactStatement.replace(argument, jsVariableString+"."+jsPrimitiveFunction.getToJava(jsVariableType));
							}
						}
					} else 
						unzippedJSCompactStatement = unzippedJSCompactStatement.replace(jsPrimitiveFunction.name(), jsPrimitiveFunction.getToJava(jsVariableType));
					unzippedJSCompactStatement = JSPrimitiveFunctionEnum.translate(unzippedJSCompactStatement, jsPrimitiveFunction);
					jsFunction.getParameters().get(i).setType(jsPrimitiveFunction.getReturnType());
				}
//				System.out.println("ARGUMENT TYPE: "+argType);
				i++;
			}
			
			String functionName = variable+"_"+key;
			functionName = functionName.toUpperCase()+"."+functionName;
			unzippedJSCompactStatement = unzippedJSCompactStatement.replace(variable+"[\""+key+"\"]", functionName);
			JSScript.jsFunctions.put(functionName, jsFunction);
//			System.err.println("Unzipped2: "+unzippedJSCompactStatement);
//			System.out.println("VARIABLES1:\n"+jsFunction);		
			
			// Build a Java Runnable of the JSFunction
			jsFunction.buildRunnable();
		}
//		System.out.println("RETURN JSCOMPACT:"+unzippedJSCompactStatement);
		unzippedJSStatement = new JSStatement(unzippedJSCompactStatement);
		unzippedJSStatement.setParent(getParent());
		return unzippedJSStatement;
	}
	
	public JSStatement setUnzippedJSStatement(JSStatement unzippedJSStatement) {
		return this.unzippedJSStatement = unzippedJSStatement;
	}
	
	public JSStatement getUnzippedJSStatement() {
		return this.unzippedJSStatement;
	}
	
	JSStatement unzippedJSStatement;
	
	@Override
	public String toString() {
		return unZip().toString();
	}
	
	public static String extractKey(String variable, String jsCompactStatement) {
		Pattern	k = Pattern.compile(variable+"\\[\"(.*)\"\\]");
		Matcher m = k.matcher(jsCompactStatement);
		while(m.find())
			return m.group(1);
		return null;
	}
	
	public static String[] extractArguments(String variable, String jsCompactStatement) {
		Pattern	k = Pattern.compile(variable+"\\[(.*)\\]\\((.*)(?<!\\))\\)");
		Matcher m = k.matcher(jsCompactStatement);
		String arguments = null;
		while(m.find())
			arguments = m.group(2);
		return arguments.split(",");
	}
		
	public String getRealKey(String key) {
		System.err.println("Key: "+key);
		JSStatement realKey = getVariable(key);
		if(realKey == null)
			return null;
		if(realKey.getValue() == null)
			return null;
		// If the variable is part of a JSForLoop don't replace it!
		if(realKey.getParent() instanceof JSForLoop)
			return key;
		System.out.println("REAL KEY"+realKey.getClass());
		return realKey.getValue().toString();
	}
	
	@Override
	public String getType() {
		String type = null;
		if(this.getUnzippedJSStatement().toString().indexOf('(') > 0) {
			String functionName = this.getUnzippedJSStatement().toString().substring(0, this.getUnzippedJSStatement().toString().indexOf('('));
			if(JSScript.jsFunctions.containsKey(functionName)) {
				JSFunction jsFunction = JSScript.jsFunctions.get(functionName);
				type = jsFunction.getReturn().getType(); // Put this in typeOf() method
			}
		}
		return type;
	}
}
