package com.loncoto.stockageGridFs;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;

import com.loncoto.stockageGridFs.worker.FileMongoDownloader;
import com.loncoto.stockageGridFs.worker.FileMongoUploader;

@SpringBootApplication
public class StockageGridfsApplication implements CommandLineRunner
{

	public static void main(String[] args) {
		SpringApplication.run(StockageGridfsApplication.class, args);
	}

	@Autowired
	private GridFsTemplate gridFsTemplate;
	
	// le commande
	// java -jar programme.jar upload reptoUpload
	// java -jar programme.jar download reptoDownload
	@Override
	public void run(String... args) throws Exception {
		System.out.println("application stockage started....");
		System.out.println(Arrays.toString(args));
		if (args.length > 1 && args[0].equalsIgnoreCase("upload")) {
			FileMongoUploader fmu = new FileMongoUploader(args[1], this.gridFsTemplate);
			fmu.run();
		}
		else if (args.length > 1 && args[0].equalsIgnoreCase("download")) {
			FileMongoDownloader fmd = new FileMongoDownloader(args[1], this.gridFsTemplate);
			fmd.run();
		}
		else {
			System.out.println("no directory or command given as arg");
		}
		
	}
}
