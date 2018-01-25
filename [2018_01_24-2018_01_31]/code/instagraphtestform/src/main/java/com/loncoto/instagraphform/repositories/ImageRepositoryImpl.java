package com.loncoto.instagraphform.repositories;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

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

	@PersistenceContext
	private EntityManager em;
	
	/*
	 * 
	 * Methode de recherche
	 * 
	 */
	
	@Override
	@Transactional(readOnly=true)
	public Page<Image> searchWithTags(List<Integer> includedTags,
									  List<Integer> excludeTags,
									  Pageable pageRequest) {
		StringBuilder sb = new StringBuilder("from Image as img");
		if (!includedTags.isEmpty()) {
			StringBuilder sbJoin = new StringBuilder();
			StringBuilder sbWhere = new StringBuilder(" WHERE ");
			for (int position = 1; position <= includedTags.size(); position++) {
				sbJoin.append(", IN(img.tags) ta" + position); // ",IN(img.tags) ta1 ..."
				//sbJoin.append(" JOIN img.tags as ta" + position); // ",IN(img.tags) ta1 ..."
				if (position > 1)
					sbWhere.append(" AND ");
				sbWhere.append("ta"+ position).append(".id=:tincid" + position);
			}
			sb.append(sbJoin);
			sb.append(sbWhere);
		}
		log.info("requette générée : " + sb.toString());
		// creation de la requette
		TypedQuery<Image> query = em.createQuery("select img " + sb.toString(), Image.class);
		TypedQuery<Long> countQuery = em.createQuery("select count(img) " + sb.toString(), Long.class);
		// passage des parametres à la requette
		for (int position = 1; position <= includedTags.size(); position++ ) {
			query.setParameter("tincid" + position, includedTags.get(position - 1));
			countQuery.setParameter("tincid" + position, includedTags.get(position - 1));
		}
		
		// pagination de la requette
		query.setFirstResult(pageRequest.getOffset()); // postion de demarrage de la requette 
		query.setMaxResults(pageRequest.getPageSize());  // combien d'image renvoyer
		
		// retourne la page
		return new PageImpl<>(query.getResultList(),pageRequest, countQuery.getSingleResult());
	}
	
	
	
	

}
