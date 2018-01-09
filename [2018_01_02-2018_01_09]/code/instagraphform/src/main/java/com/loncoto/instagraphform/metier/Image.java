package com.loncoto.instagraphform.metier;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
@Entity
public class Image extends Content {
											private String fileName;
	@Column(length=100) 					private String contentType;
											private long fileSize;
											private int width;
											private int height;
	@Column(length=60) 						private String filehash;
	@Column(length=60) 						private String storageId;
	

	public Image(long id,
				String name,
				String description,
				LocalDateTime dateAdded,
				String fileName,
				String contentType,
				long fileSize,
				int width,
				int height,
				String filehash,
				String storageId) {
		super(id, name, description, dateAdded);
		this.fileName = fileName;
		this.contentType = contentType;
		this.fileSize = fileSize;
		this.width = width;
		this.height = height;
		this.filehash = filehash;
		this.storageId = storageId;
	}
	
}
