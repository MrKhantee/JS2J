// Compile and run java file in command : http://stackoverflow.com/questions/12402547/how-to-execute-javac-and-java-commands-from-java-program
// Run and compile java file in command : http://stackoverflow.com/questions/2096283/including-jars-in-classpath-on-commandline-javac-or-apt

package js2j;

import java.io.File;

import js2j.utils.FileUtils;

public class JSFunctionCall extends JSValue {
	
	public JSFunctionCall(JSStatement jsStatement) {
		this.setJsStatement(jsStatement);
		this.setParent(jsStatement.getParent());
		this.setJSFunction(JSScript.jsFunctions.get(getFunctionName()));
		System.out.println("%%%%"+jsStatement);
		if(this.getJSFunction() != null) {
			System.out.println("BUILDING: "+this.getJSFunction().getName());
			this.setArgumentsTypes();
			this.setValue(new JSStatement(this.getJsStatement().toString().replace(getFunctionName(), getClassName()+"."+getFunctionName())));
//			System.out.println("--->"+jsStatement);
			
			this.buildJSCallFunction();
			File jsRunnableFunctionFile = new File(FileUtils.utilsFilePath+"/"+ getClassName() +".java");
			FileUtils.buildCompile(jsRunnableFunctionFile, jsCallFunctionRunnableCode);
		}
	}
	
	public String[] getFunctionArguments() {
		String arguments = getJsStatement().toString().substring(getJsStatement().toString().indexOf('(')+1, getJsStatement().toString().lastIndexOf(')'));
		if(arguments.length() == 0)
			return new String[]{};
		if(arguments.indexOf(',') < 0)
			return new String[]{arguments};
		return arguments.split(",");
	}
	
	public String getFunctionName() {
		return this.getJsStatement().toString().substring(this.getJsStatement().toString().indexOf('.')+1, this.getJsStatement().toString().indexOf('('));
	}
	
	public JSFunction getJSFunction() {
		return this.jsFunction;
	}
	
	public void setJSFunction(JSFunction jsFunction) {
		this.jsFunction = jsFunction;
	}
	
	private JSFunction jsFunction;
	
	// TODO Get from parent instead of jsVariables;
	public String buildJSArgumentsDeclaration() {
		String JSArgumentsDeclarationCode = "";
		int i = 0;
		for(String arg: getFunctionArguments()) {
//			System.out.println("JSFCall Arg:"+arg);	
			
			// Set the arguments types of this JSFunction
			String type = new JSStatement(arg).typeOf();
			this.getJSFunction().getParameters().get(i).setType(type);
				
			JSVariable jsVariable;
			if(arg.indexOf('[') > 0)
				jsVariable = (JSVariable) getVariable(extractVariable(arg));
			else
				jsVariable = (JSVariable) getVariable(arg);
			JSArgumentsDeclarationCode += "\t\t"+ jsVariable.getValue().getType() +" "+ jsVariable +"; \n";
			i++;
		}
		return JSArgumentsDeclarationCode;
	}
	
	public void setArgumentsTypes() {
		// Set the arguments types of this JSFunction
		int i=0;
		for(String arg: getFunctionArguments()) {
			String type = new JSStatement(arg).typeOf();
			System.out.println(">>>"+type);
			System.out.println(this.getJSFunction().getName());
			System.out.println(this.getJSFunction().getParameters().size());
			this.getJSFunction().getParameters().get(i).setType(type);
			
			JSFunction firstJSFunctionParent = (JSFunction) getParent(JSFunction.class);
			if(firstJSFunctionParent != null) {
				JSFunction secondJSFunctionParent = (JSFunction) firstJSFunctionParent.getParent(JSFunction.class);
				if(secondJSFunctionParent != null) {
					JSVariable jsVariable = (JSVariable) firstJSFunctionParent.getVariableAs(arg);
					if(jsVariable == null) {
						jsVariable = (JSVariable) getVariable(arg);
						/*
						 * Have to create a new JSVariable objects because 
						 * it could be that variable is part of a 
						 * JSVariable list then the Type will not be
						 * displayed
						 */
						if(jsVariable != null) {
							jsVariable = new JSVariable(jsVariable.getValue().getType(),jsVariable.getName(),jsVariable.getValue());
							firstJSFunctionParent.getJSStatements().add(0, jsVariable);
						}
					}
				}
			}	
			i++;
		}
	}
		
	public String getClassName() {
		return getFunctionName().toUpperCase();
	}
	
	public String buildJSFunction() {
		return getJSFunction() + "";
	}
	
	public void buildJSCallFunction() {
		// Define the package
		jsCallFunctionRunnableCode += "package utils;\n\n";
		// Define the imports
		// Define and open the class
		jsCallFunctionRunnableCode += "public class "+ getClassName() +" {\n\n";
		// Define the function
		jsCallFunctionRunnableCode += buildJSFunction();
		// Close the class
		jsCallFunctionRunnableCode += "}";
	}
	
	String jsCallFunctionRunnableCode = "";
	
	@Override 
	public String getType() {
		if(this.getJSFunction() != null)
			return this.getJSFunction().getReturn().getType();
		return null;
	}
	
	@Override
	public String toString() {
		return getValue()+"";
	}
}
