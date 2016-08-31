package js2j.test;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
	
	public static void main(String[] args) throws UnsupportedEncodingException {
//		// TODO Auto-generated method stub
//		// TODO Auto-generated method stub
//		Map<String, String> ghostRequestProperties = Http.getGhostRequestProperties("http://www.youtube-mp3.org", "www.youtube-mp3.org");
//		String youtubeVideoUrl = "http://www.youtube.com/watch?v=KMU0tzLwhbE";
//		String __STEP_1_WRAPPER = "http://www.youtube-mp3.org/a/pushItem/?item=%s&el=na&bf=false&r=%s";
//		String __STEP_2_WRAPPER = "http://www.youtube-mp3.org/a/itemInfo/?video_id=%s&ac=www&t=grp&r=%s";
//		String __STEP_3_WRAPPER = "http://www.youtube-mp3.org/get?ab=128&video_id=%s&h=%s&r=%s.%s";
//
//		__STEP_1_WRAPPER = String.format(__STEP_1_WRAPPER,
//										URLEncoder.encode(youtubeVideoUrl, "utf-8"),
//										Long.toString(new Date().getTime()));
//		__STEP_1_WRAPPER += "&s="+_SIG._sig(__STEP_1_WRAPPER);
//		String youtubeVideoID = null;
//		try {
//			youtubeVideoID = Http.sendRequest(__STEP_1_WRAPPER, "GET", ghostRequestProperties);
//			System.out.println(youtubeVideoID);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		__STEP_2_WRAPPER = String.format(__STEP_2_WRAPPER,
//				URLEncoder.encode(youtubeVideoID, "utf-8"),
//				Long.toString(new Date().getTime()));
//		__STEP_2_WRAPPER += "&s="+_SIG._sig(__STEP_2_WRAPPER);
//		String downloadInfo = null;
//		try {
//			downloadInfo = Http.sendRequest(__STEP_2_WRAPPER, "GET", ghostRequestProperties);
//			try {
//				downloadInfo = downloadInfo.substring(downloadInfo.indexOf("{"));
//				downloadInfo = downloadInfo.replace(";", "");
//			} catch(StringIndexOutOfBoundsException r) {
//				r.printStackTrace();
//			}
//			System.out.println(downloadInfo);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		JSONParser parser = new JSONParser();
//		JSONObject downloadInfoJSON = (JSONObject) parser.parse(downloadInfo);
//		System.out.println(downloadInfoJSON.get("h"));
//		String nowTimestamp = Long.toString(new Date().getTime());
//		__STEP_3_WRAPPER = String.format(__STEP_3_WRAPPER,
//										youtubeVideoID,
//										downloadInfoJSON.get("h"),
//										nowTimestamp,
//										cc(youtubeVideoID+nowTimestamp));
//		__STEP_3_WRAPPER += "&s="+_SIG._sig(__STEP_3_WRAPPER);
//		
//		System.out.println(__STEP_3_WRAPPER);
		
		
		Pattern	v = Pattern.compile("(?:,|\\{)?([^:]*):(\"[^\"]*\"|\\{[^}]*\\}|[^},]*)");
		String s = "kEI:'JbuMVIOfNYStUb2QgKAL',kEXPI :'17427,3700294,4005878,4011550,4011552,4011556,4011559,4016824,4020347,4020562,4021073,4021529,4021587,4021598,4023567,4023678,4023709,4025091,4025103,4025171,4025254,4025760,4025828,4025892,4026397,4026455,4026619,4027308,8300095,8300099,8500393,8500852,8500922,10200083,10200716,10200743,10200763,10200871',authuser:0,j:{    en: 1,bv: 21,pm: 'p',u: '76d6e516',qbp: 0,rre: false   }    ,kSID:'JbuMVIOfNYStUb2QgKAL'";
		String s2 = "'V':function(I,B,P){return I*B*P;},'D':function(I,B){return I<B;},'E':function(I,B){return I==B;},'B3':function(I,B){return I*B;},'G':function(I,B){return I<B;},'v3':function(I,B){return I*B;},'I3':function(I,B){return I in B;},'C':function(I,B){return I%B;},'R3':function(I,B){return I*B;},'O':function(I,B){return I%B;},'Z':function(I,B){return I<B;},'K':function(I,B){return I-B;}";
		System.out.println(s2);
		/*
		 *  Replace all simple quotes after colon, before coma
		 *  or at the end by a double quote
		 */
		s2 = s2.replaceAll("(:?)'(,?)($?)", "$1\"$2");
		/*
		 * Remove all unnecessary space (all spaces not
		 * contained in any values.
		 */
		s2 = s2.replaceAll("(,?)(?<!return)\\s+([^\\w]?)", "$1$2");
		s2 = s2.trim();
				
		System.out.println(s2);
		Matcher m = v.matcher(s2);
		while(m.find())
			System.out.println(m.group(2));
		
//		String s = "var b=1,c=0,d,e";
//		String l = "U=\"R3\",m3=\"round\",e3=\"B3\",D3=\"v3\",N3=\"I3\",g3=\"V\",K3=\"toLowerCase\",n3=\"substr\",z3=\"Z\",d3=\"C\",P3=\"O\",x3=['a','c','e','i','h','m','l','o','n','s','t','.'],G3=[6,7,1,0,10,3,7,8,11,4,7,9,10,8,0,5,2],M=['a','c','b','e','d','g','m','-','s','o','.','p','3','r','u','t','v','y','n'],X=[[17,9,14,15,14,2,3,7,6,11,12,10,9,13,5],[11,6,4,1,9,18,16,10,0,11,11,8,11,9,15,10,1,9,6]],A={\"a\":870,\"b\":906,\"c\":167,\"d\":119,\"e\":130,\"f\":899,\"g\":248,\"h\":123,\"i\":627,\"j\":706,\"k\":694,\"l\":421,\"m\":214,\"n\":561,\"o\":819,\"p\":925,\"q\":857,\"r\":539,\"s\":898,\"t\":866,\"u\":433,\"v\":299,\"w\":137,\"x\":285,\"y\":613,\"z\":635,\"_\":638,\"&\":639,\"-\":880,\"/\":687,\"=\":721},r3=[\"0\",\"1\",\"2\",\"3\",\"4\",\"5\",\"6\",\"7\",\"8\",\"9\"]";
//		String p = "a,b0U['o'](a,i)";
//		String[] split = l.split("(?<!['|\"]),");
//		for(String p: split) 
//			System.out.println(p);
//		System.out.println(splitArguments(p));
//		System.out.println(_SIG._sig("http://www.google.be?p=329991"));
	}
	
