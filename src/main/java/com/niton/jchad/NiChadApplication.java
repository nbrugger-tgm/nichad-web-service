package com.niton.jchad;

import com.niton.jchad.security.RequestLimiter;
import com.niton.jchad.security.SessionHandler;
import com.niton.util.Logging;
import com.niton.util.config.Config;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class NiChadApplication implements WebMvcConfigurer {

	public static final String USER_SESSION = "user_session";
	@Autowired
	private SessionHandler sessionHandler;

	public static void main(String[] args) {
		Config.init("config.cfg");
		Logging.init("web-service", "logging");
		SpringApplication.run(NiChadApplication.class, args);
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(sessionHandler)
		        .addPathPatterns("/users/**")
		        .addPathPatterns("/chats/**")
		        .order(3);
		registry.addInterceptor(new RequestLimiter(50000,0,30,10000))
		        .addPathPatterns("/**")
		        .order(0);
		registry.addInterceptor(new RequestLimiter(10000, 500,20,60*1000))
		        .addPathPatterns("/chats/*/messages/*")
		        .order(1);
		registry.addInterceptor(new RequestLimiter(100,1000,5,60*1000))
		        .addPathPatterns("/users/*/")
		        .order(2);
	}

	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI()
				.components(new Components()
						            .addSecuritySchemes(USER_SESSION,
						                                new SecurityScheme().type(SecurityScheme.Type.APIKEY)
						                                                    .name("X-SESSION")
						                                                    .scheme("string")
						                                                    .in(
								                                                    SecurityScheme.In.HEADER)));
	}

	@Configuration
	public class WebConfiguration implements WebMvcConfigurer {

		@Override
		public void addCorsMappings(CorsRegistry registry) {
			registry.addMapping("/**").allowedMethods("*").allowedOrigins("*");
		}
	}
}
