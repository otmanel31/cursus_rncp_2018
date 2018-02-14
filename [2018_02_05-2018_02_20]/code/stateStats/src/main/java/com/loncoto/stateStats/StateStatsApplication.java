package com.loncoto.stateStats;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class StateStatsApplication {

	public static void main(String[] args) {
		SpringApplication.run(StateStatsApplication.class, args);
	}
}
