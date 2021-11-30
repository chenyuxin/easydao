package configuration;

import org.apache.cxf.Bus;
import org.apache.cxf.bus.spring.BusExtensionPostProcessor;
import org.apache.cxf.bus.spring.BusWiringBeanFactoryPostProcessor;
import org.apache.cxf.bus.spring.Jsr250BeanPostProcessor;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.jaxrs.JAXRSBindingFactory;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
//import org.apache.cxf.transport.servlet.CXFServlet;
import org.aspectj.weaver.ast.Test;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class CxfConfig {
	
//	@SuppressWarnings({ "unchecked", "rawtypes" })
//	@Bean
//	public ServletRegistrationBean newServlet() {
//		return new ServletRegistrationBean(new CXFServlet(), "/brmp-webservice-in/webservice/*");
//	}
	
	@Autowired Jsr250BeanPostProcessor jsr250BeanPostProcessor;
	
	@Autowired BusExtensionPostProcessor busExtensionPostProcessor;
	
	
	@Bean(name = Bus.DEFAULT_BUS_ID,destroyMethod = "shutdown")
	public SpringBus springBus() {
		return new SpringBus();
	}
	
	@Bean(name = "org.apache.cxf.bus.spring.BusWiringBeanFactoryPostProcessor")
	public BusWiringBeanFactoryPostProcessor busWiringBeanFactoryPostProcessor() {
		return new BusWiringBeanFactoryPostProcessor();
	}
	
	/**
	 * 过滤拦截，设置允许跨域使用
	 * @return
	 */
	@Bean("cors-filter")
	public org.apache.cxf.rs.security.cors.CrossOriginResourceSharingFilter CrossOriginResourceSharingFilter() {
		return new org.apache.cxf.rs.security.cors.CrossOriginResourceSharingFilter();
	}
	
	@Bean(name="XXXX")
	public JAXRSServerFactoryBean server(Test test) {
		JAXRSServerFactoryBean sf = new JAXRSServerFactoryBean();
		sf.setResourceClasses(springBus().getClass());
		sf.setBindingId(JAXRSBindingFactory.JAXRS_BINDING_ID);
		
		sf.setAddress("/XXXX");
		
		sf.setServiceBean(test);
		sf.setProvider(CrossOriginResourceSharingFilter());
		
		sf.create();
		return sf;
	}
	
 
 
	
 
	
	
	
	
	

}
