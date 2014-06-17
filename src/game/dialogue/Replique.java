package game.dialogue;

import game.Jeu;

public class Replique 
{
	private String replique;
	private String enonciateur;
	
	public Replique(String replique, String enonciateur)
	{
		this.replique = replique;
		this.enonciateur = enonciateur;
	}
	
	public Replique(String replique)
	{
		this.replique = replique;
	}
	
	/**
	 * Retourne :
	 * "Enonciateur :\n
	 * replique "
	 * 
	 * Variables et commandes :
	 * <ul>
	 * <li>$mage : remplac�e par le nom du mage de l'�quipe</li>
	 * <li>$rodeur : remplac�e par le nom du rodeur de l'�quipe</li>
	 * <li>$guerrier : remplac�e par le nom du guerrier de l'�quipe</li>
	 * <li>$argent : remplac�e par le nombre d'argent de l'�quipe</li>
	 * <li>$select[choix1, choix2{end}, choix3{1}, ...] : ordonne la cr�ation d'un s�lectionneur avec 
	 * pour choix : choix1, choix2 et choix3. Le nombre entre acolade apr�s les choix (optionnel)
	 * correspond � l'index du dialogue si le joueur valide ce choix ("end" met fin au dialogue)</li>
	 * <li>$end : quitte de force le dialogue</li>
	 * <li>$gotoX : force le dialogue � aller � la r�plique d'index X</li>
	 * </ul>
	 * 
	 * @return
	 * 		Retourne une cha�ne de caract�re avec l'enonciateur et la r�plique.
	 */
	public String getFormatStandard()
	{
		if(enonciateur != null)
		{
		return (enonciateur + " :\n" + replique)	.replaceAll("\\$mage", Jeu.getEquipe().getMage().getNom())
													.replaceAll("\\$rodeur", Jeu.getEquipe().getRodeur().getNom())
													.replaceAll("\\$guerrier", Jeu.getEquipe().getGuerrier().getNom())
													.replaceAll("\\$argent", String.valueOf(Jeu.getEquipe().getArgent()))
													.replaceAll("\\$select\\[.*\\]", "")
													.replaceAll("\\$end", "")
													.replaceAll("\\$goto[0-9]+", "")
													.replaceAll("\\$if\\[.*\\]", "")
													.trim();
						
		}
		else
		{
			return (replique)	.replaceAll("\\$mage", Jeu.getEquipe().getMage().getNom())
								.replaceAll("\\$rodeur", Jeu.getEquipe().getRodeur().getNom())
								.replaceAll("\\$guerrier", Jeu.getEquipe().getGuerrier().getNom())
								.replaceAll("\\$argent", String.valueOf(Jeu.getEquipe().getArgent()))
								.replaceAll("\\$select\\[.*\\]", "")
								.replaceAll("\\$end", "")
								.replaceAll("\\$goto[0-9]+", "")
								.replaceAll("\\$if\\[.*\\]", "")
								.trim();
		}
	}
	
	public String getReplique()
	{
		System.out.println(replique);
		return replique;
	}
}
