package com.loncoto.firstsecurityform.validation;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.loncoto.firstsecurityform.metier.Utilisateur;

// un validateur d'objet Utilisateur
// le nom de la classe est IMPORTANT
// de même, le "beforeSave" est important
// ici beforeSave --> avant sauvegarde en base
// Utilisateur --> pour les entité Utilisateur
@Component(value="beforeCreateUtilisateurValidator")
public class UtilisateurValidator implements Validator {

	private static Logger log = LogManager.getLogger(UtilisateurValidator.class);
			
	// cette méthode sera appelée par spring pour vérifier si ce validateur
	// s'applique bien a l'entité à valider
	@Override
	public boolean supports(Class<?> clazz) {
		// je ne valide que les entité de classes Utilisateur
		return Utilisateur.class.equals(clazz);
	}

	// target -> l'entité à verifier valider (ici notre Utilisateur)
	// errors -> object contenant la liste des erreurs de validation, à destination
	// du framework de validation
	@Override
	public void validate(Object target, Errors errors) {
		log.info("validation objet " + target);
		Utilisateur u = (Utilisateur)target;
		String name = u.getUsername();
		if (name == null || name.length() < 3 || name.length() > 100) {
			errors.rejectValue("username", "name is empty or too short or too long");
		}
	}

}
