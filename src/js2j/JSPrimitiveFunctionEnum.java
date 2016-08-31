// Enum : http://javahowto.blogspot.be/2008/04/java-enum-examples.html

package js2j;

public enum JSPrimitiveFunctionEnum {
		
	toLowerCase("String","toLowerCase"), 
	substr("String","substring"), 
	indexOf("int","indexOf"), 
	length("int","length","length()"),
	parseInt("int","Integer.parseInt"),
	round("int","round"),
	charCodeAt("char","charAt");
	
	private String returnType;
	private String[] toJava;

	JSPrimitiveFunctionEnum(String returnType,String... toJava) {
		this.setReturnType(returnType);
		this.setToJava(toJava);
	}
	
	public static JSPrimitiveFunctionEnum asJSPrimitiveFunction(String str) {
	    for (JSPrimitiveFunctionEnum me : JSPrimitiveFunctionEnum.values()) {
	        if (new String("\""+me.name()+"\"").equalsIgnoreCase(str))
	            return me;
	    }
	    return null;
	}
	
	public static JSPrimitiveFunctionEnum containsJSPrimitiveFunction(String str) {
	    for (JSPrimitiveFunctionEnum me : JSPrimitiveFunctionEnum.values()) {
	        if (str.contains(me.name()))
	            return me;
	    }
	    return null;
	}
	
	public static String translate(String jsStatementAsString, JSPrimitiveFunctionEnum jsPrimitiveFunction) {
		switch(jsPrimitiveFunction.name()) {
			/*
			 * We have to format the substring arguments because the definition
			 * of the substr function in Javascript is different than in Java.
			 * Javascript Syntax : string.substr(start,length)
			 * Java Syntax 		 : string.substring(begin,end)
			 */
			case "substr":
				String substringArgsFormatted;
				String substring = jsStatementAsString.substring(jsStatementAsString.indexOf(jsPrimitiveFunction.getToJava(null)), jsStatementAsString.length());
				String substringArgs = substring.substring(substring.indexOf('(')+1, substring.indexOf(')'));
				String[] arguments = substringArgs.split(",");
				substringArgsFormatted = arguments[0] +","+ arguments[0] +"+"+ arguments[1];
				jsStatementAsString = jsStatementAsString.replace(substringArgs, substringArgsFormatted);
			break;
		}
		return jsStatementAsString;
	}

	public String getReturnType() {
		return returnType;
	}

	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}

	/*
	 * Some functions are not identical in syntax depending on
	 * the variable to which it is applied.
	 * e.g.: 
	 * 			Java		vs.		Javascript
	 * String	length()			length
	 * Array	length				length
	 */
	public String getToJava(String jsVariableType) {
		if(this.name().equals("length"))
			if(jsVariableType != null)
				if(jsVariableType.equals("String"))
					return toJava[1];
		return toJava[0];
	}

	public void setToJava(String[] toJava) {
		this.toJava = toJava;
	}
}
