package com.loncoto.instagraphform;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.stubbing.answers.Returns;
import org.mockito.internal.stubbing.answers.ReturnsArgumentAt;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;

import com.loncoto.instagraphform.metier.Image;
import com.loncoto.instagraphform.metier.Tag;
import com.loncoto.instagraphform.metier.projections.ImageWithTags;
import com.loncoto.instagraphform.repositories.ImageRepository;
import com.loncoto.instagraphform.repositories.UtilisateurRepository;
import com.loncoto.instagraphform.web.ImageController;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.hasSize; // test sur du contenu json
import static org.hamcrest.Matchers.equalTo; // test sur du contenu json
import static org.hamcrest.Matchers.closeTo; // test sur du contenu json

@RunWith(SpringJUnit4ClassRunner.class)
//@WebMvcTest(controllers=ImageController.class)
@EnableSpringDataWebSupport
public class InstagraphImageTest {
	
//	@MockBean
//	private ImageRepository imageRepository;
	@MockBean
	private UtilisateurRepository utilisateurRepository;
	
	@MockBean
	private ProjectionFactory projectionFactory;
	
	
	private ProjectionFactory realProjectionFactory;
	
	// cela contiendra/encapsulera notre controller à tester
//	@Autowired
//	private MockMvc mockMvc;
	
	//-----------------------------------------------
	
