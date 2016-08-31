package js2j;

public class JSPackage {
	
	private String _package;
	
	public JSPackage(String _package) {
		this.setPackage(_package);
	}

	public String getName() {
		return _package;
	}

	public void setPackage(String _package) {
		this._package = _package;
	}
	
	public String toString() {
		return "import "+ this.getName() +";\n";
	}
	
	
}
