package com.example.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	private UserDetailsService userDetailsService;
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Override
	public void configure(WebSecurity web) throws Exception {
		web
			.ignoring()
				.antMatchers("/webjars/**")
				.antMatchers("/css/**")
				.antMatchers("/js/**")
				.antMatchers("/h2-console/**");
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
				.antMatchers("/login").permitAll()
				.antMatchers("/user/signup").permitAll()
				.anyRequest().authenticated();
		
		http
			.formLogin()
				.loginProcessingUrl("/login")
				.loginPage("/login")
				.failureUrl("/login?error")
				.usernameParameter("userId")
				.passwordParameter("password")
				.defaultSuccessUrl("/user/list", true);
		
		http.csrf().disable();
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		PasswordEncoder encoder = passwordEncoder();
		auth
//			.inMemoryAuthentication()
//				.withUser("user")
//				.password(encoder.encode("user"))
//				.roles("GENERAL")
//			.and()
//				.withUser("admin")
//				.password(encoder.encode("admin"))
//				.roles("ADMIN");
			.userDetailsService(userDetailsService)
			.passwordEncoder(encoder);
	}
}