	@Test
	public void testBidon() throws Exception {
		assertTrue(true);
	}
	
//	private Page<Image> getSampleImagePage(Pageable page, int totalCount, boolean withTags) {
//		/*
//		 * si je demande la page no 3 avec des pages de taille 10
//		 * offest me reverra 30 -> offest du premier element à renvoyer
//		 */
//	
//		int start = page.getOffset();
//		List<Image> content = new ArrayList<>();
//		LocalDateTime ldt = LocalDateTime.of(2018, 1, 10, 10, 30, 0);
//		for (int i = 0; i < page.getPageSize(); i++) {
//			Image img = new Image(start + i + 1,
//					"image no " + (start + i),
//					"une image",
//					ldt,
//					"fileimage" + i + ".jpg",
//					MediaType.IMAGE_JPEG_VALUE,
//					20*1024*1024,
//					1000, 600,
//					"1354654354354354",
//					"879789879879879" + i,
//					"132132132132132" + i);
//			if (withTags)
//				img.getTags().add(new Tag(1, "tagtest", "un tag de test"));
//				
//			content.add(img);
//			ldt = ldt.plusDays(1);
//		}
//		return new PageImpl<>(content, page, totalCount);
//	}
//	
//	//-----------------------------------------------
//	@Test
//	public void testListeImage() throws Exception {
//
//		Pageable pr = new PageRequest(3, 15);
//		when(imageRepository.findAll(pr))
//							.thenReturn(getSampleImagePage(pr, 100, true));
//		
//		mockMvc.perform(get("/extendedapi/image/findbytag")
//				 .param("page", "3")
//				 .param("size", "15"))
//				.andExpect(status().isOk())
//				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
//				.andExpect(jsonPath("$.numberOfElements", equalTo(15)))
//				.andExpect(jsonPath("$.content[0].id", equalTo(46)))
//				.andExpect(jsonPath("$.content", hasSize(15)));
//				/*.andExpect(jsonPath("$.content[0].tags[0].libelle", equalTo("tagtest")));*/
//		
//		verify(imageRepository, atLeastOnce()).findAll(pr);
//		
//	}
//	
//	//-----------------------------------------------
//	@Test
//	public void testListeImageFull() throws Exception {
//		
//		// creer notre projection factory
//		realProjectionFactory = new SpelAwareProxyProjectionFactory();
//		
//		Pageable pr = new PageRequest(3, 15);
//		when(imageRepository.findAll(pr))
//							.thenReturn(getSampleImagePage(pr, 100, true));
//		// quand le controller appelera la "fausse" projection factory
//		// nous rapellerons derriere notre propre projection factory
//		when(projectionFactory.createProjection(any(), any()))
//			.thenAnswer(new Answer<Object>() {
//				@Override
//				public Object answer(InvocationOnMock invocation) throws Throwable {
//					return realProjectionFactory.createProjection(
//							(Class)invocation.getArguments()[0],
//							invocation.getArguments()[1]);
//				}
//			});
//		
//		mockMvc.perform(get("/extendedapi/image/findbytagfull")
//				 .param("page", "3")
//				 .param("size", "15"))
//				.andExpect(status().isOk())
//				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
//				.andExpect(jsonPath("$.numberOfElements", equalTo(15)))
//				.andExpect(jsonPath("$.content[0].id", equalTo(46)))
//				.andExpect(jsonPath("$.content", hasSize(15)))
//				.andExpect(jsonPath("$.content[0].tags[0].libelle", equalTo("tagtest")));
//		
//		verify(imageRepository, atLeastOnce()).findAll(pr);
//		
//	}
//	
//	
//	@Test
//	public void testUploadImage() throws Exception {
//		Image img = new Image(1, "hellcat.jpg",
//				"",
//				LocalDateTime.now(),
//				"hellcat.jpg",
//				MediaType.IMAGE_JPEG_VALUE,
//				1 * 1024 * 1024,
//				1000,
//				600,
//				"365436543654354",
//				"54654654654654",
//				"657654654654654");
//		
//		when(imageRepository.saveImageFile(any(Image.class), any(InputStream.class)))
//							.thenReturn(true);
//		when(imageRepository.save(any(Image.class)))
//							.thenReturn(img);
//		
//		byte[] data = new byte[1024*1024];
//		Random rd = new Random();
//		for (int i = 0; i < data.length; i++) {
//			data[i] = (byte)rd.nextInt(127);
//		}
//		MockMultipartFile file = new MockMultipartFile(
//				"file",
//				img.getFileName(),
//				img.getContentType(),
//				data);
//		
//		mockMvc.perform(fileUpload("/extendedapi/image/upload")
//				 .file(file))
//				.andExpect(status().isOk())
//				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
//				.andExpect(jsonPath("$.id", equalTo(1)))
//				.andExpect(jsonPath("$.name", equalTo("hellcat.jpg")));
//		
//		verify(imageRepository, atLeastOnce()).saveImageFile(any(Image.class), any(InputStream.class));
//		verify(imageRepository, atLeastOnce()).save(any(Image.class));
//		
//	}
//
//	
//	
//	@Test
//	public void testUploadImageContentTypeKO() throws Exception {
//		Image img = new Image(1, "hellcat.gif",
//				"",
//				LocalDateTime.now(),
//				"hellcat.gif",
//				MediaType.IMAGE_GIF_VALUE,
//				1 * 1024 * 1024,
//				1000,
//				600,
//				"365436543654354",
//				"54654654654654",
//				"657654654654654");
//		
//		
//		byte[] data = new byte[1024*1024];
//		Random rd = new Random();
//		for (int i = 0; i < data.length; i++) {
//			data[i] = (byte)rd.nextInt(127);
//		}
//		MockMultipartFile file = new MockMultipartFile(
//				"file",
//				img.getFileName(),
//				img.getContentType(),
//				data);
//		
//		mockMvc.perform(fileUpload("/extendedapi/image/upload")
//				 .file(file))
//				.andExpect(status().isUnsupportedMediaType())
//				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
//				.andExpect(jsonPath("$.errorfield", equalTo("contentType")))
//				.andExpect(jsonPath("$.errormessage", equalTo("only jpeg or png supported")));
//		
//		verify(imageRepository, never()).saveImageFile(any(Image.class), any(InputStream.class));
//		verify(imageRepository, never()).save(any(Image.class));
//		
//	}
//	
//	@Ignore
//	@Test
//	public void testUploadImage2() throws Exception {
//		
//		// récupération chemin fichier
//		ClassLoader classLoader = getClass().getClassLoader();
//		File fimg = new File(classLoader.getResource("imagetest/chaton.jpg").getPath());
//		
//		
//		Image img = new Image(1, fimg.getName(),
//				"",
//				LocalDateTime.now(),
//				fimg.getName(),
//				MediaType.IMAGE_JPEG_VALUE,
//				fimg.length(),
//				1000,
//				600,
//				"365436543654354",
//				"54654654654654",
//				"657654654654654");
//		
//		when(imageRepository.saveImageFile(any(Image.class), any(InputStream.class)))
//							.thenReturn(true);
//		when(imageRepository.save(any(Image.class)))
//							.thenReturn(img);
//		
//		
//		MockMultipartFile file = new MockMultipartFile(
//				"file",
//				img.getFileName(),
//				img.getContentType(),
//				new FileInputStream(fimg));
//		
//		mockMvc.perform(fileUpload("/extendedapi/image/upload")
//				 .file(file))
//				.andExpect(status().isOk())
//				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
//				.andExpect(jsonPath("$.id", equalTo(1)))
//				.andExpect(jsonPath("$.name", equalTo(img.getFileName())));
//		
//		verify(imageRepository, atLeastOnce()).saveImageFile(any(Image.class), any(InputStream.class));
//		verify(imageRepository, atLeastOnce()).save(any(Image.class));
//		
//	}

	
}
