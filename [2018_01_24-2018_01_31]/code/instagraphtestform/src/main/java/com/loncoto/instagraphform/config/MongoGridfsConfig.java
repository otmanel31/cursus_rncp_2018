package com.loncoto.instagraphform.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;

@Configuration
public class MongoGridfsConfig extends AbstractMongoConfiguration {

	@Value("${stockage.mongo.address}")
	private String mongoAdress;
	
	@Value("${stockage.mongo.database}")
	private String mongoDatabase;
	
	@Override
	protected String getDatabaseName() {
		return mongoDatabase;
	}

	// cette méthode retourne la connection au serveur mongo
	@Override
	public Mongo mongo() throws Exception {
		return new MongoClient(mongoAdress);
	}

	@Bean
	public GridFsTemplate gridFsTemplate() throws Exception {
		return new GridFsTemplate(mongoDbFactory(), mappingMongoConverter());
	}
	
}
