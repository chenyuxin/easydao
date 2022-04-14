package com.wondersgroup.common.spring.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

/**
 * 动态IOC容器注入Bean
 * @author 陈宇昕
 *
 */
@Component
public class DynamicIocUtilComponent {
	
	@Autowired ApplicationContext applicationContext;
	
	/**
	 * IOC容器注入Bean,单例模式
	 * @param beanName the name of the bean
	 * @param singletonObject the existing singleton object
	 */
	public void registerBean(String beanName, Object singletonObject) {
		//将applicationContext转换为ConfigurableApplicationContext
        ConfigurableApplicationContext configurableApplicationContext = (ConfigurableApplicationContext) applicationContext;
        // 获取bean工厂并转换为DefaultListableBeanFactory
        DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) configurableApplicationContext.getBeanFactory();
        defaultListableBeanFactory.registerSingleton(beanName, singletonObject);
	}
	
	

}
