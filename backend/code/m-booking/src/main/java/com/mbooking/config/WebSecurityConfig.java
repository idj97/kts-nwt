package com.mbooking.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.mbooking.security.impl.AuthenticationEntryPointImpl;
import com.mbooking.security.impl.AuthenticationFilterImpl;
import com.mbooking.security.impl.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private UserDetailsServiceImpl userDetails;
	
	@Autowired
	private AuthenticationEntryPointImpl authEntryPoint;
	
	@Autowired
	private AuthenticationFilterImpl authFilter;
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetails).passwordEncoder(passwordEncoder());
	}
	
	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.cors().disable();
		http.csrf().disable();
		http.headers().frameOptions().disable(); //Need this line for h2
		http
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
			.exceptionHandling().authenticationEntryPoint(authEntryPoint).and()
			.authorizeRequests()
				.antMatchers("/api/auth/login/**").permitAll()
				.antMatchers("/api/users/admins").permitAll()
				.antMatchers("/api/users/register/**").permitAll()
				.antMatchers("/api/users/confirm_registration/**").permitAll()
				.antMatchers("/api/layouts/**").permitAll()
				.antMatchers(HttpMethod.GET, "/api/locations/**").permitAll()
				.antMatchers(HttpMethod.GET, "/api/reservations/**").permitAll()
				.antMatchers("/h2/**").permitAll()
				.antMatchers(HttpMethod.GET, "/api/manifestation/**").permitAll()
			.anyRequest().authenticated().and()
			.addFilterBefore(authFilter, BasicAuthenticationFilter.class).httpBasic();
	}
	
	@Override
	public void configure(WebSecurity web) {
		web.ignoring().antMatchers(HttpMethod.POST, "/api/auth/login/**");
		web.ignoring().antMatchers(HttpMethod.GET, "/", "/webjars/**", "/*.html", "/favicon.ico", "/**/*.html", "/**/*.css", "/**/*.js");
	}
	
}
