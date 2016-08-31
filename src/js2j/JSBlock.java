package js2j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class JSBlock extends JSStatementList {
	
	public JSBlock() {
		JSScript.tmpJSTree.add(this);
	}
	
	public JSBlock(JSStatement jsStatement) {
		this.setFirstJSStatement(jsStatement);
	}
	
	public JSStatement getFirstJSStatement() {
		return this.jsStatement;
	}
	
	protected void setFirstJSStatement(JSStatement jsStatement) {
		this.jsStatement = jsStatement;
	}
	
	private JSStatement jsStatement;
	
	public ArrayList<String> getLinesCodeAsString() {
		ArrayList<String> tmp = new ArrayList<String>();
		for(JSStatement jsStatement: this.getJSStatements())
			tmp.add(jsStatement.toString());
		return tmp;
	}
	
	public abstract boolean hasVariable(String variableName);
	public abstract JSStatement getVariableAs(String variableName);

	public void addVariable(JSStatement builtJSStatement) {
		if(builtJSStatement instanceof JSFunction)
			this.getVariables().put(((JSFunction) builtJSStatement).getName(),builtJSStatement);
		else if(builtJSStatement instanceof JSJSONObjectElement) {
//			System.out.println("NAME:"+((JSJSONObjectElement) builtJSStatement).getName());
			this.getVariables().put(((JSJSONObjectElement) builtJSStatement).getName(),builtJSStatement);
		} else if(builtJSStatement instanceof JSVariable) {
//			System.out.println("VOILA");
			this.getVariables().put(((JSVariable) builtJSStatement).getName(),builtJSStatement);
		}
	}
	
	public HashMap<String,JSStatement> getVariables() {
		return this.jsVariables;
	}
	
	// Local variables of this JSBlock
	private HashMap<String, JSStatement> jsVariables = new HashMap<String,JSStatement>();
	
	public void addJSBlock(JSBlock jsBlock) {
		this.getJSBlocks().add(jsBlock);
	}
	
	public ArrayList<JSBlock> getJSBlocks() {
		return this.jsBlocks;
	}
	
	// Local jsBlocks of this JSBlock
	private ArrayList<JSBlock> jsBlocks = new ArrayList<JSBlock>();
	
	private static int totalLinesCodeRead = 0;
		
	public void createJSBlock(JSStatement jsStatement) {
		Pattern beginBlockPatter = Pattern.compile("(?<!\")\\{");
		Pattern endBlockPatter = Pattern.compile("(?<!\")\\}");

		if(totalLinesCodeRead == JSScript.jsStatements.size()-2)
			return;
													
		Matcher beginBlockPattern = beginBlockPatter.matcher(jsStatement.toString()),
			endBlockPattern = endBlockPatter.matcher(jsStatement.toString());		
		if(endBlockPattern.find()) {
			if(JSScript.tmpJSTree.size() > 0) {
				JSScript.tmpJSTree.remove(JSScript.tmpJSTree.size()-1);
				if(JSScript.tmpJSTree.size() > 0)
					System.out.println(JSScript.tmpJSTree.get(0));
			}
		} else if(beginBlockPattern.find()) {
		}
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private String name;
	
	void setOneLineBlock(boolean isOneLineBlock) {
		this.isOneLineBlock = isOneLineBlock;
	}
	
	public boolean isOneLineBlock() {
		return this.isOneLineBlock;
	}
	
	private boolean isOneLineBlock = false;
}
