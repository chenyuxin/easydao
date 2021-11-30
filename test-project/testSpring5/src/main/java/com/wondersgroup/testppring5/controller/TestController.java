package com.wondersgroup.testppring5.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wondersgroup.commondao.dao.daoutil.DaoConfResource;

@Controller
public class TestController {
	
	@Autowired DaoConfResource daoConfResource;
	
	@RequestMapping("/test1")
	public String test1(HttpServletRequest request) {
		System.out.println("ShowSql:" + daoConfResource.isShowSql());
		System.out.println("QueryDateCacheTime:" + daoConfResource.getQueryDateCacheTime());
		request.setAttribute("QueryDateCacheTime", "QueryDateCacheTime:" + daoConfResource.getQueryDateCacheTime());
		return "index";
	}
	
	@RequestMapping("/test2")
	public String test2() {
		return "index-html";
	}

}
