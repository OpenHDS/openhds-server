package org.openhds.config.webapp;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate5.support.OpenSessionInViewFilter;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class XmlWebServiceInitializer extends WebMvcConfigurationSupport{

	@Autowired
	private ApplicationContext context;

	@Bean
	public ServletRegistrationBean<DispatcherServlet> xmlRestApi() {
		AnnotationConfigWebApplicationContext contextApi = new AnnotationConfigWebApplicationContext();
		contextApi.scan("org.openhds.webservice.resources.xml");
		contextApi.setParent(context);

		DispatcherServlet dispatcherServlet = new DispatcherServlet();
		dispatcherServlet.setApplicationContext(contextApi);

		ServletRegistrationBean<DispatcherServlet> servletRegistrationBean = 
				new ServletRegistrationBean<DispatcherServlet>(dispatcherServlet, "/api/rest/*");
		servletRegistrationBean.setName("webServices");
		return servletRegistrationBean;
	}

	@Bean
	FilterRegistrationBean transactionFilterRegistration() {
		FilterRegistrationBean reg = new FilterRegistrationBean();
		reg.setFilter(new OpenSessionInViewFilter());
		reg.addUrlPatterns("/api/rest/*");
		return reg;
	}
}
