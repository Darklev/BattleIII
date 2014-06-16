package util;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Regex {
	
	/**
	 * Equivalent de la fonction eval en javascript, python...
	 * 
	 * @param subject
	 * 	Chaine de caract�res � evaluer
	 * @param regex
	 * 	Regex a utilis� pour evaluer la chaine.
	 * @return
	 * 	Un tableau de cha�nes de caract�res r�sultat de l'eval
	 */
	public static String[] eval(String subject, String regex){
		ArrayList<String> res = new ArrayList<String>();
		Pattern pattern = Pattern.compile(regex);
		
		Matcher matcher = pattern.matcher(subject);

		while(matcher.find()){
			int i = 0;
			while(i < matcher.groupCount() + 1){
				res.add(matcher.group(i));
				i++;
			}
		}
		
		System.out.println(res);
		
		return res.toArray(new String[res.size()]);
	}
}
