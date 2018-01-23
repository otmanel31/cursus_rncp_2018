package com.loncoto.secondJunitSpring.services;

import java.util.List;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.loncoto.secondJunitSpring.metier.Gazouille;
import com.loncoto.secondJunitSpring.repositories.GazouilleDao;

@Service
public class GazouilleService {
	public static final String CENSORED = "twitter";
	private Pattern censorer;
	
	public GazouilleService() {
		censorer = Pattern.compile(CENSORED, Pattern.CASE_INSENSITIVE);
	}

	@Autowired
	private GazouilleDao gazouilleDao;
	public GazouilleDao getGazouilleDao() {return gazouilleDao;}
	public void setGazouilleDao(GazouilleDao gazouilleDao) {this.gazouilleDao = gazouilleDao;}
	

	public Gazouille publish(Gazouille gazouille) {
		// remplacer toute occurence de "twitter" par "gazouille"
		gazouille.setTitre(censorer.matcher(gazouille.getTitre()).replaceAll("gazouille"));
		gazouille.setCorps(censorer.matcher(gazouille.getCorps()).replaceAll("gazouille"));
		// sauvegarder
		gazouilleDao.save(gazouille);
		return gazouille;
	}
	
	public Gazouille readGazouille(int id) {
		Gazouille g = gazouilleDao.findById(id);
		if (g == null) {
			throw new GazouilleNotFoundException("gazouille introuvable");
		}
		return g;
	}
	
	public List<Gazouille> readAllGazouille() {
		return gazouilleDao.findAll();
	}
	
	
	public static class GazouilleNotFoundException extends RuntimeException {
		public GazouilleNotFoundException(String message) {
			super(message);
		}
	}
}
