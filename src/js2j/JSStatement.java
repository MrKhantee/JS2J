// Catch negative numbers : http://stackoverflow.com/questions/9043551/regex-match-integer-only

package js2j;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import js2j.operators.JSAssignment;
import js2j.operators.JSAssignmentList;
import js2j.operators.JSBitToBit;
import js2j.operators.JSComparison;
import js2j.operators.JSComputation;
import js2j.operators.JSIncrementation;
import js2j.operators.JSLogical;
import js2j.operators.JSMathAssignment;
import js2j.operators.JSRotationBit;

import org.apache.commons.lang3.StringUtils;

public class JSStatement {
	
	final private String jsStatementString;
	final private int lineNumber;
	
	public JSStatement() {
		this.jsStatementString = "";
		this.lineNumber = 0;
	}
	
	public JSStatement(String jsStatementString) {
		this.jsStatementString = jsStatementString;
		this.lineNumber = -1;
	}
	
	public JSStatement(String jsStatementString, int lineNumber) {
		this.jsStatementString = jsStatementString;
		this.lineNumber = lineNumber;
	}
	
	public JSStatement getJsStatement() {
		return jsStatement;
	}

	protected void setJsStatement(JSStatement jsStatement) {
		this.jsStatement = jsStatement;
	}
	
	private JSStatement jsStatement;
	
	@Override
	public String toString() {
		return this.jsStatementString;
	}
	
	public JSStatement getTopParent() {
		JSStatement parent;
		if((parent = (JSStatement) getParent()) != null) {
			parent.getTopParent();
		}
		return (JSStatement) this;
	}
	
	public JSStatement getParent(Class<?> parentType) {
		JSStatement parent = (JSStatement) getParent();
		if(parent == null)
			return null;
		if(parentType.isAssignableFrom(parent.getClass()))
			return parent;
//		System.out.println(parentType+" >>>Look: "+parent.getClass());
		return parent.getParent(parentType);
	}
	
	public JSStatement getParent() {
		return this.parent;
	}
	
	public void setParent(JSStatement parent) {
		this.parent = parent;
	}

	private JSStatement parent;
	
	/**
	 * 
	 * @param jsVariableName
	 * @return
	 */
	public boolean hasVariable(String jsVariableName) {
		if(getVariable(jsVariableName) == null)
			return false;
		return true;
	}
	
	/**
	 * Get the variable 
	 * @param jsVariableName
	 * @return
	 */
	public JSStatement getVariable(String jsVariableName) {
		JSStatement jsStatement = null;
//		System.out.println(jsVariableName+"!>>!"+this);
//		System.out.println(getParent().getClass());
		if((jsStatement = getParent()) == null)
			return JSScript.jsVariables.get(jsVariableName);
		if( jsStatement instanceof JSBlock ) {
			if( ((JSBlock) jsStatement).getVariableAs(jsVariableName) != null ) {
//				System.out.println(jsVariableName+"!>>!"+((JSBlock) jsStatement).getVariableAs(jsVariableName).getClass());
				return ((JSBlock) jsStatement).getVariableAs(jsVariableName);
			}
		}
		return jsStatement.getVariable(jsVariableName);
	}
	
	public JSStatement getValue() {
		return this.value;
	}
	
	public void setValue(JSStatement value) {
		this.value = value;
	}
	
	private JSStatement value;
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	private String type;
	
	public boolean isFunction() {
		return jsStatementString.indexOf("function") == 0
				|| jsStatementString.matches("(var\\s)?(.*)=function(.*)");
	}
	
	public boolean isForLoop() {
		return jsStatementString.indexOf("for") == 0;
	}
	
	public boolean isIf() {
		return jsStatementString.indexOf("if") == 0;
	}
	
	public boolean isElseIf() {
		return jsStatementString.indexOf("else if") == 0;
	}
	
	
	public boolean isElse() {
		return jsStatementString.indexOf("else") == 0;
	}
	
	public boolean isJSONObject() {
		return jsStatementString.matches("(var\\s)?(.*)=(?!\")\\{(.*)")
				&& jsStatementString.indexOf('[') < 0;
	}
	
	public boolean isJSONObjectElement() {
		return jsStatementString.matches("\\}?,?('|\")(.*)('|\"):(.*)");
	}
	
