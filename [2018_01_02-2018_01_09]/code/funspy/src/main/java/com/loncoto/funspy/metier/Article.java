package com.loncoto.funspy.metier;

import java.time.LocalDate;

import javax.persistence.Column;
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
public class Article {
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)	private int id;
	@Column(length=100) 									private String nom;
															private String description;
															private double prix;
															private double poids;
															private LocalDate dateSortie;
	
}
