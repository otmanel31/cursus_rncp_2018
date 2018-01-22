package com.loncoto.firstJunitForm;

public class TextUtils {

	// capitalise une chaine (premiere lettre en majuscule)
	public String capitalise(String chaine) {
		if (chaine == null) return null;
		if (chaine.length() == 0) return "";
		return chaine.substring(0, 1).toUpperCase() + chaine.substring(1);
	}
	
	// renvoie la chaine dans le sens inverse
	public String inverse(String chaine) {
		if (chaine == null) return null;
		StringBuilder sb = new StringBuilder(chaine.length());
		for (int i = chaine.length() - 1; i >= 0; i--) {
			sb.append(chaine.charAt(i));
		}
		return sb.toString();
	}
	
}
