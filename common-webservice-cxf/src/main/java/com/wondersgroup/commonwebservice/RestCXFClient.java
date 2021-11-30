package com.wondersgroup.commonwebservice;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;

public class RestCXFClient {
	
	public void reqTestCenterGet(){
		String baseAddress = "http://localhost:8980/EMPI_center/webservice/EMPIObj/putEMPIObj";
		WebClient webClient = WebClient.create(baseAddress);
		webClient.acceptEncoding("utf-8");
		webClient.encoding("utf-8");
		webClient.accept(MediaType.APPLICATION_JSON);
		webClient.type(MediaType.APPLICATION_JSON);
		Response resp = webClient.post("A91o哦");
		
		//Response resp = webClient.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON).get();
		//System.out.println(resp.getMetadata());
		//readEntity（“方法的返回值类型”）
		//System.out.println(resp3.readEntity(User.class));
		System.out.println(resp.readEntity(String.class));
	}
	
	/**
	 * 向 RestService
	 * 请求post
	 */
	public static String reqRestService(String baseClientURL,String str){
		return reqRestService(baseClientURL,str,-1);
	}
	
	/**
	 * 向  RestService
	 * 请求post
	 * 超时设置
	 */
	public static String reqRestService(String baseClientURL,String str,long timeOut){
		WebClient webClient = WebClient.create(baseClientURL);
		webClient.acceptEncoding("utf-8");
		webClient.encoding("utf-8");
		webClient.accept(MediaType.APPLICATION_JSON);
		webClient.type(MediaType.APPLICATION_JSON);

		if ( timeOut > 0 ) {
			HTTPConduit conduit = WebClient.getConfig(webClient).getHttpConduit(); 
			HTTPClientPolicy policy = new HTTPClientPolicy();
			policy.setReceiveTimeout(timeOut);
			conduit.setClient(policy);
		}
		/*
		TimeOutThread timeOutThread = new TimeOutThread(timeOut, webClient, "close");
		timeOutThread.start();//开始计时
		Response resp = webClient.post(str);
		timeOutThread.end();//成功返回结果后终止计时
		*/
		Response resp = webClient.post(str);
		return resp.readEntity(String.class);
	}
	
	public static void main(String[] args) {
		reqRestService("http://59.225.200.64:8790/ssoguojia-webservice/webservice/UserDeleteService", "aaa", 1);
	}

}