	public boolean isVariable() {
		return jsStatementString.indexOf("var") == 0 
				&& jsStatementString.matches("var\\s(.*)=(.*)")
				&& StringUtils.countMatches(jsStatementString, "=") == 1
				&& !isFunction();
	}
	
	public boolean isVariableList() {
		return jsStatementString.indexOf("var") == 0
				&& jsStatementString.matches("var\\s(.*)=(.*)(,(.*)=(.*))+");
	}
	
	public boolean isAssignmentList() {
		return jsStatementString.indexOf("var") < 0
				&& jsStatementString.matches("(.*)=(.*)(,(.*)=(.*))+")
				&& !isFunctionCall()
				&& !isLogical()
				&& !isMathAssignment()
				&& !isComparison()
				&& !isForLoop()
				&& !isFunction()
				&& !isTry()
				&& !isIncrementation()
				&& !isIf()
				&& !isElse()
				&& !isVariable()
				&& !isVariableList()
				&& !isJSONObject()
				&& !isJSONObjectElement();
	}
	
	public boolean isAssignment() {
		return jsStatementString.matches("(.*)=(.*)")
				&& !isFunctionCall()
				&& !isLogical()
				&& !isMathAssignment()
				&& !isComparison()
				&& !isForLoop()
				&& !isFunction()
				&& !isTry()
				&& !isIncrementation()
				&& !isIf()
				&& !isElse()
				&& !isVariable()
				&& !isVariableList()
				&& !isJSONObject()
				&& !isJSONObjectElement();
	}
	
	public boolean isMathAssignment() {
		return jsStatementString.matches("(.*)(\\+|\\-|\\*|/|%)=(.*)");
	}
	
	public boolean isComparison() {
		return jsStatementString.matches("(.*)(<=?|>=?|\\!={1,2}|={2,3}|\\sin\\s)(.*)");
	}
	
	public boolean isReturn() {
		return jsStatementString.indexOf("return") == 0;
	}
	
	public boolean isLogical() {
		return jsStatementString.indexOf("!") == 0
				|| jsStatementString.indexOf("||") > 0
				|| jsStatementString.indexOf("&&") > 0;
	}
	
	public boolean isJSForLoopStatement() {
		JSStatement parentLoop;
		if((parentLoop = getParent(JSForLoop.class)) != null) {
			if(((JSForLoop) parentLoop).getJSForLoopStatementList().size() < 3)
				return true;
		}
		return false;
	}
	
	public boolean isJSIfStatement() {
		JSStatement parentIf;
		System.out.println("Parent if: "+getParent().getClass());
		if((parentIf = getParent()) instanceof JSIf) {
			if(((JSIf) parentIf).getJSIfStatement() == null)
				return true;
		}
		return false;
	}
	
	public boolean isParentJSJSONObjectElement() {
		if((getParent()) instanceof JSJSONObjectElement)
			return true;
		return false;
	}
	
	public boolean isIncrementation() {
		return jsStatementString.indexOf("++") > 0 || jsStatementString.indexOf("--") > 0;
	}
	
	public boolean isCompactStatement() {
		return !isComputation() 
				&& jsStatementString.matches("=?(.*)\\[(.*)\\]\\((.*)\\)(.*)");
	}
	
	public boolean isComputation() {
		return jsStatementString.matches("^(?!['|\"])((.*)(\\*|\\+|-|%|/))+(.*)(?!['|\"])$");
	}
	
	public boolean isDefinedVariable() {
		return jsStatementString.matches("([a-zA-Z0-9]|_)*")
				&& hasVariable(jsStatementString);
	}
	
	public boolean isTry() {
		return jsStatementString.indexOf("try") == 0;
	}
	
	public boolean isCatch() {
		return jsStatementString.indexOf("catch") == 0;
	}
	
	public boolean isEval() {
		return jsStatementString.indexOf("eval") == 0;
	}
	
	public boolean isThrow() {
		return jsStatementString.indexOf("throw") == 0;
	}
	
	public boolean isFunctionCall() {
		if(jsStatementString.indexOf('(') < 0)
			return false;
		String functionName = jsStatementString.substring(0, jsStatementString.indexOf('('));
		return JSScript.jsFunctions.containsKey(functionName);
	}
	
	public boolean isArrayCall() {
		return jsStatementString.matches("\\w\\[(.*)]")
				&& !isCompactStatement();
	}
	
	public boolean isTypeof() {
		return jsStatementString.matches("(\"\\w+\")\\s*([\\!=]=)\\s*typeof\\s*(\\w+)");
	}
	
