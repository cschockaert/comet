package com.cschockaert.kcomet;

import java.util.Arrays;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.cometd.bayeux.server.BayeuxServer;
import org.cometd.server.BayeuxServerImpl;
import org.cometd.server.CometDServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.jetty.websocket.jsr356.server.deploy.WebSocketServerContainerInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.boot.context.embedded.ServletContextInitializer;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.boot.context.embedded.jetty.JettyEmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.jetty.JettyServerCustomizer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
@SpringBootApplication
public class ServerStartup {

	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(ServerStartup.class, args);

		System.out.println("Let's inspect the beans provided by Spring Boot:");

		String[] beanNames = ctx.getBeanDefinitionNames();
		Arrays.sort(beanNames);
		for (String beanName : beanNames) {
			System.out.println(beanName);
		}
	}

	private static Logger logger = LoggerFactory.getLogger(ServerStartup.class);

	@Bean
	public JettyEmbeddedServletContainerFactory servletContainerFactory() {
		JettyEmbeddedServletContainerFactory factory = new JettyEmbeddedServletContainerFactory();

		factory.addServerCustomizers(new JettyServerCustomizer() {
			@Override
			public void customize(Server server) {
				try {
					WebSocketServerContainerInitializer.configureContext((WebAppContext) server.getHandler());
				}

				catch (ServletException e) {
					logger.error(e.getMessage());
				}

			}
		});
		return factory;
	}

	@Bean
	public ServletRegistrationBean servletRegistrationBean() {
		CometDServlet cometdServlet = new CometDServlet();
		return new ServletRegistrationBean(cometdServlet, "/cometd/*");
	}

//	@Bean
//	public FilterRegistrationBean filterRegistration() {
//		FilterRegistrationBean filter = new FilterRegistrationBean();
//		CrossOriginFilter crossOriginFilter = new CrossOriginFilter();
//		filter.setFilter(crossOriginFilter);
//		filter.setAsyncSupported(true);
//		filter.setName("cross-origin");
//		filter.addUrlPatterns("/cometd/*", "/chat/template/*");
//		return filter;
//	}

	@Bean
	public ServletContextInitializer initializer() {
		return new ServletContextInitializer() {
			@Override
			public void onStartup(ServletContext servletContext) throws ServletException {
				servletContext.setAttribute(BayeuxServer.ATTRIBUTE, bayeuxServer(servletContext));
			}

		};
	}

	@Bean
	public BayeuxServer bayeuxServer(ServletContext servletContext) {
		BayeuxServerImpl bean = new BayeuxServerImpl();
		bean.addExtension(new org.cometd.server.ext.AcknowledgedMessagesExtension());
		bean.setOption(ServletContext.class.getName(), servletContext);
		bean.setOption("ws.cometdURLMapping", "/cometd/*");
		return bean;
	}

}
