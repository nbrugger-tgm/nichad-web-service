package com.niton.jchad;

import com.niton.util.Logging;
import com.niton.util.config.Config;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.ObjectInputFilter;

@SpringBootApplication
public class NiChadApplication {

	public static void main(String[] args) {
		Config.init("config.cfg");
		Logging.init("web-service", "logging");
		SpringApplication.run(NiChadApplication.class, args);
	}

}
