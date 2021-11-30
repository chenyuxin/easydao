package com.wondersgroup.test.configuration;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.druid.pool.DruidDataSource;
import com.wondersgroup.commondao.dao.daoutil.springcfg.SpringConfiguration;

/**
 * 多数据源配置样例
 */
@Configuration
@AutoConfigureAfter(SpringConfiguration.class)//此处在默认配置之后再读此配置。（需要默认配置首先建立默认数据源）
public class MoreDataSourceConfiguration {
	
	/**
	 * 记录下此注入bean的 beanName， 在具体使用的时候，给commonDao传参，让commonDao执行此数据源的数据库连接。
	 */
	@Bean(name = "dataSource_auth",destroyMethod = "close")
	public static DataSource dataSourceAuth(
			@Value("${auth.db.driver_class}") String driverClassName,
			@Value("${auth.db.url}") String url,
			@Value("${auth.db.username}") String username,
			@Value("${auth.db.password}") String password,
			@Value("${auth.db.pool.initialSize:1}") int initialSize,
			@Value("${auth.db.pool.minIdle:1}") int minIdle,
			@Value("${auth.db.pool.maxActive:5}") int maxActive,
			@Value("${auth.db.pool.maxWait:180}") int maxWait,
			@Value("${auth.db.pool.vsql}") String validationQuery
			) throws SQLException {
		DruidDataSource dataSource = new DruidDataSource();
		dataSource.setDriverClassName(driverClassName);
		dataSource.setUrl(url);
		dataSource.setUsername(username);
		dataSource.setPassword(password);
		
		//配置初始化大小、最小、最大
		dataSource.setInitialSize(initialSize);
		dataSource.setMinIdle(minIdle);
		dataSource.setMaxActive(maxActive);
		
		//配置获取连接等待超时的时间
		dataSource.setMaxWait(maxWait);
		
		//验证连接测试sql
		dataSource.setValidationQuery(validationQuery);
		
		//配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒
		dataSource.setTimeBetweenConnectErrorMillis(60000);
		// 配置一个连接在池中最小生存的时间，单位是毫秒
		dataSource.setMinEvictableIdleTimeMillis(300000);
		
		dataSource.setTestWhileIdle(true);
		dataSource.setTestOnBorrow(false);//申请连接时执行validationQuery检测连接是否有效,设为TRUE影响性能
		dataSource.setTestOnReturn(false);//归还连接时执行validationQuery检测连接是否有效,设为TRUE影响性能
		
		//打开PSCache，并且指定每个连接上PSCache的大小
		dataSource.setPoolPreparedStatements(true);
		dataSource.setMaxPoolPreparedStatementPerConnectionSize(100);
		
		dataSource.setRemoveAbandoned(true);//对于长时间不使用的连接强制关闭
		dataSource.setRemoveAbandonedTimeout(1800);//超过30分钟开始关闭空闲连接 
		
		//长时间保证连接的有效性，防止数据库收回所有连接
		dataSource.setKeepAlive(true);
		dataSource.setConnectionProperties("keepAlive=true");
		
		dataSource.setLogAbandoned(true);//将当前关闭动作记录到日志
		dataSource.setFilters("stat");//配置监控统计拦截的filters
		return dataSource;
		
	}
	

}
