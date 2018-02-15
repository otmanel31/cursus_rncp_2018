package com.loncoto.stockageGridFs.worker;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;

import com.mongodb.gridfs.GridFSDBFile;

public class FileMongoDownloader implements Runnable {

private String storageRep;
	
	private GridFsTemplate gridFsTemplate;
	
	public FileMongoDownloader(String storageRep, GridFsTemplate gridFsTemplate) {
		this.storageRep = storageRep;
		this.gridFsTemplate = gridFsTemplate;
	}

	
	@Override
	public void run() {
		File rep = new File(this.storageRep);
		if (rep.exists() && rep.isDirectory()) {
			Query q = new Query();
			//q.addCriteria(Criteria.where("_id").is(("")));
			//this.gridFsTemplate.find(new ObjectId("5434354354"));
			List<GridFSDBFile> fichiers =  this.gridFsTemplate.find(q);
			for (GridFSDBFile fichier : fichiers) {
				System.out.println("téléchargement fichier " + fichier.getFilename() 
									+ " d'id " + fichier.getId());
				try {
					// copier le fichier dans notre répertoire
					fichier.writeTo(Paths.get(rep.getAbsolutePath(), fichier.getFilename())
										 .toFile());
				} catch (IOException e) {e.printStackTrace();}
				
			}
			
		}

	}

}
