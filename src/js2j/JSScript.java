package js2j;

import java.util.ArrayList;
import java.util.HashMap;

public class JSScript {

	final private JSStatement script;
	
	public static ArrayList<JSStatement> jsStatements = new ArrayList<JSStatement>();
	public static HashMap<String,JSStatement> jsVariables = new HashMap<String,JSStatement>();

	public static HashMap<String,JSFunction> jsFunctions = new HashMap<String,JSFunction>();
	
	public JSScript(String script) {
		this.script = new JSStatement(script,0);
	}
	
	public JSStatement getJsStatement() {
		return script;
	}
	
	public static ArrayList<JSBlock> getJSTree() {
		return jSTree;
	}

	public void setJSTree(ArrayList<JSBlock> jsTree) {
		JSScript.jSTree = jsTree;
	}
	
	protected static ArrayList<JSBlock> jSTree = new ArrayList<JSBlock>();
	
	
	public static ArrayList<JSStatement> tmpJSTree = new ArrayList<JSStatement>();

	public JSScript read() {
		String[] jsCodeStatements = script.toString().split(";");
		for(int i=0; i<jsCodeStatements.length; i++)
			jsStatements.add(new JSStatement(jsCodeStatements[i].trim(),i));
		
//		for(JSStatement s: jsStatements) {
//			System.out.println(s);
//		}
		
		// Build the first statement
		jsStatements.get(0).build();
		
		System.out.println("//////// SUMMARY ///////////");
		for(JSBlock b: getJSTree()) {
			JSBlock parent = null;
			String head = "";
			if(b.getParent() == null)
				head = "(HEAD)";
			else
				parent = (JSBlock) b.getParent();
			String par = "";
			if(parent != null)
				par = parent.getName();
			System.out.println("/// -> "+" "+ b.getName() + " ^" + par + " "+ head);
		}
		System.out.println("////////////////////////////");
		for(String func: JSScript.jsFunctions.keySet()) {
			System.out.println();
			System.out.println("//////// "+func+" ///////////");
			System.out.println(JSScript.jsFunctions.get(func));
		}
		System.out.println("////////////////////////////");
		return this;
	}
	
	public void out(String outputDirectory) {
		JSScript.outputDirectory = outputDirectory;
		for(JSBlock b: JSScript.getJSTree()) {
			String head = "";
			/*
			 * Check if the current JSBlock is a Head block
			 */
			if(b.getParent() == null) {
				if(b instanceof JSFunction) {
					((JSFunction) b).buildRunnable();
				}
			}
			System.out.println("/// -> "+" "+ b.getName() + " "+ head);
		}
	}
	
	static String outputDirectory;
}
