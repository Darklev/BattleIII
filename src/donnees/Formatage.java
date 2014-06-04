package donnees;


/**
 * M�thodes permettant de formater des cha�nes de carac�res en fonction des besoin du jeu.
 * 
 * @author Darklev
 */
public class Formatage 
{
	/**
	 * Formate une chaine de caract�re pour la s�parer en plusieurs lignes
	 * (avec \n). Chaque ligne contiendra au plus le nombre pass� en param�tre de 
	 * caract�res.
	 * 
	 * @param chaine
	 * 		La chaine � formater.
	 * @param car
	 * 		Le nombre de caract�res maximale de carac�res par ligne.
	 * @return
	 * 		La chaine de caract�res sur plusieurs lignes.
	 */
	public static String multiLignes(String chaine, int car)
	{
		String res = "";

		String[] container = chaine.split(" +");
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
