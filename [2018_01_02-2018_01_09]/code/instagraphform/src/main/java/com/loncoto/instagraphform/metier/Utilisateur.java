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

@Getter @Setter @NoArgsConstructor @ToString(exclude= {"roles", "password"})
@Entity
public class Utilisateur {
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)	private int id;
	@Column(length=100, nullable=false, unique=true)		private String username;
	@Column(length=80) @JsonIgnore							private String password;
															private boolean enabled;
	@ManyToMany 											private Set<Role> roles;
	
	public Set<Role> getRoles() {
		if (roles == null)
			roles = new HashSet<>();
		return roles;
	}

	public Utilisateur(int id, String username, String password, boolean enabled) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.enabled = enabled;
	}
	
	
}
