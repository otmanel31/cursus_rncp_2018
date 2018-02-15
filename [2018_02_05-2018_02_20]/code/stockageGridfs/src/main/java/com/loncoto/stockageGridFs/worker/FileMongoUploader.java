package com.loncoto.stockageGridFs.worker;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;

public class FileMongoUploader implements Runnable {

	private String storageRep;
	
	private GridFsTemplate gridFsTemplate;
	
	public FileMongoUploader(String storageRep, GridFsTemplate gridFsTemplate) {
		this.storageRep = storageRep;
		this.gridFsTemplate = gridFsTemplate;
	}

	@Override
	public void run() {
		File f = new File(storageRep);
		if (f.exists() && f.isDirectory()) {
			File[] files = f.listFiles();
			
			for (File fichier : files) {
				if (fichier.isFile() && fichier.length() < 50 * 1024 * 1024) {
					FileInputStream fis;
					try {
						fis = new FileInputStream(fichier);
						String storageId = this.gridFsTemplate.store(fis, fichier.getName())
											   .getId().toString();
						System.out.println("upload de " + fichier.getName() + " id= " + storageId);
						fis.close();
					} catch (FileNotFoundException e) {e.printStackTrace();	}
					catch (IOException e) {e.printStackTrace();}
				}
			}
		}
		
	}
	
	
	
}
