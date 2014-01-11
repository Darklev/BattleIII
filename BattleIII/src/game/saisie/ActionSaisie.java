package game.saisie;

import java.util.HashMap;

/**
 * Permet de lancer des op�rations apr�s une saisies.
 * 
 * 
 * @author Darklev
 *
 */
public interface ActionSaisie 
{
	/**
	 * Op�ration(s) apr�s une saisie, comme affecter la saisie
	 * � une variable.
	 * 
	 * La saisie est obtenue avec Saisie.getSaisie();
	 * 
	 * @param param
	 * 		Dictionnaire de param�tres.
	 */
	public void action(HashMap<String, Object> param);

	/**
	 * Contr�le(s) � effectuer sur la saisie avant de lancer les op�rations.
	 * 
	 * @return Vrai si tous les contr�les sont valid�s, faux sinon.
	 */
	public boolean controle();
	

}
