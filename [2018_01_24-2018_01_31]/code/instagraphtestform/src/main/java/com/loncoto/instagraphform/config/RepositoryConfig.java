package com.loncoto.instagraphform.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;

import com.loncoto.instagraphform.metier.Content;
import com.loncoto.instagraphform.metier.Image;
import com.loncoto.instagraphform.metier.Tag;



// @configuration nécéssaire pour spécifier que cette classe est une classe de configuration
// de l'application spring
@Configuration
public class RepositoryConfig extends RepositoryRestConfigurerAdapter {

	// configuration spécifique au repository spring data rest
	@Override
	public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
		super.configureRepositoryRestConfiguration(config);
		// demander a spring data rest de renvoyer la clé primaire
		// avec le reste de l'objet dans le json pour les entités spécifiées
		config.exposeIdsFor(Content.class, Image.class, Tag.class);
	}

	
	
}
