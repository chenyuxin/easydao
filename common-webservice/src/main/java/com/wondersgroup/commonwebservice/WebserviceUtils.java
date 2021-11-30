package com.wondersgroup.commonwebservice;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import com.wondersgroup.commonwebservice.util.HTTPSTrustManager;
import com.wondersgroup.commonwebservice.util.webserviceutil.Header;

public class WebserviceUtils {
	
	public static final String NORMAL_CONTENT_TYPE = "application/x-www-form-urlencoded;charset=utf-8";
	public static final String XML_CONTENT_TYPE = "text/xml;charset=utf-8";
	public static final String HTML_CONTENT_TYPE = "text/html;charset=utf-8";
	public static final String JSON_CONTENT_TYPE = "application/json;charset=utf-8";
	
	/**
	 * 接口请求post
	 * @param reqString 请求的body内容
	 * @param URLString 请求地址url
	 * @param header 请求头类
	 * @return
	 * @throws Exception
	 */
	public static String post(String reqString,String URLString, Header header) throws Exception {
		if (null == header) {
			return writeReqPost(reqString, URLString, null);
		} else {
			Map<String, String> headerMap = new HashMap<String,String>();
			headerMap.putAll(header.getReqHeader());
			if(null != header.getContentType() && !header.getContentType().isEmpty() ) {
				headerMap.put("Content-Type", header.getContentType());
			}
			return writeReqPost(reqString, URLString, headerMap);
		}
	}

	/**
	 * 接口请求post
	 * @param reqString 请求的body内容
	 * @param URLString 请求地址url
	 * @param header 请求头map
	 * @return
	 * @throws Exception 
	 */
	public static String writeReqPost(String reqString,String URLString, Map<String, String> header) throws Exception{
		if (URLString.substring(0, 10).toLowerCase().contains("https")) {
			return writeReqHttpsPost(reqString, URLString, header);//执行https的请求
		}
		URL url = new URL(URLString);
		URLConnection uc = url.openConnection();
		uc.setDoOutput(true); // ....必须设置为'true'.  
        //uc.setRequestProperty("Content-Type", "text/xml;charset=utf-8");
        uc.setRequestProperty("Content-Type", NORMAL_CONTENT_TYPE);//默认一个Content-Type，替换可以重新放入header
        if (null != header && !header.isEmpty()) {
			for (Map.Entry<String, String> h : header.entrySet()) {
				uc.setRequestProperty(h.getKey(), h.getValue());
			}
		}
        OutputStreamWriter out = new OutputStreamWriter(uc.getOutputStream(), "utf-8");
        out.write(reqString);
        out.flush();
        out.close();
        
        BufferedReader rd = new BufferedReader(new InputStreamReader(uc.getInputStream(),"utf-8"));  
        StringBuffer responseStr = new StringBuffer();  
        String str;  
        while ((str = rd.readLine()) != null) {  
            responseStr.append(str);
            //str = StringEscapeUtils.unescapeXml(str);//xml标签转换
            //System.out.println(str);
        }
        rd.close(); 
        //System.out.println(responseStr.toString());
        return responseStr.toString();
	}
	
	/**
	 * 接口请求post
	 * @param reqString 请求的body内容
	 * @param URLString 请求地址url
	 * @return
	 * @throws Exception
	 */
	public static String writeReqPost(String reqString,String URLString) throws Exception {
		return writeReqPost(reqString,URLString,null);
	}
	
	/**
	 * 接口请求get
	 * @param reqString 请求的body内容
	 * @param URLString 请求地址url
	 * @param header 请求头类
	 * @return
	 * @throws Exception
	 */
	public static String get(String URLString, Map<String, String> param, Header header) throws Exception {
		if (null == header) {
			return writeReqGet(URLString, param, null);
		} else {
			Map<String, String> headerMap = new HashMap<String,String>();
			headerMap.putAll(header.getReqHeader());
			if(null != header.getContentType() && !header.getContentType().isEmpty() ) {
				headerMap.put("Content-Type", header.getContentType());
			}
			return writeReqGet(URLString, param, headerMap);
		}
	}
	
