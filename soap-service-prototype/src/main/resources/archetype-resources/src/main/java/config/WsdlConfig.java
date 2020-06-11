package ${package}.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.soap.server.endpoint.SoapFaultDefinition;
import org.springframework.ws.soap.server.endpoint.SoapFaultMappingExceptionResolver;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.XsdSchemaCollection;
import org.springframework.xml.xsd.commons.CommonsXsdSchemaCollection;
import ${package}.exceptions.DetailSoapFaultDefinitionExceptionResolver;
import ${package}.exceptions.ServiceFaultException;


import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Properties;

@EnableWs
@Configuration
public class WsdlConfig extends WsConfigurerAdapter {

    @Value("${wsdl.LocationUri}")
    private String wsdlLocationUri;

    @Bean
    public SoapFaultMappingExceptionResolver exceptionResolver(){
        SoapFaultMappingExceptionResolver exceptionResolver = new DetailSoapFaultDefinitionExceptionResolver();

        SoapFaultDefinition faultDefinition = new SoapFaultDefinition();
        faultDefinition.setFaultCode(SoapFaultDefinition.SERVER);
        exceptionResolver.setDefaultFault(faultDefinition);

        Properties errorMappings = new Properties();
        errorMappings.setProperty(Exception.class.getName(), SoapFaultDefinition.SERVER.toString());
        errorMappings.setProperty(ServiceFaultException.class.getName(), SoapFaultDefinition.SERVER.toString());
        exceptionResolver.setExceptionMappings(errorMappings);
        exceptionResolver.setOrder(1);
        return exceptionResolver;
    }

    @Bean
    public ServletRegistrationBean messageDispatcherServlet(ApplicationContext applicationContext) {
        MessageDispatcherServlet servlet = new MessageDispatcherServlet();
        servlet.setApplicationContext(applicationContext);
        servlet.setTransformWsdlLocations(false);
        return new ServletRegistrationBean(servlet, "/ws/*");
    }

    //тестовый endpoint
    @Bean(name = "test")
    public DefaultWsdl11Definition defaultWsdl11Definition(XsdSchemaCollection schemaCollection) {
        DefaultWsdl11Definition definition = new DefaultWsdl11Definition();
        definition.setPortTypeName("testPort");
        definition.setLocationUri(wsdlLocationUri);
        definition.setSchemaCollection(schemaCollection);
        return definition;
    }

    @Bean
    public XsdSchemaCollection schemaCollection() {
        CommonsXsdSchemaCollection commonsXsdSchemaCollection = new CommonsXsdSchemaCollection(
                new ClassPathResource("schemas/test/test.xsd")
                );
        commonsXsdSchemaCollection.setInline(true);
        return commonsXsdSchemaCollection;
    }

    @Bean
    public OncePerRequestFilter wsdRequestCompatibilityFilter() {
        return new OncePerRequestFilter(){
            @Override
            protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
                if ("GET".equals(httpServletRequest.getMethod()) && "wsdl".equalsIgnoreCase(httpServletRequest.getQueryString()) ) {
                    httpServletRequest.getSession().getServletContext().getRequestDispatcher(httpServletRequest.getRequestURI() + ".wsdl").forward(httpServletRequest, httpServletResponse);
                } else if("GET".equals(httpServletRequest.getMethod()) && httpServletRequest.getRequestURI().matches(".*wsdl")){
                    filterChain.doFilter(httpServletRequest, httpServletResponse);
                }else if("GET".equals(httpServletRequest.getMethod())){
                    httpServletRequest.getSession().getServletContext().getRequestDispatcher("/serviceInfo").forward(httpServletRequest, httpServletResponse);
                } else {
                    filterChain.doFilter(httpServletRequest, httpServletResponse);
                }
            }
        };
    }
}
