// Enum : http://javahowto.blogspot.be/2008/04/java-enum-examples.html

package js2j;

public enum JSPrimitiveType {
		
	Int(1), Double(2);
	
	private int powerIndex;

	JSPrimitiveType(int powerIndex) {
		this.setPowerIndex(powerIndex);
	}
	
	public static JSPrimitiveType asJSPrimitiveType(String str) {
	    for (JSPrimitiveType me : JSPrimitiveType.values()) {
	        if (me.name().equalsIgnoreCase(str))
	            return me;
	    }
	    return null;
	}
	
	public static JSPrimitiveType asJSPrimitiveType(int powerIndex) {
	    for (JSPrimitiveType me : JSPrimitiveType.values()) {
	        if (me.getPowerIndex() == powerIndex)
	            return me;
	    }
	    return null;
	}

	public int getPowerIndex() {
		return powerIndex;
	}

	public void setPowerIndex(int powerIndex) {
		this.powerIndex = powerIndex;
	}
}
