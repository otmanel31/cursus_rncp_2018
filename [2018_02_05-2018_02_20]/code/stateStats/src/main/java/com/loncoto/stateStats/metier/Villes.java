package com.loncoto.stateStats.metier;

import java.util.List;

import javax.persistence.Id;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="villes")
public class Villes {
	@Id	
	private String id;
	private String city;
	private int pop;
	private String state;
	private List<Double> loc;
	
	public String getId() {return id;}
	public void setId(String id) {this.id = id;}
	public String getCity() {return city;}
	public void setCity(String city) {this.city = city;}
	public int getPop() {return pop;}
	public void setPop(int pop) {this.pop = pop;}
	public String getState() {return state;}
	public void setState(String state) {this.state = state;}
	public List<Double> getLoc() {return loc;}
	public void setLoc(List<Double> loc) {this.loc = loc;}
	
	public Villes() {}
	public Villes(String id, String city, int pop, String state, List<Double> loc) {
		super();
		this.id = id;
		this.city = city;
		this.pop = pop;
		this.state = state;
		this.loc = loc;
	}
	
	
	
}
