package com.wondersgroup.testppring5.rest;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wondersgroup.commondao.dao.intf.CommonDao;


@RestController
public class DataServiceRestController {
	
	@SuppressWarnings("unused")
	@Autowired private ApplicationContext applicationContext;
	
	//@Autowired GetRegistryData getRegistryData;
	
	@Autowired CommonDao commondao;

	@RequestMapping(value="test/{action}",
			method= {RequestMethod.POST},
			produces="application/json")
	public String test(@RequestBody String body,@RequestHeader Map<String,String> header,
			@RequestParam Map<String, String> getParam,@PathVariable String action ){
//		System.out.println("body:"+ body);
//		System.out.println("header:");
//		System.out.println(header);
//		System.out.println("getParam:");
//		System.out.println(getParam);
		System.out.println("action:" + action);
		
		Date date = commondao.selectBaseObj("select sysdate from dual",Date.class,"dataSource");
		System.out.println(date);
		
		return action;
		
		
		/*
		GetRegistryData getRegistryData = null;
		try {
			getRegistryData = applicationContext.getBean(action, GetRegistryData.class);
		} catch (Exception e) {
			Map<String, Object> resPo = ResponseUtil.createResponse(ResponseHead.NoSupport, "没有找到对应服务:".concat(action));
			return CommonUtil.toJSONString(resPo);
		}
		ReqPo reqPage = null;
		try {
			reqPage = JSON.parseObject(body, ReqPo.class);
		} catch (Exception e) {
			Map<String, Object> resPo = ResponseUtil.createResponse(ResponseHead.Error, "json格式错误");
			return CommonUtil.toJSONString(resPo); 
		}
		int pageSize = reqPage.getPageSize();
		int currentPage = reqPage.getCurrentPage();
		
		if (currentPage < 1) {	currentPage = 1; }
		if (pageSize < 1) { pageSize = 10; }
		
		Map<String, Object> dataMap = new HashMap<String, Object>();
		try {
			PageBean<?> result = getRegistryData.queryData(currentPage, pageSize, reqPage.getParamMap());
			dataMap.put("total", result.getTotal());
			dataMap.put("pageSize", result.getPageSize());
			dataMap.put("currentPage", result.getCurrentPage());
			dataMap.put("rows", result.getRows());
		} catch (Exception e) {
			//e.printStackTrace();
			Map<String, Object> resPo = ResponseUtil.createResponse(ResponseHead.Error, e.getMessage());
			return CommonUtil.toJSONString(resPo);
		}
		
		Map<String, Object> resPo = ResponseUtil.createResponse(ResponseHead.Success, dataMap);
		return CommonUtil.toJSONString(resPo);
		*/
		
	}

}
