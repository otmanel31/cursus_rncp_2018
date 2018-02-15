package com.loncoto.instagraphform.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Component;

import com.mongodb.BasicDBObject;


// objet injectable dans d'autre beans spring
@Component
public class FileStorageManagerMongo {
	private static Logger log = LogManager.getLogger(FileStorageManagerMongo.class);

	@Autowired
	private GridFsTemplate gridFsTemplate;
	
	public FileStorageManagerMongo() {
		super();
		log.info("demarrage du file storage manager");
	}
	
	public String saveNewFile(String collection, InputStream data, String fileName, String contentType) {
		BasicDBObject meta = new BasicDBObject("collection", collection);
		return this.gridFsTemplate.store(data, fileName, contentType, meta)
				   .getId().toString();
	}
	

	public Optional<InputStream> getImageFileStream(String storageId) {
		Query q = new Query();
		q.addCriteria(Criteria.where("_id").is(storageId));
		return Optional.of(this.gridFsTemplate.findOne(q).getInputStream());
	}

	public Optional<File> getImageFile(String storageId) {
		return Optional.empty();
	}
	
	public boolean deleteImageFile(String storageId) {
		Query q = new Query();
		q.addCriteria(Criteria.where("_id").is(storageId));
		this.gridFsTemplate.delete(q);
		return true;
	}
	
}
