package com.loncoto.planificatorform.metier;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @NoArgsConstructor @ToString(exclude= {"interventions"})
@Entity
public class Intervenant {
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY) 	private int id;
															private String nom;
															private String email;
															
	@OneToMany(mappedBy="intervenant")						private Set<Intervention> interventions;
    
	public Set<Intervention> getInterventions() {
		if (interventions == null) interventions = new HashSet<>();
		return interventions;
	}

	public Intervenant(int id, String nom, String email) {
		super();
		this.id = id;
		this.nom = nom;
		this.email = email;
	}
	
	
}
