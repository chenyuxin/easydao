package com.wondersgroup.testppring5.rest;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wondersgroup.commondao.dao.intf.CommonDao;

@RestController
public class TestRestController {
	
	@Autowired CommonDao commonDao;
	
	@RequestMapping(value = "info/{action}",
			method= {RequestMethod.POST,RequestMethod.GET},
			produces={"application/json;charset=UTF-8",MediaType.TEXT_XML_VALUE})
	public String info(@RequestBody String body,@RequestHeader Map<String,String> header,
			@RequestParam Map<String, String> getParam,@PathVariable String action ){
		System.out.println("body:"+ body);
		System.out.println("header:");
		System.out.println(header);
		System.out.println("getParam:");
		System.out.println(getParam);
		System.out.println("action:" + action);
		
		Date date = commonDao.selectBaseObj("select sysdate from dual", Date.class,"dataSource");
		return date.toString();

	}


}
