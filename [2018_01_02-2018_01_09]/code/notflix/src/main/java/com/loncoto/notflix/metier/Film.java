package com.loncoto.notflix.metier;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
@Entity
public class Film {
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY) 	private int id;
															private String titre;
															private String realisateur;
															private	int annee;
															private String synopsis;

}
