package js2j;

import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;

public class JSPackageList extends ArrayList<JSPackage> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8604833669005489500L;

	public JSPackageList() {
	}
	
	public boolean hasPackage(JSPackage _package) {
		for(JSPackage jsPackage: this) {
			if(jsPackage.getName().equals(_package.getName()))
				return false;
		}
		return true;
	}
	
	@Override
	public String toString() {
		if(size() > 0)
			return StringUtils.join(this, "");
		return "";
	}
}
