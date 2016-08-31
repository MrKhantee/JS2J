// http://stackoverflow.com/questions/5245840/how-to-convert-string-to-jsonobject-in-java
// https://gist.github.com/zvineyard/2012742
// http://stackoverflow.com/questions/1395551/convert-a-json-string-to-object-in-java
// http://www.java-forums.org/blogs/java-socket/664-how-send-http-request-url.html
// Open URL in browser : http://stackoverflow.com/questions/527719/how-to-add-hyperlink-in-jlabel
// Get WebPage Title : http://www.mkyong.com/java/jsoup-html-parser-hello-world-examples/
// Unescape HTML strings to UNICODE : http://stackoverflow.com/questions/13789799/convert-html-escaped-strings-to-plain-unicode-ascii

package js2j.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Http {
	
    public static LinkedHashMap<String, String> parseQuery(URL url) throws UnsupportedEncodingException {
        LinkedHashMap<String, String> queryParams = new LinkedHashMap<String, String>();
        String query = url.getQuery();
        String[] params = query.split("&");
        for (String p : params) {
            int idx = p.indexOf("=");
            queryParams.put(URLDecoder.decode(p.substring(0, idx), "UTF-8"), URLDecoder.decode(p.substring(idx + 1), "UTF-8"));
        }
        return queryParams;
    }
    
    /**
     * 
     * @param 	requestUrl
     * @param 	method
     * @param 	parameters
     * @param 	requestProperties
     * @return	
     * @throws 	IOException
     */
    public static String sendRequest(String requestUrl, String method,
        	String parameters, String[]...requestProperties) throws IOException {
    	
    	List<String> response = new ArrayList<String>();
        
        URL url = new URL(requestUrl);
        URLConnection urlConn = url.openConnection();
        urlConn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
    	urlConn.setRequestProperty("Referrer","http://");
        urlConn.setUseCaches(false);
        for(String[] requestProperty: requestProperties)
        	urlConn.setRequestProperty(requestProperty[0],requestProperty[1]);
        urlConn.setReadTimeout(1000);
        
        // the request will return a response
        urlConn.setDoInput(true);
        
        if ("POST".equals(method)) {
        	// set request method to POST
        	urlConn.setDoOutput(true);
        } else {
        	// set request method to GET
        	urlConn.setDoOutput(false);
        }
        
        if ("POST".equals(method) && parameters != null && parameters.length() > 0) {
        	OutputStreamWriter writer = new OutputStreamWriter(urlConn.getOutputStream());
        	writer.write(parameters);
        	writer.flush();  
        }
        
        // reads response, store line by line in an array of Strings
        BufferedReader reader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
 
        String line = "";
        while ((line = reader.readLine()) != null) {
        	response.add(line);
        }
        
        reader.close();
        
        StringBuilder builder = new StringBuilder();
        for(String s : response.toArray(new String[0])) {
            builder.append(s);
        }
        return builder.toString();   
//        return (String[]) response.toArray(new String[0]);
    }
    
    /**
     * Makes a HTTP request to a URL and receive response
     * @param requestUrl the URL address
     * @param method Indicates the request method, "POST" or "GET"
     * @param params a map of parameters send along with the request
     * @return An array of String containing text lines in response
     * @throws IOException
     */
    public static String sendRequest(String requestUrl, String method,
    	Map<String, String> params) throws IOException {
    	StringBuffer requestParams = new StringBuffer();
        
        if (params != null && params.size() > 0) {
        	Iterator<String> paramIterator = params.keySet().iterator();
        	while (paramIterator.hasNext()) {
        		String key = paramIterator.next();
        		String value = params.get(key);
        		try {
					requestParams.append(URLEncoder.encode(key, "UTF-8"));
					requestParams.append("=").append(URLEncoder.encode(value, "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
        		requestParams.append("&");
        	}
        }
        String response = null;
		response = sendRequest(requestUrl, method, new String(requestParams));
        return response;
    }
    
    public static void updateGhostRequestProperties(String userAgent, String acceptLanguage, String accept) {
		Map<String,String> ghostRequestProperties = new HashMap<String,String>();
		ghostRequestProperties.put("User-Agent", userAgent);
		ghostRequestProperties.put("Referer", "");
		ghostRequestProperties.put("Host", "");
		ghostRequestProperties.put("Connection", "keep-alive");
		ghostRequestProperties.put("Cache-Control", "no-cache");
		ghostRequestProperties.put("Accept-Location", "*");
		ghostRequestProperties.put("Accept-Language", acceptLanguage); // To change
		ghostRequestProperties.put("Accept-Encoding", "gzip, deflate");
		ghostRequestProperties.put("Accept", acceptLanguage); // To Change
    }
    
    public static Map<String, String> getGhostRequestProperties(String referer, String host) {
		ghostRequestProperties.put("Referer", referer);
		ghostRequestProperties.put("Host", host);
    	return Http.ghostRequestProperties;
    }
    
	private static Map<String,String> ghostRequestProperties = new HashMap<String,String>();
    
}
