package com.loncoto.instagraphform.repositories;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.loncoto.instagraphform.metier.Image;
import com.loncoto.instagraphform.util.FileStorageManager;

public class ImageRepositoryImpl implements ImageRepositoryCustom {

	@Autowired
	private FileStorageManager fileStorageManager;
	
	@Override
	public boolean saveImageFile(Image img, InputStream f) {
		String storageId = fileStorageManager.saveNewFile("images", f);
		img.setStorageId(storageId);

		return true;
	}

	@Override
	public Optional<File> getImageFile(String storageId) {
		return fileStorageManager.getImageFile(storageId);
	}
	
	

}