	public boolean isPrimitiveFunction() {
		return JSPrimitiveFunctionEnum.containsJSPrimitiveFunction(jsStatementString) != null;
	}
	
	public boolean isBitToBit() {
		return jsStatementString.matches("(\\w+)(&|\\^|\\|)(\\w+)(.*)");
	}
	
	public boolean isRotationBit() {
		return jsStatementString.matches("([\\w|&|\\^|\\|]+)([\\>|\\<]{2,3})([\\w|&|\\^|\\|]+)");
	}
	
	public int getLineNumber() {
		return lineNumber;
	}
	
	public JSStatement getPreviousJSStatement() {
		return JSScript.jsStatements.get(getLineNumber()-1);
	}
	
	public JSStatement getNextJSStatement() {
		return JSScript.jsStatements.get(getLineNumber()+1);
	}
	
	public boolean hasNextJSStatement() {
		return (getLineNumber()+1) < JSScript.jsStatements.size();
	}
	
	/**
	 * 
	 * @return	The next JSStatement after this JSStatement in 
	 * 			the JSScript parsed.
	 * 			| JSScript.jsStatements.get(getLineNumber()+1);
	 */
	public JSStatement next() {
		if(hasNextJSStatement())
			return JSScript.jsStatements.get(getLineNumber()+1);
		return null;
	}
	
	public void buildNext(JSStatement parent) {
		JSStatement nextJSStatement = next();
		if(nextJSStatement != null) {
			nextJSStatement.setParent(parent);
			nextJSStatement.build();
		}
	}
	
	public void buildNextAfter(JSStatement parent, char c) {
		JSStatement jsStatement = new JSStatement(toString().substring(toString().indexOf(c)+1),getLineNumber());
		jsStatement.setParent(parent);
		jsStatement.build();
	}
	
