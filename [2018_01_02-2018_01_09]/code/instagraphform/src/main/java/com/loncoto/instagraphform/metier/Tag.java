package com.loncoto.instagraphform.metier;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @NoArgsConstructor @ToString(exclude= {"contents"})
@Entity
public class Tag {
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY) 	private int id;
	@Column(length=100) 									private String libelle;
															private String description;
	@ManyToMany(mappedBy="tags") @JsonIgnore				private Set<Content> contents;
															
	public Set<Content> getContents() {
		if (contents == null)
			contents = new HashSet<>();
		return contents;
	}
	
	
	public Tag(int id, String libelle, String description) {
		super();
		this.id = id;
		this.libelle = libelle;
		this.description = description;
	}
	
	
}
