package palmaslab.mapas.config;

	import java.util.HashSet;
	import java.util.LinkedHashSet;
	import java.util.Set;
	import java.util.concurrent.TimeUnit;
	
	import javax.servlet.MultipartConfigElement;
	import javax.servlet.http.HttpServletRequest;
	import javax.servlet.http.HttpServletResponse;
	import javax.sql.DataSource;
	
	import nz.net.ultraq.thymeleaf.LayoutDialect;
	
	import org.springframework.boot.SpringApplication;
	import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
	import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
	import org.springframework.boot.context.embedded.MultipartConfigFactory;
	import org.springframework.boot.context.embedded.ServletRegistrationBean;
	import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
	import org.springframework.boot.orm.jpa.EntityScan;
	import org.springframework.context.annotation.Bean;
	import org.springframework.context.annotation.ComponentScan;
	import org.springframework.context.annotation.Configuration;
	import org.springframework.context.annotation.Import;
	import org.springframework.context.annotation.PropertySource;
	import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
	import org.springframework.orm.jpa.JpaVendorAdapter;
	import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
	import org.springframework.orm.jpa.vendor.Database;
	import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
	import org.springframework.web.multipart.MultipartResolver;
	import org.springframework.web.multipart.commons.CommonsMultipartResolver;
	import org.springframework.web.multipart.support.StandardServletMultipartResolver;
	import org.springframework.web.servlet.DispatcherServlet;
	import org.springframework.web.servlet.ModelAndView;
	import org.springframework.web.servlet.ViewResolver;
	import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
	import org.springframework.web.servlet.config.annotation.EnableWebMvc;
	import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
	import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
	import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
	import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
	import org.thymeleaf.dialect.IDialect;
	import org.thymeleaf.extras.springsecurity3.dialect.SpringSecurityDialect;
	import org.thymeleaf.spring4.SpringTemplateEngine;
	import org.thymeleaf.spring4.view.ThymeleafViewResolver;
	import org.thymeleaf.templateresolver.ITemplateResolver;
	import org.thymeleaf.templateresolver.ServletContextTemplateResolver;
	
	
	
	@EntityScan(basePackages= "palmaslab.mapas.repository")
	@EnableJpaRepositories(basePackages= "palmaslab.mapas.repository"/*.PostoSaudeRepository.class*/)
	@Configuration
	@EnableAutoConfiguration
	@ComponentScan(basePackages="palmaslab.mapas.controller")
	@Import({palmaslab.mapas.security.SecurityConfiguration.class})
	@EnableWebMvc
	@PropertySource("application.properties")
	public class Application extends WebMvcConfigurerAdapter{
		
		
		
		private static final String[] CLASSPATH_RESOURCE_LOCATIONS = {
	        "classpath:/META-INF/resources/", "classpath:/resources/",
	        "classpath:/static/", "classpath:/public/" };
		
		
		public static void main(String[] args) {
			  SpringApplication.run(Application.class, args);
			 }
		 @Bean
		    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
		            DataSource dataSource, JpaVendorAdapter jpaVendorAdapter) {
		        LocalContainerEntityManagerFactoryBean lef = new LocalContainerEntityManagerFactoryBean();
		        lef.setDataSource(dataSource);
		        
		        lef.setJpaVendorAdapter(jpaVendorAdapter);
		        lef.setPackagesToScan("palmaslab.mapas.controller");
		        return lef;
		    }
	
		  
		    @Bean
		    public JpaVendorAdapter jpaVendorAdapter() {
		        HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
		        hibernateJpaVendorAdapter.setShowSql(false);
		        hibernateJpaVendorAdapter.setGenerateDdl(true); //Auto creating scheme when true
		        hibernateJpaVendorAdapter.setDatabase(Database.H2);//Database type
		        return hibernateJpaVendorAdapter;
		    }
		    
		    @Bean 
		    
		    public SpringTemplateEngine templateEngine() { 
		        SpringTemplateEngine engine = new SpringTemplateEngine();
		        Set<IDialect> dialects = new HashSet<IDialect>();
		        dialects.add(new SpringSecurityDialect());
		        dialects.add(new LayoutDialect());
		        engine.setAdditionalDialects(dialects);
		        LinkedHashSet<ITemplateResolver> templateResolvers = new LinkedHashSet<ITemplateResolver>(2);
		        templateResolvers.add(templateResolverServlet());
		        templateResolvers.add(layoutTemplateResolverServlet());
		        engine.setTemplateResolvers(templateResolvers);
		        return engine;
		    } 
		 
		 
		    @Bean 
		    public ServletContextTemplateResolver layoutTemplateResolverServlet() { 
		        ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver();
		        templateResolver.setPrefix("/WEB-INF/layout/");
		        templateResolver.setSuffix("");
		        templateResolver.setTemplateMode("LEGACYHTML5");
		        templateResolver.setOrder(1);
		        templateResolver.setCacheable(false);
		        return templateResolver;
		    } 
		     
		    @Bean 
		    public ServletContextTemplateResolver templateResolverServlet() { 
		        ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver();
		        templateResolver.setPrefix("/WEB-INF/view/");
		   //     System.out.println("templateResolver.getName()"+templateResolver.getName());
		        templateResolver.setSuffix(".html");
		        templateResolver.setTemplateMode("LEGACYHTML5");
		    //    templateResolver.setTemplateMode("HTML5");
		        templateResolver.setOrder(2);
		        templateResolver.setCacheable(false);
		        return templateResolver;
		    } 
		 
		 
		    @Bean 
		    public ViewResolver thymeleafViewResolver() { 
		        ThymeleafViewResolver resolver = new ThymeleafViewResolver();
		        resolver.setTemplateEngine(templateEngine());
		        resolver.setOrder(1);
		      //  resolver.setCharacterEncoding("ISO-8859-1");
		        resolver.setCharacterEncoding("UTF-8");
		        resolver.setCache(false);
		        return resolver;
		    }     
		/*@Bean
		public InternalResourceViewResolver viewResolver() {
			InternalResourceViewResolver resolver = new InternalResourceViewResolver();
			resolver.setPrefix("/WEB-INF/view/");
			resolver.setSuffix(".jsp");
			System.out.println("!!!!!!!!! internal resourceView");
			return resolver;
		}*/
	
		@Bean
		public ServletRegistrationBean dispatcherRegistration() {
		    ServletRegistrationBean registration = new ServletRegistrationBean(dispatcherServlet());
		    registration.addUrlMappings("/");
		    registration.setLoadOnStartup(1);
	
		    System.out.println("~~~~~~~ Servlet regristated " + registration.getServletName());
		    return registration;
	
		}
		@Override
		public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		    configurer.enable();
		}
	
		@Bean
		public DispatcherServlet dispatcherServlet() {
		    return new DispatcherServlet();
		}
		@Bean
	    public MultipartConfigElement multipartConfigElement() {
	        MultipartConfigFactory factory = new MultipartConfigFactory();
	        factory.setMaxFileSize("9999999KB");
	        factory.setMaxRequestSize("9999999KB");
	        return factory.createMultipartConfig();
	    }
		 @Bean
		  public MultipartResolver multipartResolver() {
		    CommonsMultipartResolver resolver = new CommonsMultipartResolver();
		    resolver.setMaxUploadSize(999999999);
		    return resolver;
		  }
	
		
		@Bean
		public CommonsMultipartResolver filterMultipartResolver() {
		    CommonsMultipartResolver resolver=new CommonsMultipartResolver();
		 //   resolver.setDefaultEncoding("ISO-8859-1");
		    resolver.setDefaultEncoding("UTF-8");
		    resolver.setMaxUploadSize(999999999);
		    resolver.setMaxInMemorySize(999999999);
		    
		    return resolver;
		}
		/*
		 @Bean
		  public MultipartResolver multipartResolver() {
		    return new StandardServletMultipartResolver();
		  }*/
		@Bean
	    public EmbeddedServletContainerFactory servletContainer() {
	        TomcatEmbeddedServletContainerFactory factory = new TomcatEmbeddedServletContainerFactory();
	        factory.setPort(8081);
	        factory.setSessionTimeout(30, TimeUnit.MINUTES);
	        //factory.addErrorPages(new ErrorPage(HttpStatus.404, "/notfound.html"));
	        return factory;
	    }
		@Override
		public void addResourceHandlers(ResourceHandlerRegistry registry) {
		    if (!registry.hasMappingForPattern("/webjars/**")) {
		        registry.addResourceHandler("/webjars/**").addResourceLocations(
		                "classpath:/META-INF/resources/webjars/");
		    }
		    if (!registry.hasMappingForPattern("/locals/**")) {
		        registry.addResourceHandler("/locals/**").addResourceLocations(
		        		 "classpath:/locals");
		   
		    }
		    if (!registry.hasMappingForPattern("/**")) {
				registry.addResourceHandler("/**").addResourceLocations(
						CLASSPATH_RESOURCE_LOCATIONS);
			}
		}
		/*	@Override
		public void addInterceptors(InterceptorRegistry registry) {
	        registry.addInterceptor(new ThymeleafLayoutInterceptor());
	    }
	//INTERCEPTOR DE LAYOUTS!!!
		
		public class ThymeleafLayoutInterceptor extends HandlerInterceptorAdapter {
			 
		    private static final String DEFAULT_LAYOUT = "layouts/default";
		    private static final String DEFAULT_VIEW_ATTRIBUTE_NAME = "view";
		 
		    @Override
		    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		        if (modelAndView == null || !modelAndView.hasView()) {
		            return;
		        }
		        String originalViewName = modelAndView.getViewName();
		        modelAndView.setViewName(DEFAULT_LAYOUT);
		        modelAndView.addObject(DEFAULT_VIEW_ATTRIBUTE_NAME, originalViewName);
		    }    
		}*/
	}
