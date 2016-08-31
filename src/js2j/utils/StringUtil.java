// http://stackoverflow.com/questions/11145681/how-to-convert-a-string-with-unicode-encoding-to-a-string-of-letters

package js2j.utils;
import js2j.JSStatement;

public class StringUtil {
	public static String unicodeToString(String s) {
		String str = s.split(" ")[0];
		str = str.replace("\\","");
		String[] arr = str.split("u");
		String text = "";
		for(int i = 1; i < arr.length; i++){
		    int hexVal = Integer.parseInt(arr[i], 16);
		    text += (char)hexVal;
		}
		return text;
	}
	
	public static String extractName(JSStatement jsStatement) {
		return jsStatement.toString().substring(0, jsStatement.toString().indexOf('('));
	}
	
	public static String[] extractParameters(JSStatement jsStatement) {
		int oBr = jsStatement.toString().indexOf('('),
				cBr = jsStatement.toString().indexOf(')');
		if(jsStatement.toString().substring(oBr+1, cBr).length() > 0) {
			String[] parameters = jsStatement.toString().substring(oBr+1, cBr).split(",");
			return parameters;
		}
		return null;
	}
	
	public static int hasAsArgument(String argument, String functionCall) {
		if(functionCall.indexOf('(') < 0)
			return -1;
		String argumentsAsString = functionCall.substring(functionCall.indexOf('(')+1, functionCall.indexOf(')'));
//		System.out.println("BONJOUR: "+argument+" "+argumentsAsString);
		if(argumentsAsString.indexOf(',') > 0) {
			String[] arguments = argumentsAsString.split(",");
			for(int i=0; i<arguments.length; i++) {
//				System.out.println(argument+"=="+arguments[i]);
				if(arguments[i].equals(argument))
					return i;
			}
		} else {
			if(argumentsAsString.equals(argument))
				return 0;
		}
		return -1;
	}
}