//	public static String[] splitArguments(String arguments) {
//		int b = 0, sb = 0, cb = 0;
//		ArrayList<Integer> cutPoints = new ArrayList<Integer>();
//
//		for(int i=0; i<arguments.length(); i++) {
//			switch(arguments.charAt(i)) {
//				case '(': b++; break;
//				case ')': b--; break;
//				case '[': sb++; break;
//				case ']': sb--; break;
//				case '{': cb++; break;
//				case '}': cb--; break;
//				case ',': 
//					if(b == 0 && sb == 0 && cb == 0)
//						cutPoints.add(i);
//					break;
//				default: break;
//			}
//		}
//		
//		System.out.println(cutPoints.size());
//		String[] splittedArguments = new String[cutPoints.size()+1];
//
//		if(cutPoints.size() > 0) {
//			for(int i = 0; i<=cutPoints.size(); i++) {
//				if(i == 0)
//					splittedArguments[i] = arguments.substring(0, cutPoints.get(i));
//				else if(i == cutPoints.size())
//					splittedArguments[i] = arguments.substring(cutPoints.get(i-1)+1);
//				else
//					splittedArguments[i] = arguments.substring(cutPoints.get(i-1)+1, cutPoints.get(i));
//			}
//		}
//		for(String s: splittedArguments) {
//			System.out.println(">"+s);
//		}
//		return new String[] {arguments};
//	}
	
//	public static int _sig(String a) {
//		return _SIG._sig(a);
//	}
	
//	public static int cc(String a) {
//		int __AM = 65521;
//		int b=1,c=0,d;
//		
//		for(int e=0;e<a.length();e++) {
//			d=a.charAt(e);
//			b=(b+d)%__AM;
//			c=(c+b)%__AM;
//		}
//		return c<<16|b;
//	}
}
