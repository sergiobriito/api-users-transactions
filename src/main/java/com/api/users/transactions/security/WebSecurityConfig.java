package com.api.users.transactions.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.api.users.transactions.services.UserService;

@Configuration
public class WebSecurityConfig  {
	
	@Autowired
	UserService userService;
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
		http
			.httpBasic()
			.and()
			.authorizeHttpRequests()
			.requestMatchers(HttpMethod.GET,"/user/**").permitAll()
			.requestMatchers(HttpMethod.POST,"/user").permitAll()
			.requestMatchers(HttpMethod.PUT,"/user/**").permitAll()
			.requestMatchers(HttpMethod.DELETE,"/user/**").permitAll()
			.requestMatchers(HttpMethod.GET,"/transaction/**").permitAll()
			.requestMatchers(HttpMethod.POST,"/transaction").hasRole("USER")
			.anyRequest().authenticated()
			.and()
			.csrf().disable();
		return http.build();
	};
	
	
	protected void configure(AuthenticationManagerBuilder auth) throws Exception{
		auth.userDetailsService(userService).passwordEncoder(passwordEncoder());
	};
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	};
}
