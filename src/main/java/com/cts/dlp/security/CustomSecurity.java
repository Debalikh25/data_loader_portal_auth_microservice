package com.cts.dlp.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.cts.dlp.filter.JwtFilter;
import com.cts.dlp.services.CustomUserDetailsService;


@SuppressWarnings("deprecation")
@Configuration
@EnableWebSecurity
public class CustomSecurity extends WebSecurityConfigurerAdapter{
	
	private static final String[] AUTH_WHITELIST = {
	        "/swagger-resources/**",
	        "/swagger-ui.html",
	        "/v2/api-docs",
	        "/webjars/**"
	};
	
	@Autowired
	private CustomUserDetailsService cuds;  
	
	@Autowired
	private JwtFilter filter;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		// TODO Auto-generated method stub
		auth.userDetailsService(cuds).passwordEncoder(passwordEncoder());
		super.configure(auth);
	}
	
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// TODO Auto-generated method stub
		
		
		http
		.cors().and()
		.csrf().disable()
		.authorizeRequests()
		.antMatchers("/api/v1/login" , "/api/v1/token/expired" ,"/swagger-ui/*" , "/api/v1/token/expiry").permitAll()
		.anyRequest().authenticated()
	    .and()
	    .sessionManagement()
	    .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		
		 http.addFilterBefore(this.filter, UsernamePasswordAuthenticationFilter.class);
		
	}
	
	@Override
	public void configure(WebSecurity web) throws Exception {
	    web.ignoring().antMatchers(AUTH_WHITELIST);
	}
	
	@Bean
	public PasswordEncoder passwordEncoder(){
		 return NoOpPasswordEncoder.getInstance();
	}
	
	
	
	
	@Override
	@Bean
	protected AuthenticationManager authenticationManager() throws Exception {
		// TODO Auto-generated method stub
		return super.authenticationManager();
	}
	
	

}
