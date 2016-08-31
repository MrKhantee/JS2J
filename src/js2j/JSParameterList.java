package js2j;

import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;

public class JSParameterList extends ArrayList<JSParameter> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8604833669005489500L;

	public JSParameterList(JSParameter...jsParameters) {
//		System.out.println("%%%%%"+jsParameters);
		if(jsParameters != null) {
			for(JSParameter jsParameter: jsParameters) {
				if(jsParameter != null) {
					add(jsParameter);
				}
			}
		} 
	}
	
	public boolean hasParameter(String parameterName) {
		for(JSParameter jsParameter: this)
			if(jsParameter.getName().equals(parameterName))
				return true;
		return false;
	}
	
	public JSParameter get(String parameterName) {
		for(JSParameter jsParameter: this)
			if(jsParameter.getName().equals(parameterName))
				return jsParameter;
		return null;
	}
	
	@Override
	public String toString() {
		if(size() > 0) {
			return StringUtils.join(this, ',');
		}
		return "";
	}
}
