package js2j.test;


public class EVAL {

	public static String eval(String s) {
		switch(s) {
			case "location.hostname":
				return "youtube-mp3.org";
			default:
				return "";
		}
	}


}