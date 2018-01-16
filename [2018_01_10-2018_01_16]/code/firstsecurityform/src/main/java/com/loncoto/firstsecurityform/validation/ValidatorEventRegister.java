package com.loncoto.firstsecurityform.validation;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.event.ValidatingRepositoryEventListener;
import org.springframework.validation.Validator;

@Configuration
public class ValidatorEventRegister implements InitializingBean {
	private static Logger log = LogManager.getLogger(ValidatorEventRegister.class);
	// le service/objet permettant d'enregistrer un validateur
	// aupres des repository
	@Autowired
	private ValidatingRepositoryEventListener validatingRepositoryEventListener;
	
	// la liste de tous les validateurs détécté par spring
	@Autowired
	private Map<String, Validator> validators;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		log.info("initialisation des validateurs");
		List<String> events = Arrays.asList("beforeCreate");
		for (Entry<String, Validator> entry : validators.entrySet()) {
			events.stream()
				  .filter( p -> entry.getKey().startsWith(p))
				  .findFirst()
				  .ifPresent(
				v ->  {
					log.info("ajout validateur : " + v + " -> " + entry.getValue().getClass().getSimpleName());
					validatingRepositoryEventListener.addValidator(v, entry.getValue());
				}
			);
		}
		

	}

}
