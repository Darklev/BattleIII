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
public class MusicManager 
{
	/**
	 * Musique en cours.
	 */
	private static Music currentMusic;
	
	/**
	 * Chemin vers le fichier .ogg
	 */
	private static String path = "#?NO MUSIC?#";
	

	/**
	 * Dictionnaire des musiques charg�es
	 */
	private static HashMap<String, Music> musics;
	
	private MusicManager(){}
	
	/**
	 * Initialise la classe.
	 */
	public static void init()
	{
		musics = new HashMap<String,Music>();
	}
	
	/**
	 * Joue en boucle la musique dont le nom est pass� en param�tre.
	 * S'il s'agit de la m�me musique que celle en cours, la nouvelle musique n'est pas charg�e
	 * et la musique en cours n'est donc pas interrompue.
	 * 
	 * @param musique
	 * 		Nom de la musique � jouer.
	 */
	public static void playLoop(String musique)
	{
		System.out.println(musique);
		if(musics.containsKey(musique))
		{
			currentMusic = musics.get(musique);
			path = "resources/musiques/"+musique+".ogg";
			if(!musics.get(musique).playing())
			{
				currentMusic.loop();
				setVolume();
			}
		}
		else
		{
			musique = "resources/musiques/"+musique+".ogg";
			if(!path.equals(musique))
			{
					try 
					{
						path = musique;
						currentMusic = new Music(musique);
					}
					catch (SlickException e) 
					{
						e.printStackTrace();
					}
					currentMusic.loop();
					setVolume();
			}
		}
	}
	
	/**
	 * Stoppe la musique en cours.
	 */
	public static void stop()
	{
				
		if(currentMusic != null)
		{
			if(currentMusic.playing())
			{
				currentMusic.stop();
			}
		}		
	}
	
	/**
	 * Met � jour le volume de la musique, en fonction du volume
	 * param�tr�e dans la Classe Jeu.Config.
	 */
	public static void setVolume()
	{
		currentMusic.setVolume(Config.musique/100f);
	}
	
	/**
	 * Charge une musique et la garde en m�moire dans le dictionnaire de musiques.
	 * Permet de jouer une musique plus rapidement avec la m�thode JouerEnBoucle().
	 * @throws SlickException 
	 */
	public static void loadMusic(String musique) throws SlickException
	{
		musics.put(musique, new Music("resources/musiques/"+musique+".ogg"));
	}
}
