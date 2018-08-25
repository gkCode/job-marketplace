package com.org.marketplace;

import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

@SpringBootApplication
@EntityScan(basePackageClasses = { JobApplication.class, Jsr310JpaConverters.class })
/**
 * @author gauravkahadane
 *
 */
public class JobApplication implements ApplicationListener<ApplicationReadyEvent> {

	private static final Logger LOGGER = LoggerFactory.getLogger(JobApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(JobApplication.class, args);
	}
	
	@PostConstruct
	void init() {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
	}

	@Override
	public void onApplicationEvent(ApplicationReadyEvent arg0) {
		LOGGER.info("\n\n\n\n\t\t\t #### Started Job Marketplace Application ####\n\n");
	}
}
