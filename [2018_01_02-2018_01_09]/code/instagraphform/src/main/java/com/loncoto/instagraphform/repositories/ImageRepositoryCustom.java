package com.loncoto.instagraphform.repositories;

import java.io.File;
import java.io.InputStream;
import java.util.Optional;

import com.loncoto.instagraphform.metier.Image;

public interface ImageRepositoryCustom {
	
	// sauvegarde du fichier uniquement
	boolean saveImageFile(Image img, InputStream f);
	Optional<File> getImageFile(String storageId);
	
}
