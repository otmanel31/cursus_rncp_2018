package com.loncoto.stockageGridFs;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;

import com.loncoto.stockageGridFs.worker.FileMongoUploader;

@SpringBootApplication
public class StockageGridfsApplication implements CommandLineRunner
{

	public static void main(String[] args) {
		SpringApplication.run(StockageGridfsApplication.class, args);
	}

	@Autowired
	private GridFsTemplate gridFsTemplate;
	
	@Override
	public void run(String... args) throws Exception {
		System.out.println("application stockage started....");
		System.out.println(Arrays.toString(args));
		if (args.length > 0) {
			FileMongoUploader fmu = new FileMongoUploader(args[0], this.gridFsTemplate);
			fmu.run();
		}
		else {
			System.out.println("no directory given as arg");
		}
		
	}
}
