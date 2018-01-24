package com.loncoto.planificatorform.metier;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @NoArgsConstructor @ToString(exclude= {"intervenant"})
@Entity
public class Intervention {
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY) 	private int id;
															private String lieu;
															private LocalDateTime debut;
															private LocalDateTime fin;
															private int noMateriel;
															
	@ManyToOne												private Intervenant intervenant;


	public Intervention(int id, String lieu, LocalDateTime debut, LocalDateTime fin, int noMateriel) {
		super();
		this.id = id;
		this.lieu = lieu;
		this.debut = debut;
		this.fin = fin;
		this.noMateriel = noMateriel;
	}

}
