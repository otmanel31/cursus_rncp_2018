package com.loncoto.instagraphform.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import javax.servlet.http.HttpSession;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;

import com.loncoto.instagraphform.metier.CompositeOwnerKey;
import com.loncoto.instagraphform.metier.Image;
import com.loncoto.instagraphform.metier.Owner;
import com.loncoto.instagraphform.metier.Utilisateur;
import com.loncoto.instagraphform.metier.projections.ImageWithTags;
import com.loncoto.instagraphform.repositories.ImageRepository;
import com.loncoto.instagraphform.repositories.OwnerRepository;
import com.loncoto.instagraphform.repositories.UtilisateurRepository;
import com.loncoto.instagraphform.util.FileStorageManager;

@Controller
@RequestMapping("/extendedapi/image")
public class ImageController {
	
	private static Logger log = LogManager.getLogger(ImageController.class);

	private final ProjectionFactory projectionFactory;
	
	// injection de la ProjectionFactory via notre constructeur
	@Autowired
	public ImageController(ProjectionFactory projectionFactory) {
		this.projectionFactory = projectionFactory;
	}
	
	@Autowired
	private ImageRepository imageRepository;
	@Autowired
	private UtilisateurRepository utilisateurRepository;
	@Autowired
	private OwnerRepository ownerRepository;
	
	
	
	
	@CrossOrigin(origins="http://localhost:4200")
	@RequestMapping(value="/findbytag",
					method=RequestMethod.GET,
					produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Page<Image> findByTags(
			@RequestParam("tagsId") Optional<List<Integer>> tagsId,
			@PageableDefault(page=0, size=12) Pageable page) {
		if (tagsId.isPresent())
			log.info("tagsId = " + tagsId.get().toString());
		else
			log.info("pas de tags en parametre");
		return imageRepository.findAll(page);
	}
	
	@CrossOrigin(origins="http://localhost:4200")
	@RequestMapping(value="/findbytagfull",
					method=RequestMethod.GET,
					produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@PreAuthorize("hasRole('ROLE_USER')")
	public Page<ImageWithTags> findByTagsFull(
			@RequestParam("tagsId") Optional<List<Integer>> tagsId,
			@PageableDefault(page=0, size=12) Pageable page) {
		if (tagsId.isPresent()) {
			log.info("tagsId = " + tagsId.get().toString());
			return imageRepository
						.searchWithTags(tagsId.get(), null, page)
						.map(img -> projectionFactory.createProjection(ImageWithTags.class, img));
		}
		else {
			log.info("pas de tags en parametre");
			return imageRepository
						.findAll(page)
						.map(img -> projectionFactory.createProjection(ImageWithTags.class, img));
		}
	}
	
	
	
	@CrossOrigin(origins="http://localhost:4200")
	@RequestMapping(value="/upload",
					method=RequestMethod.POST,
					produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@PreAuthorize("hasRole('ROLE_USER')")
	public Image upload(@RequestParam("file") MultipartFile file,
						@AuthenticationPrincipal Authentication authentication) {
		log.info("current user: " + authentication.getName());
		log.info("file name  :" + file.getOriginalFilename());
		log.info("content type  :" + file.getContentType());
		try {
			Image img = new Image(0, 
				 file.getOriginalFilename(),
				 "",
				 LocalDateTime.now(),
				 file.getOriginalFilename(),
				 file.getContentType(),
				 file.getSize(),							  // taille du fichier
				 0,
				 0,
				 DigestUtils.sha1Hex(file.getInputStream()),  // somme de controle du fichier
				 "",
				 "");
		
			imageRepository.saveImageFile(img, file.getInputStream());
			// le fichier est sauvegardé et img contient le storageId correspondant
			imageRepository.save(img);
			// ligne insérée dans la BDD
			
			Utilisateur u = utilisateurRepository.findByUsername(authentication.getName());
			// j'asssocie l'utilisateur comme propriétaire de l'image
			Owner o = new Owner(new CompositeOwnerKey(img, u));
			ownerRepository.save(o);
			//ownerRepository.findOne(arg0)
			
			return img;
		} catch (IOException e) {
			throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR,
												"erreur a la sauvegarde");
		}
	}
	
	@RequestMapping(value="/download/{id:[0-9]+}", method=RequestMethod.GET)
	@ResponseBody
	@PreAuthorize("permitAll")
	public ResponseEntity<FileSystemResource> imageData(@PathVariable("id") long id) {
		Image img = imageRepository.findOne(id);
		if (img == null)
			throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "image inconnue");
		// on recupere le fichier correspondant
		Optional<File> fichier = imageRepository.getImageFile(img.getStorageId());
		if (!fichier.isPresent())
			throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "fichier image introuvable");
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.parseMediaType(img.getContentType()));
		headers.setContentLength(img.getFileSize());
		headers.setContentDispositionFormData("attachment", img.getFileName());
		ResponseEntity<FileSystemResource> re =
				new ResponseEntity<FileSystemResource>(new FileSystemResource(fichier.get()),
													headers,
													HttpStatus.ACCEPTED);
		return re;
	}
	
	@RequestMapping(value="/downloadthumb/{id:[0-9]+}", method=RequestMethod.GET)
	@ResponseBody
	@PreAuthorize("permitAll")
	public ResponseEntity<FileSystemResource> imageDataThumb(@PathVariable("id") long id) {
		Image img = imageRepository.findOne(id);
		if (img == null)
			throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "image inconnue");
		// on recupere le fichier correspondant
		Optional<File> fichier = imageRepository.getImageFile(img.getThumbStorageId());
		if (!fichier.isPresent())
			throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "fichier image introuvable");
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.IMAGE_JPEG);
		headers.setContentLength(fichier.get().length());
		headers.setContentDispositionFormData("attachment", img.getFileName());
		ResponseEntity<FileSystemResource> re =
				new ResponseEntity<FileSystemResource>(new FileSystemResource(fichier.get()),
													headers,
													HttpStatus.ACCEPTED);
		return re;
	}

	
	@CrossOrigin(origins="http://localhost:4200")
	@RequestMapping(value="/delete",
					method=RequestMethod.DELETE,
					produces=MediaType.APPLICATION_JSON_VALUE)	
	@ResponseBody
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
	public Map<String, Object> deleteImages(
			@RequestParam("imagesId") List<Long> imagesId,
			@AuthenticationPrincipal Authentication authentication) {
		Map<String, Object> result = new HashMap<>();
		Iterable<Image> images = imageRepository.findAll(imagesId);
		
		//----------------------------------- autorisé?
		final Utilisateur u = utilisateurRepository.findByUsername(authentication.getName());
		if (!authentication.getAuthorities()
							.stream()
							.anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")))
		{
			log.info("not role_admin, test if image owner");
			final OwnerRepository repo = this.ownerRepository;
			boolean ok = StreamSupport.stream(images.spliterator(), false)
						 			.allMatch(img -> repo.exists(new CompositeOwnerKey(img, u)));
			if (!ok) {
				throw new HttpClientErrorException(HttpStatus.FORBIDDEN, "can not delete these images");
			}
			log.info("delete ok, user is owner of image");
		}
		//-----------------------------------------
		
		// efface les images dans la base de donnée
		for (Image img : images) {
			// si l'image a un propriétaire, effacer l'association d'abord
			//Owner o = ownerRepository.findOne(new CompositeOwnerKey(img, u));
			List<Owner> os = ownerRepository.findByClef_ImageId(img.getId());
			if (!os.isEmpty())
				ownerRepository.delete(os);
			imageRepository.delete(img);
		}
		
		
		int nbImageToDelete = 0;
		int nbFilesDeleted = 0;
		// efface les fichiers images correspondants
		for (Image img : images) {
			nbImageToDelete++;
			if (imageRepository.deleteImageFile(img))
				nbFilesDeleted++;
		}
		// retrouner les informations sur ce qui a été fait
		result.put("nbImagesDeleted", nbImageToDelete);
		result.put("nbFilesDeleted", nbFilesDeleted);
		return result;
	}
	
}
