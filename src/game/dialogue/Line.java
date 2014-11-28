package game.dialogue;

import game.launcher.Launcher;
import game.system.application.Application;

public class Line 
{
	private String replique;
	private String enonciateur;
	
	public Line(String replique, String enonciateur)
	{
		this.replique = replique;
		this.enonciateur = enonciateur;
	}
	
	public Line(String replique)
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
		return (enonciateur + " :\n" + replique)	.replaceAll("\\$mage", Application.application().getGame().getParty().getMage().getName())
													.replaceAll("\\$rodeur", Application.application().getGame().getParty().getRanger().getName())
													.replaceAll("\\$guerrier", Application.application().getGame().getParty().getWarrior().getName())
													.replaceAll("\\$argent", String.valueOf(Application.application().getGame().getParty().getMoney()))
													.replaceAll("\\$select\\[.*\\]", "")
													.replaceAll("\\$end", "")
													.replaceAll("\\$goto[0-9]+", "")
													.replaceAll("\\$if\\[.*\\]", "")
													.trim();
						
		}
		else
		{
			return (replique)	.replaceAll("\\$mage", Application.application().getGame().getParty().getMage().getName())
								.replaceAll("\\$rodeur", Application.application().getGame().getParty().getRanger().getName())
								.replaceAll("\\$guerrier", Application.application().getGame().getParty().getWarrior().getName())
								.replaceAll("\\$argent", String.valueOf(Application.application().getGame().getParty().getMoney()))
								.replaceAll("\\$select\\[.*\\]", "")
								.replaceAll("\\$end", "")
								.replaceAll("\\$goto[0-9]+", "")
								.replaceAll("\\$if\\[.*\\]", "")
								.trim();
		}
	}
	
	public String getLine()
	{
		return replique;
	}
}
