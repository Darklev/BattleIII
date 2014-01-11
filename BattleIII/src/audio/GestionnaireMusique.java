package audio;


import java.util.HashMap;

import game.Config;

import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;



/**
 * M�thodes statiques permettant la gestion de la musique.
 * 
 * @author Darklev
 */
public class GestionnaireMusique 
{
	/**
	 * Musique en cours.
	 */
	private static Music current_music;
	
	/**
	 * Chemin vers le fichier .ogg
	 */
	private static String path = "#?NO MUSIC?#";
	

	/**
	 * Dictionnaire des musiques charg�es
	 */
	private static HashMap<String, Music> musiques;
	
	private GestionnaireMusique(){}
	
	/**
	 * Initialise la classe.
	 */
	public static void init()
	{
		musiques = new HashMap<String,Music>();
	}
	
	/**
	 * Joue en boucle la musique dont le nom est pass� en param�tre.
	 * S'il s'agit de la m�me musique que celle en cours, la nouvelle musique n'est pas charg�e
	 * et la musique en cours n'est donc pas interrompue.
	 * 
	 * @param musique
	 * 		Nom de la musique � jouer.
	 */
	public static void jouerEnBoucle(String musique)
	{
		if(musiques.containsKey(musique))
		{
			current_music = musiques.get(musique);
			path = "ressources/musiques/"+musique+".ogg";
			if(!musiques.get(musique).playing())
			{
				current_music.loop();
				setVolume();
			}
		}
		else
		{
			musique = "ressources/musiques/"+musique+".ogg";
			if(!path.equals(musique))
			{
					try 
					{
						path = musique;
						current_music = new Music(musique);
					}
					catch (SlickException e) 
					{
						e.printStackTrace();
					}
					current_music.loop();
					setVolume();
			}
		}
	}
	
	/**
	 * Stoppe la musique en cours.
	 */
	public static void stopper()
	{
				
		if(current_music != null)
		{
			if(current_music.playing())
			{
				current_music.stop();
			}
		}		
	}
	
	/**
	 * Met � jour le volume de la musique, en fonction du volume
	 * param�tr�e dans la Classe Jeu.Config.
	 */
	public static void setVolume()
	{
		current_music.setVolume(Config.musique/100f);
	}
	
	/**
	 * Charge une musique et la garde en m�moire dans le dictionnaire de musiques.
	 * Permet de jouer une musique plus rapidement avec la m�thode JouerEnBoucle().
	 * @throws SlickException 
	 */
	public static void chargerMusique(String musique) throws SlickException
	{
		musiques.put(musique, new Music("ressources/musiques/"+musique+".ogg"));
	}
}
