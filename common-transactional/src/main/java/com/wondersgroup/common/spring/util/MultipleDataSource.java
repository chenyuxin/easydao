package com.wondersgroup.common.spring.util;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;

import com.alibaba.druid.pool.DruidDataSource;
import com.wondersgroup.commondao.dao.daoutil.DaoUtil;
import com.wondersgroup.commonutil.type.database.DataBaseType;

@Component
public class MultipleDataSource {
	
	@Autowired ApplicationContext applicationContext;
	
	/**
	 * 注册新数据源入Ioc,<br>
	 * 同时注入该数据源的事务管理，Ioc容器中id为新数据源的beanName+"TransactionManager"
	 * @param beanName 
	 * @param ip
	 * @param port
	 * @param dataBaseName 数据库实例名
	 * @param dataBaseType
	 */
	public void registerDataSource(String beanName,String ip,int port,String dataBaseName,DataBaseType dataBaseType) {
		DruidDataSource defaultDataSource = (DruidDataSource) applicationContext.getBean(DaoUtil.defaultDataSourceName);
		DruidDataSource newDataSource = defaultDataSource.cloneDruidDataSource();
		newDataSource.setDriverClassName(dataBaseType.getJdbcDriverClassName());
		newDataSource.setUrl(dataBaseType.getJdbcUrl(ip, port, dataBaseName));
		try {
			newDataSource.init();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager(); //定义事务管理TransactionManager
        dataSourceTransactionManager.setDataSource(newDataSource); // 设置数据源
		
		//将applicationContext转换为ConfigurableApplicationContext
        ConfigurableApplicationContext configurableApplicationContext = (ConfigurableApplicationContext) applicationContext;
        // 获取bean工厂并转换为DefaultListableBeanFactory
        DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) configurableApplicationContext.getBeanFactory();
        defaultListableBeanFactory.registerSingleton(beanName, newDataSource);
        defaultListableBeanFactory.registerSingleton(beanName + DaoUtil.TransactionManager, dataSourceTransactionManager);
		//System.out.println(beanName + DaoUtil.TransactionManager);
	}
	
	/**
	 * 注册新数据源入Ioc,<br>
	 * 同时注入该数据源的事务管理，Ioc容器中id为新数据源的beanName+"TransactionManager"
	 * @param beanName
	 * @param ip
	 * @param port
	 * @param dataBaseName 数据库实例名
	 * @param dataBaseType
	 * @param username
	 * @param password
	 */
	public void registerDataSource(String beanName,String ip,int port,String dataBaseName,DataBaseType dataBaseType,String username,String password) {
		DruidDataSource defaultDataSource = (DruidDataSource) applicationContext.getBean(DaoUtil.defaultDataSourceName);
		DruidDataSource newDataSource = defaultDataSource.cloneDruidDataSource();
		newDataSource.setDriverClassName(dataBaseType.getJdbcDriverClassName());
		newDataSource.setUrl(dataBaseType.getJdbcUrl(ip, port, dataBaseName));
		newDataSource.setUsername(username);
		newDataSource.setPassword(password);
		try {
			newDataSource.init();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager(); //定义事务管理TransactionManager
        dataSourceTransactionManager.setDataSource(newDataSource); // 设置数据源
		
		//将applicationContext转换为ConfigurableApplicationContext
        ConfigurableApplicationContext configurableApplicationContext = (ConfigurableApplicationContext) applicationContext;
        // 获取bean工厂并转换为DefaultListableBeanFactory
        DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) configurableApplicationContext.getBeanFactory();
        defaultListableBeanFactory.registerSingleton(beanName, newDataSource);
        defaultListableBeanFactory.registerSingleton(beanName + DaoUtil.TransactionManager, dataSourceTransactionManager);
	}

}
