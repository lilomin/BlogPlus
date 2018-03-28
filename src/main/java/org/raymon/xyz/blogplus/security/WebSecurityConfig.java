package org.raymon.xyz.blogplus.security;

import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Created by lilm on 18-3-25.
 */
public class WebSecurityConfig implements WebMvcConfigurer {
	
	public static final String SESSION_KEY = "user";
	
	@Bean
	public SecurityInterceptor getSecurityInterceptor() {
		return new SecurityInterceptor();
	}
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		InterceptorRegistration interceptor = registry.addInterceptor(getSecurityInterceptor());
		
		interceptor.addPathPatterns("/management");
		interceptor.addPathPatterns("/management/**");
		
		interceptor.addPathPatterns("/api");
		
		interceptor.excludePathPatterns("/user/register");
		interceptor.excludePathPatterns("/user/login");
	}
	
}
