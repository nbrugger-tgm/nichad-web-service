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
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class NiChadApplication implements WebMvcConfigurer {

	public static final String USER_SESSION = "user_session";
	@Autowired
	private SessionHandler sessionHandler;
	@Autowired
	private RequestLimiter limiter;

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
		        .order(1);
		registry.addInterceptor(limiter)
		        .addPathPatterns("/users/**")
		        .addPathPatterns("/chats/**")
		        .order(0);
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

}
