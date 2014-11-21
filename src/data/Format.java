package data;


/**
 * M�thodes permettant de formater des cha�nes de carac�res en fonction des besoin du jeu.
 * 
 * @author Darklev
 */
public class Format 
{
	/**
	 * Formate une chaine de caract�re pour la s�parer en plusieurs lignes
	 * (avec \n). Chaque ligne contiendra au plus le nombre pass� en param�tre de 
	 * caract�res.
	 * 
	 * @param string
	 * 		La chaine � formater.
	 * @param car
	 * 		Le nombre de caract�res maximale de carac�res par ligne.
	 * @return
	 * 		La chaine de caract�res sur plusieurs lignes.
	 */
	public static String multiLines(String string, int car)
	{
		String res = "";

		String[] container = string.split(" +");
		int car_ligne = 0;
		for(String mot : container)
		{
			car_ligne += mot.length()+1;
			if(car_ligne <= car)
			{
				res += " "+mot;
			}
			else
			{
				res += "\n"+mot;
				car_ligne = mot.length();
			}
		}
		return res.substring(1);
	}
}
