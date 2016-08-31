package js2j;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import org.apache.commons.collections.map.LinkedMap;

public class Main {
	
	public static void main(String[] args) {
		String jsScript = "(function(){window.google={kEI:'d5KIVMi3FcL0UIrYgIgE',kEXPI:'3700294,4011550,4011552,4011556,4011559,4016824,4020347,4020562,4021073,4021529,4021587,4021598,4023567,4023678,4023709,4025091,4025171,4025254,4025285,4025760,4025828,4025892,4026224,4026397,8300095,8500393,8500852,8500922,10200083,10200716,10200743,10200763,10200871',authuser:0,j:{en:1,bv:21,pm:'p',u:'76d6e516',qbp:0,rre:false},kSID:'d5KIVMi3FcL0UIrYgIgE'};google.kHL='fr-BE';})();(function(){google.lc=[];google.li=0;google.getEI=function(a){for(var b;a&&(!a.getAttribute||!(b=a.getAttribute(\"eid\")));)a=a.parentNode;return b||google.kEI};google.https=function(){return \"https:\"==window.location.protocol};google.ml=function(){};google.time=function(){return(new Date).getTime()};google.log=function(a,b,d,e,k){var c=new Image,h=google.lc,f=google.li,g=\"\",l=google.ls||\"\";c.onerror=c.onload=c.onabort=function(){delete h[f]};h[f]=c;d||-1!=b.search(\"&ei=\")||(e=google.getEI(e),g=\"&ei=\"+e,e!=google.kEI&&(g+=\"&lei=\"+google.kEI));a=d||\"/\"+(k||\"gen_204\")+\"?atyp=i&ct=\"+a+\"&cad=\"+b+g+l+\"&zx=\"+google.time();/^http:/i.test(a)&&google.https()?(google.ml(Error(\"a\"),!1,{src:a,glmm:1}),delete h[f]):(c.src=a,google.li=f+1)};google.y={};google.x=function(a,b){google.y[a.id]=[a,b];return!1};google.load=function(a,b,d){google.x({id:a+m++},function(){google.load(a,b,d)})};var m=0;})();google.kCSI={};google.j.b=(!!location.hash&&!!location.hash.match('[#&]((q|fp)=|tbs=rimg|tbs=simg|tbs=sbi)'))||(google.j.qbp==1);(function(){google.sn=\"webhp\";google.timers={};";
		new JSScript(jsScript).read();//.out("C:/Users/Max/Desktop/js2j");
//		
//		/**
//		 * Combine all bytecode files into a single jar
//		 */
//		FileUtils.buildProject();
		
//		LinkedMap p = parseJSON(format(jsScript));
//		System.err.println((p.get("V")));
//		String content = null;
//		try {
//			content = new Scanner(new File("C:/Users/Max/Desktop/y-mp3.org-jsscript.txt")).useDelimiter("\\Z").next();
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		parseScript(format(content));
//		parseScript(jsScript2);
	}
	
	public static void parseScript(String jsScript) {		
		ArrayList<Integer> d = new ArrayList<Integer>();
		d.add(0);
		int j = 0, i = 0, k = 0;
		System.out.println(jsScript.length());
		while(j < jsScript.length()) {
			int del = 0;
			do {
				char c = jsScript.charAt(i);
				if(i > jsScript.length()-1) break;
				if(c == '{') del++;
				if(c == '}') del--;
				if(c == '(') del++;
				if(c == ')') del--;
				i++;
			} while(del > 0 || jsScript.charAt(i) != ';');
			d.add(++i);
			System.out.println(jsScript.substring(d.get(k),i-1));
			j = i;
			k++;
		}
	}
	
	public static LinkedMap parseJSON(String jsScript) {
		ArrayList<Integer> a = new ArrayList<Integer>();
		LinkedMap p = new LinkedMap();	
		a.add(0);
		int j = 0, i = 0, k = 0;
		while(j < jsScript.length()) {
			int del = 0, dsq = 0, ddq = 0;
			while(true) {
				if(i > jsScript.length()-1) break;
				char c = jsScript.charAt(i);
				if(c == '{') del++;
				else if(c == '}') del--;
				else if(c == '(') del++;
				else if(c == ')') del--;
				else if(c == '\"') 
					if(ddq > 0 && jsScript.charAt(i-1) != '\\') ddq--; else ddq++;
				else if(c == '\'') 
					if(dsq > 0 && jsScript.charAt(i-1) != '\\') dsq--; else dsq++;
				else if(c == ',' && del == 0 && dsq == 0 && ddq == 0) {
					String d = jsScript.substring(a.get(k),i);
					if(d.indexOf('{') == 0 && d.lastIndexOf('}') == d.length()-1)
						// Recursively parse the object 
						p.put(p.lastKey(), parseJSON(d.substring(1,d.length()-1)));
					else
						// Register the value
						p.put(p.lastKey(), d);
					break;
				} else if(c == ':' && del == 0 && dsq == 0 && ddq == 0) {
					// Remove any quote in the key
					String key = jsScript.substring(a.get(k),i).replace("'", "").replace("\"", "");
					// Register the key
					p.put(key, null);
					break;
				} else if(i == jsScript.length()-1) {
					// Register the last value of the current object
					p.put(p.lastKey(), jsScript.substring(a.get(k),i+1));
				}
				i++;
			}
			a.add(++i);
			j = i;
			k++;
		}
		return p;
	}
		
	public static String format(String jsScript) {
		/*
		 *  Replace all simple quotes after colon, before coma
		 *  or at the end by a double quote
		 */
		jsScript = jsScript.replaceAll("(:?)'(,?)($?)", "$1\"$2");
		/*
		 * Remove all unnecessary space (all spaces not
		 * contained in any values.
		 */
		jsScript = jsScript.replaceAll("(,?)(?<!return|var|function|typeof|throw|else|do|\\<a|\\<div)\\s+([^\\w]?)", "$1$2");
		return jsScript.trim();
	}
}