	/**
	 * 接口请求get
	 * @param URLString 请求地址
	 * @param param 参数map
	 * @param header 头部map
	 * @return
	 * @throws Exception
	 */
	public static String writeReqGet(String URLString, Map<String, String> param, Map<String, String> header) throws Exception {
		StringBuffer lastURLString = new StringBuffer(URLString);
		boolean firstP = true;//get请求地址？区别没有带参数和带参数的情况
		if (URLString.contains("?")) {
			firstP = false;
		}
		if (null != param && !param.isEmpty()) {
			for (Map.Entry<String, String> p : param.entrySet()) {
				if (firstP) {
					lastURLString.append("?");
					firstP = false;
				} else {
					lastURLString.append("&");
				}
				lastURLString.append(p.getKey()).append("=").append(URLEncoder.encode(p.getValue(), "utf-8"));
			}
		}
		//System.out.println(lastURLString);
		URL url = new URL(lastURLString.toString());
		URLConnection uc = url.openConnection();
//		uc.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
		if (null != header && !header.isEmpty()) {
			for (Map.Entry<String, String> h : header.entrySet()) {
				uc.setRequestProperty(h.getKey(), h.getValue());
			}
		}	
		InputStream in = uc.getInputStream();
		InputStreamReader isr = new InputStreamReader(in, "utf-8");
		BufferedReader br = new BufferedReader(isr);
		String line;
		StringBuilder sb = new StringBuilder();
		while ((line = br.readLine()) != null) {
		    sb.append(line);
		}
		br.close();
		isr.close();
		in.close();
		return sb.toString();
	}
	
	/**
	 * 接口请求get
	 * @param URLString 请求地址
	 * @return
	 * @throws Exception
	 */
	public static String writeReqGet(String URLString) throws Exception {
		return writeReqGet(URLString, null, null);
	}
	
	
	
	/**
	 * post的URL地址需要传入get参数是先更新URL为带上get参数的URL, 返回带get参数的URL
	 * @param URLString
	 * @param param
	 * @return URLString 带get参数的
	 * @throws UnsupportedEncodingException
	 */
	public static String getUrlsetParams(String URLString, Map<String, String> param) throws UnsupportedEncodingException{
		StringBuffer lastURLString = new StringBuffer(URLString);
		boolean firstP = true;//get请求地址？区别没有带参数和带参数的情况
		if (URLString.contains("?")) {
			firstP = false;
		}
		if (null != param && !param.isEmpty()) {
			for (Map.Entry<String, String> p : param.entrySet()) {
				if (firstP) {
					lastURLString.append("?");
					firstP = false;
				} else {
					lastURLString.append("&");
				}
				lastURLString.append(p.getKey()).append("=").append(URLEncoder.encode(p.getValue(), "utf-8"));
			}
		}
		//System.out.println(lastURLString);
		return lastURLString.toString();
	}
	
	
	
	/**
     * 发布https 书写报文 请求参数 获取返回 返回报文
     */
	private static String writeReqHttpsPost(String reqString,String URLString,Map<String, String> header) throws Exception{
    	URL url = new URL(URLString);
		HttpsURLConnection uc = (HttpsURLConnection)url.openConnection();
		HTTPSTrustManager.allowAllSSL();//信任所有证书
		uc.setDoOutput(true); // ....必须设置为'true'.  
	    //uc.setRequestProperty("Content-Type", "text/xml;charset=utf-8");
	    uc.setRequestProperty("Content-Type", NORMAL_CONTENT_TYPE);//默认一个Content-Type，替换可以重新放入header
        if (null != header && !header.isEmpty()) {
			for (Map.Entry<String, String> h : header.entrySet()) {
				uc.setRequestProperty(h.getKey(), h.getValue());
			}
		}
	    OutputStreamWriter out = new OutputStreamWriter(uc.getOutputStream(), "utf-8");
	    out.write(reqString);
	    out.flush();
	    out.close();
	        
	    BufferedReader rd = new BufferedReader(new InputStreamReader(uc.getInputStream(),"utf-8"));  
        StringBuffer responseStr = new StringBuffer();  
        String str;  
        while ((str = rd.readLine()) != null) {  
            responseStr.append(str);
            //System.out.println(str);
        }
        rd.close(); 
        //System.out.println(responseStr.toString());
        return responseStr.toString();
    }
	
	
}
