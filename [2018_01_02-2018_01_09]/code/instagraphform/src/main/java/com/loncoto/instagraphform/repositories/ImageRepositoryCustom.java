package com.loncoto.instagraphform.repositories;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.loncoto.instagraphform.metier.Image;

public interface ImageRepositoryCustom {
	
	// sauvegarde du fichier uniquement
	boolean saveImageFile(Image img, InputStream f);
	Optional<File> getImageFile(String storageId);
	boolean deleteImageFile(Image image);
	
	//recherche d'image par tags
	Page<Image> searchWithTags(List<Integer> includedTags,
								List<Integer> excludeTags,
								Pageable pageRequest);
	
}
