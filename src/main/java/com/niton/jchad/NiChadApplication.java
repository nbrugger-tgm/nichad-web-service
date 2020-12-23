package com.niton.jchad;

import com.niton.jchad.security.RequestLimiter;
import com.niton.jchad.security.SessionHandler;
import com.niton.util.Logging;
import com.niton.util.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class NiChadApplication implements WebMvcConfigurer {

	public static void main(String[] args) {
		Config.init("config.cfg");
		Logging.init("web-service", "logging");
		SpringApplication.run(NiChadApplication.class, args);
	}

	@Autowired
	private SessionHandler sessionHandler;
	@Autowired
	private RequestLimiter limiter;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(sessionHandler).order(1);
		registry.addInterceptor(limiter).order(0);
	}
}
