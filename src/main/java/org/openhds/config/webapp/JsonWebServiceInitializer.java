package org.openhds.config.webapp;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.MarshallingHttpMessageConverter;
import org.springframework.orm.hibernate5.support.OpenSessionInViewFilter;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

public class JsonWebServiceInitializer extends WebMvcConfigurationSupport{

	@Autowired
	private ApplicationContext context;

	@Bean
	public ServletRegistrationBean<DispatcherServlet> jsonRestApi() {
		AnnotationConfigWebApplicationContext contextApi = new AnnotationConfigWebApplicationContext();
		contextApi.scan("org.openhds.webservice.resources.json");
		contextApi.setParent(context);

		DispatcherServlet dispatcherServlet = new DispatcherServlet();
		dispatcherServlet.setApplicationContext(contextApi);

		ServletRegistrationBean<DispatcherServlet> servletRegistrationBean = 
				new ServletRegistrationBean<DispatcherServlet>(dispatcherServlet, "/api2/rest/*");
		servletRegistrationBean.setName("webServices2");
		return servletRegistrationBean;
	}

	@Bean
	FilterRegistrationBean transactionFilterRegistration() {
		FilterRegistrationBean reg = new FilterRegistrationBean();
		reg.setFilter(new OpenSessionInViewFilter());
		reg.addUrlPatterns("/api2/rest/*");
		return reg;
	}
	
	@Bean
	public RequestMappingHandlerAdapter requestMappingHandlerAdapter() {
		RequestMappingHandlerAdapter adapter = super.requestMappingHandlerAdapter();
		adapter.getMessageConverters().add(marshallingHttpMessageConverter());
		adapter.getMessageConverters().add(mappingJackson2HttpMessageConverter());
		return adapter;
	}
	
	@Bean
	public MarshallingHttpMessageConverter marshallingHttpMessageConverter() {
		MarshallingHttpMessageConverter converter = new MarshallingHttpMessageConverter();;
		converter.setMarshaller(new Jaxb2Marshaller());
		converter.setUnmarshaller(new Jaxb2Marshaller());
		
		return converter;
	}
	
	@Bean 
	public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
		return new MappingJackson2HttpMessageConverter();
	}
	
	@Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.favorPathExtension(false).favorParameter(true).parameterName("mediaType")
        .ignoreAcceptHeader(true).useRegisteredExtensionsOnly(false)
        .defaultContentType(MediaType.APPLICATION_JSON)
        .mediaType("xml", MediaType.APPLICATION_XML)
        .mediaType("json", MediaType.APPLICATION_JSON);
    }
	
	
    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
    }
    
    private List<MediaType> getSupportedMediaTypes() {
        final List<MediaType> list = new ArrayList<MediaType>();
        list.add(MediaType.APPLICATION_JSON);
        list.add(MediaType.APPLICATION_XML);

        return list;
    }
    
    
   
}
