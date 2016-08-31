package js2j;

import java.io.File;

import js2j.utils.FileUtils;

public class JSFunction extends JSBlock {
	
	public JSFunction() {}
		
	public JSFunction(JSStatement jsStatement) {
		super();
		this.setFirstJSStatement(jsStatement);
		this.setParent(jsStatement.getParent());
		this.setName(extractName(jsStatement.toString()));
//		System.out.println("FUNCNAME:"+getName());
		JSScript.jsFunctions.put(getName(), this);
		if(getName() != null) {
			String functionName = getName().toUpperCase()+"."+getName();
			JSScript.jsFunctions.put(functionName, this);
		}
		this.setParameters(new JSParameterList(extractParameters(jsStatement.toString())));
	}
	
	public void setReturn(JSReturn returnStatement) {
		this.returnStatement = returnStatement;
	}
	
	public JSReturn getReturn() {
		return this.returnStatement;
	}
	
	private JSReturn returnStatement;
	
	private static String extractName(String sttmnt) {
		int eq = sttmnt.indexOf('='),
				var = sttmnt.indexOf("var"),
				fn = sttmnt.indexOf("function");
		// Check if var is present before =
		if(sttmnt.indexOf("function") != 0) {
			if(eq > 0 && var < eq && var >= 0) 
				return sttmnt.substring(var+4, eq);
			if(eq > 0)
				return sttmnt.substring(0, eq);
		} else {
			if(sttmnt.indexOf("function(") != 0)
				return sttmnt.substring(fn+new String("function").length()+1,sttmnt.indexOf('(')).trim();
		}
		return null;
	}
	
	private JSParameter[] extractParameters(String sttmnt) {
		int oBr = sttmnt.indexOf('('),
				cBr = sttmnt.indexOf(')');
		if(sttmnt.substring(oBr+1, cBr).length() > 0) {
			String[] parameters = sttmnt.substring(oBr+1, cBr).split(",");
			JSParameter[] jsParameters = new JSParameter[parameters.length];
			for(int i=0; i<parameters.length; i++) {
				jsParameters[i] = new JSParameter(parameters[i]);
				jsParameters[i].setParent(getFirstJSStatement().getParent());
			}
			return jsParameters;
		}
		return null;
	}

	public JSParameterList getParameters() {
		return parameters;
	}

	private void setParameters(JSParameterList parameters) {
		this.parameters = parameters;
	}
	
	private JSParameterList parameters;
	
	public void addPackage(JSPackage jsPackage) {
//		System.out.println("ADD package: "+jsPackage);
//		if(!this.getPackages().hasPackage(jsPackage.g))
		this.getPackages().add(jsPackage);
	}
	
	public JSPackageList getPackages() {
		return packages;
	}

	private JSPackageList packages = new JSPackageList();
	
	public void buildRunnable() {
		String jsFunctionCode = "package utils;\n\n";
		jsFunctionCode += getPackages();
		jsFunctionCode += "\n";
		jsFunctionCode += "public class "+ getName().toUpperCase() +" {\n\n";
		jsFunctionCode += this+"\n\n";
		jsFunctionCode += "}";
		File jsRunnableFunctionFile = new File(JSScript.outputDirectory+"/utils/"+ getName().toUpperCase() +".java");
		FileUtils.buildCompile(jsRunnableFunctionFile, jsFunctionCode);
	}

	@Override
	public boolean hasVariable(String variableName) {
		return getVariableAs(variableName) != null;
	}

	@Override
	public JSStatement getVariableAs(String variableName) {
//		System.out.println(getName()+"+++"+getVariables().size());
//		if(getVariables().containsKey(variableName))
//			return getVariables().get(variableName);
		for(JSStatement jsStatement: getJSStatements()) {
	    	if(jsStatement instanceof JSVariable) 
	    		if(((JSVariable) jsStatement).getName().equals(variableName))
	    			return jsStatement;
	    	if(jsStatement instanceof JSVariableList) {
//	    		System.out.println(((JSFunction)((JSVariableList) jsStatement).getParent()).getName());
//	    		System.out.println("SYSTEM HI"+variableName+" "+((JSVariableList) jsStatement).getVariableAs(variableName));
	    		JSVariable jsVariable = (JSVariable) ((JSVariableList) jsStatement).getVariableAs(variableName);
	    		if(jsVariable == null) 
	    			continue;
	    		return jsVariable;
	    	}
	    }
		return getParameters().get(variableName);
	}
	
	@Override
	public String toString() {
		String jsFunctionCode = "";
		if(getReturn() != null)
			jsFunctionCode += "\tpublic static "+ getReturn().getType()+" "+getName()+ "("+getParameters()+") {\n";
	    for(JSStatement jsStatement: getJSStatements()) {
	    	jsFunctionCode += "\t\t"+jsStatement;
	    	if(jsStatement instanceof JSVariable) 
	    		jsFunctionCode += "; ";
	    	jsFunctionCode += "\n";
	    }
		return jsFunctionCode += "\t}\n";
	}

}
