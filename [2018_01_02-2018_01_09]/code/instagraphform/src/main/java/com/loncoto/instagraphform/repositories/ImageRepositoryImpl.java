package com.loncoto.instagraphform.repositories;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.loncoto.instagraphform.metier.Image;
import com.loncoto.instagraphform.util.FileStorageManager;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.resizers.configurations.Antialiasing;
import net.coobird.thumbnailator.resizers.configurations.ScalingMode;

public class ImageRepositoryImpl implements ImageRepositoryCustom {

	private static Logger log = LogManager.getLogger(ImageRepositoryImpl.class);
	
	public static final int THUMB_WIDTH = 164;
	public static final int THUMB_HEIGHT = 164;
	
	@Autowired
	private FileStorageManager fileStorageManager;
	
	@Override
	public boolean saveImageFile(Image img, InputStream f) {
		//-------------------------------------
		// sauvegarde image originale
		//----------------------------------
		String storageId = fileStorageManager.saveNewFile("images", f);
		img.setStorageId(storageId);
		//----------------------------------
		// generation et sauvegarde miniature
		//----------------------------------
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			Thumbnails.of(fileStorageManager.getImageFile(storageId).get())
						.size(THUMB_WIDTH, THUMB_HEIGHT)
						.scalingMode(ScalingMode.BICUBIC)
						.antialiasing(Antialiasing.ON)
						.outputFormat("jpg")
						.toOutputStream(bos);
			// sauvegarde de la miniature en fichier
			String thumbStorageId = fileStorageManager.saveNewFile("imagesThumb",
					new ByteArrayInputStream(bos.toByteArray()));
			// mise a jour objet image avec reference thumbnail
			img.setThumbStorageId(thumbStorageId);
		} catch (IOException | ArrayIndexOutOfBoundsException e) {
			log.error("erreur a la generation miniature " + e);
		}
		return true;
	}

	@Override
	public Optional<File> getImageFile(String storageId) {
		return fileStorageManager.getImageFile(storageId);
	}

	@Override
	public boolean deleteImageFile(Image image) {
		if (image == null)
			return false;
		boolean successA = fileStorageManager.deleteImageFile(image.getStorageId());
		boolean successB = fileStorageManager.deleteImageFile(image.getThumbStorageId());
		return successA && successB;
	}
	
	
	

}
