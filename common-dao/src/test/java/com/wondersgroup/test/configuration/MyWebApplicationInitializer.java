package com.wondersgroup.test.configuration;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import com.wondersgroup.commondao.dao.daoutil.springcfg.SpringConfiguration;

/**
 * 代替web.xml
 */
public class MyWebApplicationInitializer implements WebApplicationInitializer {

	@Override
	public void onStartup(ServletContext servletCxt) throws ServletException {
		//通过注解的方式初始化Spring的上下文
        AnnotationConfigWebApplicationContext ac = new AnnotationConfigWebApplicationContext();
        ac.register(SpringConfiguration.class);//注册spring的配置类（替代传统项目中xml的configuration）
        //ac.register(DruidDataSourceAutoConfigure.class);//注册多个配置类自定义
        // Manage the lifecycle of the root application context
        servletCxt.addListener(new ContextLoaderListener(ac));
        
        //通过注解的方式初始化SpringMvc的上下文
        AnnotationConfigWebApplicationContext dispatcherContext = new AnnotationConfigWebApplicationContext();
        dispatcherContext.register(MvcConfig.class);//注册springMvc的配置类（替代传统项目中xml的configuration）
        //基于java代码的方式初始化DispatcherServlet
        DispatcherServlet servlet = new DispatcherServlet(dispatcherContext);
        ServletRegistration.Dynamic springmvc = servletCxt.addServlet("springmvc", servlet);
        springmvc.addMapping("/");
        //springmvc.setLoadOnStartup(1);//启动时启动
	}

}
