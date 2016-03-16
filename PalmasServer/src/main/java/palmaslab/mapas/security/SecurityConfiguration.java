package palmaslab.mapas.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.savedrequest.NullRequestCache;
import org.springframework.web.filter.CharacterEncodingFilter;



@Configuration
//Setup Spring Security to intercept incoming requests to the Controllers
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	// This anonymous inner class' onAuthenticationSuccess() method is invoked
	// whenever a client successfully logs in. The class just sends back an
	// HTTP 200 OK status code to the client so that they know they logged
	// in correctly. The class does not redirect the client anywhere like the
	// default handler does with a HTTP 302 response. The redirect has been
	// removed to be friendlier to mobile clients and Retrofit.
	private static final AuthenticationSuccessHandler NO_REDIRECT_SUCCESS_HANDLER = new AuthenticationSuccessHandler() {
		public void onAuthenticationSuccess(HttpServletRequest request,
				HttpServletResponse response, Authentication authentication)
				throws IOException, ServletException {
			response.setStatus(HttpStatus.SC_OK);
		}
	};
	 
	// Normally, the logout success handler redirects the client to the login page. We
	// just want to let the client know that it succcessfully logged out and make the
	// response a bit of JSON so that Retrofit can handle it. The handler sends back
	// a 200 OK response and an empty JSON object.
	private static final LogoutSuccessHandler JSON_LOGOUT_SUCCESS_HANDLER = new LogoutSuccessHandler() {
		public void onLogoutSuccess(HttpServletRequest request,
				HttpServletResponse response, Authentication authentication)
				throws IOException, ServletException {
			response.setStatus(HttpStatus.SC_OK);
			response.setContentType("application/json");
			response.getWriter().write("{}");
		}
	};
	
	/**
	 * This method is used to inject access control policies into Spring
	 * security to control what resources / paths / http methods clients have
	 * access to.
	 */
	@Override
	protected void configure(final HttpSecurity http) throws Exception {
		// By default, Spring inserts a token into web pages to prevent
		// cross-site request forgery attacks. 
		// See: http://en.wikipedia.org/wiki/Cross-site_request_forgery
		//
		// Unfortunately, there is no easy way with the default setup to communicate 
		// these CSRF tokens to a mobile client so we disable them.
		// Don't worry, the next iteration of the example will fix this
		// problem.
		http.csrf().disable();
		// We don't want to cache requests during login
		http.requestCache().requestCache(new NullRequestCache());
		
		// Allow all clients to access the login page and use
		// it to login
		http.formLogin()
			// The default login url on Spring is "j_security_check" ...
		    // which isn't very friendly. We change the login url to
		    // something more reasonable ("/login").
			.loginProcessingUrl("/")
			// The default login system is designed to redirect you to
			// another URL after you successfully authenticate. For mobile
			// clients, we don't want to be redirected, we just want to tell
			// them that they successfully authenticated and return a session
			// cookie to them. this extra configuration option ensures that the 
			// client isn't redirected anywhere with an HTTP 302 response code.
			//.successHandler(NO_REDIRECT_SUCCESS_HANDLER)
			.defaultSuccessUrl("/index")
			// Allow everyone to access the login URL
			.permitAll();
		
		// Make sure that clients can logout too!!
		http.logout()
		    // Change the default logout path to /logout
			.logoutUrl("/home")
			// Make sure that a redirect is not sent to the client
			// on logout
			.logoutSuccessHandler(JSON_LOGOUT_SUCCESS_HANDLER)
			// Allow everyone to access the logout URL
			.permitAll();
		
		// Require clients to login and have an account with the "user" role
		// in order to access /video
		// http.authorizeRequests().antMatchers("/video").hasRole("user");
		
		// Require clients to login and have an account with the "user" role
		// in order to send a POST request to /video
		// http.authorizeRequests().antMatchers(HttpMethod.POST, "/video").hasRole("user");
		
		// We force clients to authenticate before accessing ANY URLs 
		// other than the login and lougout that we have configured above.
		http.authorizeRequests().anyRequest().authenticated();
		
		 CharacterEncodingFilter filter = new CharacterEncodingFilter();
	      //  filter.setEncoding("ISO-8859-1");
		 filter.setEncoding("UTF-8");
	        filter.setForceEncoding(true);
	        http.addFilterBefore(filter,CsrfFilter.class);
	}

	/**
	 * 
	 * This method is used to setup the users that will be able to login to the
	 * system. This is a VERY insecure setup that is using two hardcoded users /
	 * passwords and should never be used for anything other than local testing
	 * on a machine that is not accessible via the Internet. Even if you use
	 * this code for testing, at the bare minimum, you should change the
	 * passwords listed below.
	 * 
	 * @param auth
	 * @throws Exception
	 */
	@Autowired
	protected void registerAuthentication(
			final AuthenticationManagerBuilder auth) throws Exception {
		
		// This example creates a simple in-memory UserDetailService that
		// is provided by Spring
		auth.inMemoryAuthentication()
				.withUser("xxx")
				.password("xxx")
				.authorities("admin","user")
				
				;
	}

}
/*
@Configuration
@EnableWebMvcSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
 
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests().anyRequest().authenticated();
        http
                .formLogin().failureUrl("/login?error")
                .defaultSuccessUrl("/mapa/hello")
                .loginPage("/mapa/login")
                .permitAll()
                .and()
                .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/mapa/login")
                .permitAll();
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!! CONFIGURE");
    }
 
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().withUser("user").password("password").roles("USER");
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!! CONFIGURE 2 ");
    }
	  protected void registerAuthentication(AuthenticationManagerBuilder auth) throws Exception {
	        auth.inMemoryAuthentication()
	            .withUser( "user" ).password( "password" ).roles( "USER" );
	        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!! registerauthentication");
	    }

	    @Bean @Override
	    public AuthenticationManager authenticationManagerBean() throws Exception {
	    	System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!! registerautentication Bean");
	        return super.authenticationManagerBean();
	    }
	    
	
	
}*/
