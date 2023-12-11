package com.ace.job.recruitment.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.ErrorPageRegistrar;
import org.springframework.boot.web.server.ErrorPageRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.ace.job.recruitment.service.AppUserDetailsService;

@Configuration
public class WebSecurityConfig {

	@Autowired
	private AppUserDetailsService appUserDetailsService;

	@Bean
	public UserDetailsService userDetailsService() {
		return new AppUserDetailsService();
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SessionRegistry sessionRegistry() {
		return new SessionRegistryImpl();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();

		provider.setUserDetailsService(userDetailsService());
		provider.setPasswordEncoder(passwordEncoder());

		return provider;
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
		return authConfig.getAuthenticationManager();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf().disable().authorizeRequests(authz -> authz.antMatchers("/validate-email").permitAll()
				.antMatchers("/javascripts/candidate_vacancy_info.js").permitAll()
				.antMatchers("/connection-timeout-exception").permitAll().antMatchers("/send-reset-mail").permitAll()
				.antMatchers("/resend-reset-mail").permitAll().antMatchers("/validate-reset-token").permitAll()
				.antMatchers("/reset-password").permitAll().antMatchers("/javascripts/forgot-password.js").permitAll()
				.antMatchers("/", "/candidate/**", "/404", "/403", "/error-page").permitAll()
				.antMatchers("/hr/", "/profile/").hasAnyAuthority("Junior-HR", "Senior-HR", "Admin", "Default-Admin")
				.antMatchers("/department/").hasAnyAuthority("Department-head", "Interviewer")
				.antMatchers("/department-head/", "/profile/")
				.hasAnyAuthority("Department-head", "Admin", "Default-Admin").antMatchers("/admin/", "/profile/")
				.hasAnyAuthority("Admin", "Default-Admin").antMatchers("/interviewer/", "/profile/")
				.hasAnyAuthority("Interviewer", "Admin", "Department-head", "Default-Admin")
				.antMatchers("/senior-hr/**").hasAnyAuthority("Senior-HR", "Admin", "Default-Admin").anyRequest()
				.authenticated())
				.formLogin(form -> form.loginPage("/signin").loginProcessingUrl("/login").defaultSuccessUrl("/")
						.successHandler(myCustomAuthenticationSuccessHandler()).permitAll())
				.exceptionHandling(
						exception -> exception.accessDeniedHandler((request, response, accessDeniedException) -> {
							response.sendRedirect("/403");
						}).authenticationEntryPoint((request, response, authException) -> {
							if (response.getStatus() == HttpStatus.NOT_FOUND.value()) {
								response.sendRedirect("/404");
							} else {
								response.sendRedirect("/error-page");
							}
						}))
				.sessionManagement(
						sessionManagement -> sessionManagement.maximumSessions(-1).sessionRegistry(sessionRegistry()))
				.logout(logout -> logout.logoutRequestMatcher(new AntPathRequestMatcher("/logout")).permitAll())
				.rememberMe().key("1122121").rememberMeParameter("remember-me").rememberMeCookieName("remember-me")
				.userDetailsService(appUserDetailsService).tokenValiditySeconds(30 * 24 * 60 * 60);
		return http.build();
	}

	@Bean
	public AuthenticationSuccessHandler myCustomAuthenticationSuccessHandler() {
		return new CustomAuthenticationSuccessHandler();
	}

	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return web -> web.ignoring().antMatchers("/static/**", "/css/**", "/js/**", "/scss/**", "/fonts/**",
				"/images/**", "/vendors/**", "javascripts/**");

	}

	@Bean
	public ErrorPageRegistrar errorPageRegistrar() {
		return new CustomErrorPageRegistrar();
	}

	private static class CustomErrorPageRegistrar implements ErrorPageRegistrar {
		@Override
		public void registerErrorPages(ErrorPageRegistry registry) {
			ErrorPage error404Page = new ErrorPage(HttpStatus.NOT_FOUND, "/404");
			registry.addErrorPages(error404Page);
		}
	}
}