	public boolean build() {
		
		System.out.println("#"+this.getLineNumber()+" >>> "+this);

		// Check if its the end of the reading scrip
		if(toString() == null) {
			System.out.println("END");
		} 
		
		// Check if current JSStatement is an ArrayCall
		else if(isArrayCall()) {
			System.out.println("ARRAY CALL");
			/*
			 * Check if a cast has to be made
			 */
			if(getParent() instanceof JSAssignment) {
				String leftOperand = ((JSAssignment) getParent()).getLeftOperand().toString();
				JSVariable jsVariable = (JSVariable) getVariable(leftOperand);
				String variableType = jsVariable.getValue().getType();
//				System.out.println(variableType);
				if(JSPrimitiveType.asJSPrimitiveType(variableType).getPowerIndex() 
						< JSPrimitiveType.asJSPrimitiveType(typeOf()).getPowerIndex()) {
					jsVariable.getValue().setType(typeOf());
					((JSAssignment) getParent()).setRightOperand(new JSValue(new JSStatement(toString())));
				}
			}
			this.buildNext(getParent());			
		}
		
		// Check if a JSBlock is closed
		else if(toString().indexOf('}') == 0) {
			JSBlock topParent = (JSBlock) getParent(JSBlock.class);
			if(topParent != null) {
				JSStatement jsStatement = new JSStatement(toString().substring(1),this.getLineNumber());
				jsStatement.setParent(topParent.getParent());
				jsStatement.build();
			}
		}	
		
		// Check if it's a Throw statement
		else if(isThrow()) {
			System.out.println("THROW");
			new JSThrow(this);
			if(getParent() instanceof JSBlock) {
				if(((JSBlock)getParent()).isOneLineBlock())
					this.buildNext(getParent().getParent());
				else 
					this.buildNext(getParent());
			}
		}
		
		else if(isRotationBit()) {
			System.out.println("ROTATIONBIT");
			new JSRotationBit(this);
		}
		
		// Check if it's a Bit to Bit statement
		else if(isBitToBit()) {
			System.out.println("BITTOBIT");
			new JSBitToBit(this);
		}
		
		// Check if it's a Typeof statement
		else if(isTypeof()) {
			System.out.println("TYPEOF");
			new JSTypeOf(this);
		}
		
		// Check if it's an Assignment List
		else if(isAssignmentList()) {
			System.out.println("JSASSIGNMENTLIST");
			new JSAssignmentList(this);
		}
		
		// Check if it's an Assignment
		else if(isAssignment()) {
			System.out.println("JSASSIGNMENT");
			new JSAssignment(this);
			
			if(isJSForLoopStatement()) {
				// Goto next JSStatement in the JSScript;
				this.buildNext(getParent());
			}
		}	
		
		// Check if it's a Integer
		 else if(JSValue.isInt(toString())) {
			System.out.println("INT DETECTED");
			if(getParent() instanceof JSReturn) 
				((JSReturn) getParent()).setValue(new JSInt(this));
			if(getParent() instanceof JSAssignment)
				((JSAssignment) getParent()).setRightOperand(new JSInt(this));
			if(getParent() instanceof JSComparison)
				((JSComparison) getParent()).addOperand(new JSInt(this));
			if(getParent() instanceof JSComputation)
				((JSComputation) getParent()).addOperand(new JSInt(this));
			if(getParent() instanceof JSBitToBit)
				((JSBitToBit) getParent()).addOperand(new JSInt(this));			
		}
		
		// Check if the current JSStatement is a JSONObject
		else if(isJSONObject()) {
			System.out.println("JSONOBJECT");
			JSJSONObject jsBlock = new JSJSONObject(this);
			JSScript.jSTree.add(jsBlock);
			this.buildNextAfter(jsBlock, '{');
		}
		
		// Check if the current JSStatement is a JSONObjectElement
		else if(isJSONObjectElement()) {
			System.out.println("JSONOBJECTELEMENT");
			new JSJSONObjectElement(this);
		}
		
		// Check if the current JSStatement is a Function
		else if(isFunction()) {
			System.out.println("JSFUNCTION");
			JSFunction jsFunction = new JSFunction(this);

			if(isParentJSJSONObjectElement()) {
				String functionName = ((JSJSONObject) getParent(JSJSONObject.class)).getName()+"_"+((JSJSONObjectElement) getParent()).getName();
				jsFunction.setName(functionName);
				((JSJSONObjectElement) getParent()).setValue(jsFunction);
			} else {
				/*
				 * Nested functions not allowed in Java.
				 * When nested function is detected put it
				 * in the main block
				 */
				JSScript.jSTree.add(jsFunction);
			}
			this.buildNextAfter(jsFunction, '{');
		}
		
		// Check if the current JSStatement is Math Assignment
		else if(isReturn()) {
			System.out.println("RETURN");
			JSReturn jsReturn = new JSReturn(this);
			this.buildNextAfter(jsReturn, ' ');
						
			JSStatement nextJSStatement = next();
			// If next JSStatement == null => End of the script
			if(nextJSStatement != null) {
				
				JSBlock parentBlock;
				/**
				 * Check if the parent JSBLock is in one line.
				 * If it's the case, getParent() 2 times because
				 * there will be no }.
				 */
				if((parentBlock = (JSBlock) getParent(JSBlock.class)) != null) {
					if(parentBlock.isOneLineBlock())
						nextJSStatement.setParent(getParent(JSBlock.class).getParent());
					else 
						nextJSStatement.setParent(getParent());
				}	
				nextJSStatement.build();
			}
		} 
		
		// Check if the current JSStatement is a Variable List
		else if(isVariableList()) {
			System.out.println("VARIABLE LIST");
			new JSVariableList(this);
			
			// Goto Next JSStatement;
			this.buildNext(getParent());			
		}
		
		// Check if the current JSStatement is a Variable
		else if(isVariable()) {
			System.out.println("VARIABLE");
						
			JSVariable jsVariable = new JSVariable(this);
			// Add JSVariable to the first JSBlock parent; 
			
			if(jsVariable != null)
				jsVariable.setParent(getParent());
			
			// Check if the variable if a parameter of a JSForLoop
			if(isJSForLoopStatement())
				((JSForLoop) getParent()).addJSForLoopStatement(jsVariable);
			else
				/*
				 * Add JSVariable to the first JSBlock parent only if it's not one 
				 * its parameters
				 */ {
				((JSBlock) getParent(JSBlock.class)).add(jsVariable);
				((JSBlock) getParent(JSBlock.class)).addVariable(jsVariable);
			}
				
			// Goto next JSStatement in the JSScript;
			this.buildNext(getParent());			
		}
		
		// Check if the current JSStatement is a For Loop
		else if(isForLoop()) {
			System.out.println("FOR");
			new JSForLoop(this);
		}
		
		// Check if the current JSStatement is a Try 
		else if(isTry()) {
			System.out.println("TRY");
			JSTry jsBlock = new JSTry(this);
			this.buildNextAfter(jsBlock, '{');			
		}
		
		// Check if the current JSStatement is a Catch
		else if(isCatch()) {
			System.out.println("CATCH");
			new JSCatch(this);
			// Goto next JSStatement in JSScript
			this.buildNext(getParent());			
		}
		
		// Check if the current JSStatement is an If
		else if(isIf()) {
			System.out.println("IF");
			new JSIf(this);
		}
		
		// Check if the current JSStatement is an If
		else if(isElse()) {
			System.out.println("ELSE");
			new JSElse(this);
		}
		
		// Check if the current JSStatement is an Logical Operator
		else if(isLogical()) {
			System.out.println("LOGICAL");
			new JSLogical(this);
		}
		
		// Check if the current JSStatement is a Incrementation
		else if(isIncrementation()) {
			System.out.println("INCREMENTATION");
			JSIncrementation jsIncrementation = new JSIncrementation(this);
			if(isJSForLoopStatement()) {
				((JSForLoop) getParent(JSForLoop.class)).addJSForLoopStatement(jsIncrementation);
				JSStatement nextJSStatement = new JSStatement(JSForLoop.getFirstStatementInForLoop(this),getLineNumber());
				nextJSStatement.setParent(getParent());
				nextJSStatement.build();
			} else {
				this.buildNext(getParent());
			}
		}
		
		// Check if the current JSStatement is an Eval
		else if(isEval()) {
			System.out.println("EVAL");
			new JSEval(this);
		}
		
		// Check if the current JSStatement is a Comparison Statement
		else if(isComparison()) {
			System.out.println("COMPARISON");
			new JSComparison(this);
		}
		
		// Check if the current JSStatement is a JSCompactStatement Statement
		else if(isCompactStatement()) {
			System.out.println("JSCOMPACTSTATEMENT");
			JSCompactStatement jsCompactStatement = new JSCompactStatement(this);
			if(isJSForLoopStatement())
				((JSForLoop) getParent()).addJSForLoopStatement(jsCompactStatement);
			else if(isJSIfStatement())
				((JSIf) getParent()).setJSIfStatement(jsCompactStatement.getUnzippedJSStatement());
			
			if(getParent() instanceof JSAssignment) {
				this.bindType();
				((JSAssignment) getParent()).addOperand(new JSValue(jsCompactStatement.getUnzippedJSStatement()));
			}
			else if(getParent() instanceof JSComputation) {
				((JSComputation) getParent()).addOperand(jsCompactStatement.getUnzippedJSStatement());
			}
			if(getParent(JSComparison.class) == null
					&& !(getParent() instanceof JSIf)) {
				// Goto next JSStatement in JSScript
				this.buildNext(getParent());			
			}
		}
		
		// Check if the current JSStatement is Math Assignment
		else if(isMathAssignment()) {
			System.out.println("MATH ASSIGNMENT");
			new JSMathAssignment(this);
			// Goto next JSStatement in JSScript
			this.buildNext(getParent());			
		} 
		
		// Check if the current JSStatement is Math Assignment
		else if(isComputation()) {
			System.out.println("COMPUTATION");
			new JSComputation(this);
		}
		
		// Check if the current JSStatement is Math Assignment
		else if(isDefinedVariable()) {
			System.out.println("DEFINED VARIABLE");

			if(getParent() instanceof JSComparison) {
				((JSComparison) this.getParent()).addOperand(this); 
			} else if(getParent() instanceof JSComputation) {
				((JSComputation) getParent()).addOperand(this);
			} else if(getParent() instanceof JSBitToBit) {
				((JSBitToBit) getParent()).addOperand(this);
			} else if(getParent() instanceof JSRotationBit) {
				((JSRotationBit) getParent()).addOperand(this);
			} else if(getParent() instanceof JSReturn) {
				// Set the value of JSReturn 
				((JSReturn) getParent()).setValue((JSVariable) getVariable(toString()));
			}
		} else if(isFunctionCall()) {
			System.out.println("FUNCTION CALL");
			JSFunctionCall jsFunctionCall = new JSFunctionCall(this);
			if(getParent() instanceof JSComparison)
				((JSComparison) getParent()).addOperand(jsFunctionCall);
			if(getParent() instanceof JSLogical)
				((JSLogical) getParent()).addOperand(jsFunctionCall);
			
			if(getParent(JSLogical.class) == null
					&& getParent(JSComparison.class) == null) {
				// Goto next JSStatement in JSScript
				this.buildNext(getParent());
			}
			
		} else if(isPrimitiveFunction()) {
			System.out.println("PRIMITIVE FUNCTION");
			JSPrimitiveFunction jsPrimitiveFunction = new JSPrimitiveFunction(this);
			if(getParent() instanceof JSAssignment) 
				((JSAssignment)getParent()).addOperand(jsPrimitiveFunction);
			if(getParent() instanceof JSComparison) 
				((JSComparison)getParent()).addOperand(jsPrimitiveFunction);
		} else {
			System.out.println("NEXT BLOOOCK");
			// Goto next JSStatement in JSScript
			this.buildNext(getParent());
		}
		return true;
	}
	
