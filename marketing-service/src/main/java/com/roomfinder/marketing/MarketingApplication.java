package com.roomfinder.marketing;

import com.roomfinder.marketing.configuration.firebase.FirebaseProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@EnableFeignClients
@EnableMongoAuditing
@EnableConfigurationProperties(FirebaseProperties.class)
public class MarketingApplication {
	public static void main(String[] args) {
		SpringApplication.run(MarketingApplication.class, args);
	}


}