	Pattern sqb = Pattern.compile("\\[(.*)\\]");
	
	public String typeOf() {
		String type = "";
		JSCompactStatement jsCompactStatement = new JSCompactStatement(toString());
		jsCompactStatement.setParent(getParent());
		jsCompactStatement.parse(jsCompactStatement.toString());
		String unzippedJSCompactStatement = jsCompactStatement.toString();
		
		if(JSValue.isArray(unzippedJSCompactStatement)) { // isArrayCall
			JSStatement jsVariable = (JSStatement) this.getVariable(extractVariable(unzippedJSCompactStatement));
			System.out.println("TYPEOF VAR:"+jsVariable.getClass());
			if(jsVariable instanceof JSParameter) {
				type = ((JSParameter)jsVariable).getType();
			} else if(jsVariable instanceof JSArray){
				type = ((JSArray)jsVariable.getValue()).getType();
			} else if(jsVariable instanceof JSVariable) {
				type = ((JSVariable) jsVariable).getValue().getType();
			}
		    Matcher  matcher = sqb.matcher(unzippedJSCompactStatement);
		    int count = 0;
		    while(matcher.find())
		        count++;
			for(int i=0;i<count;i++)
				type = type.replaceFirst("\\[\\]", "");
			return type;
		} else {
			JSStatement jsVariable;
			if(unzippedJSCompactStatement.indexOf('[') > 0)
				jsVariable = getVariable(extractVariable(unzippedJSCompactStatement));
			else
				jsVariable = getVariable(unzippedJSCompactStatement);
			if(jsVariable instanceof JSVariable) {
				type = jsVariable.getType();
				if(type == null)
					type = jsVariable.getValue().getType();
				System.out.println("=~~~>"+jsVariable +">"+type);
				return type;
			} else {
				System.out.println("FOUND:"+unzippedJSCompactStatement);
				jsVariable = new JSVariable(new JSStatement("var tmp="+unzippedJSCompactStatement));
				if(jsVariable != null) {
					if(jsVariable.getValue() != null) {
						type = jsVariable.getValue().getType();
						System.out.println("FOUND:"+unzippedJSCompactStatement);
						System.out.println(type);
						if(type != null)
							return type;
						else
							return jsCompactStatement.getType();
					}
				}
			}
			return null;
		}
	}
	
	/*
	 * Check if a type of a variable previously declared
	 * has to be updated (dynamic binding)
	 */	
	public void bindType() {
		String leftOperand = ((JSAssignment) getParent()).getLeftOperand().toString();
		if(getVariable(leftOperand) instanceof JSVariable) {
			JSVariable jsVariable = (JSVariable) getVariable(leftOperand);
			String variableType = jsVariable.getValue().getType();
			String currentStatementType = typeOf();
			if(currentStatementType != null) {
				if(JSPrimitiveType.asJSPrimitiveType(variableType).getPowerIndex() 
						< JSPrimitiveType.asJSPrimitiveType(currentStatementType).getPowerIndex()) {
					jsVariable.setType(currentStatementType);
				}
			}
		}
	}
	
	public static String extractKey(String jsCompactStatement) {
		Pattern	k = Pattern.compile("^\\w\\[(.*)\\]$");
		Matcher m = k.matcher(jsCompactStatement);
		while(m.find())
			return m.group(1);
		return null;
	}
	
	public static String extractVariable(String jsCompactStatement) {
		return jsCompactStatement.substring(0, jsCompactStatement.indexOf('['));
	}
}
