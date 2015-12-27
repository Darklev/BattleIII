package map;

import java.util.ArrayList;

import org.newdawn.slick.tiled.TiledMap;

/**
 * Fabrique d'AnimatedTileManager.
 * @author Sulivan
 *
 */
public class FactoryAnimatedTileManager 
{
	/**
	 * Fabrique une AnimatedTileManager � partir d'une tile d'une map.
	 * 
	 * @param map
	 * 		Map dont le tile anim� appartient.
	 * @param idTile
	 * 		Id de la tile anim�e.
	 * @return
	 * 		L'AnimatedTileManager du tile pass� en param�tre ou retourne null 
	 * 		si le tile n'est pas un tile anim�.
	 */
	public static AnimatedTileManager make(TiledMap map, int idTile)
	{
		String animRequest = map.getTileProperty(idTile, "animation", "false");
		String type = map.getTileProperty(idTile, "animationtype", "loop");
		int codeType = 0;
		if(type.equalsIgnoreCase("loop"))
		{
			codeType = AnimatedTileManager.LOOP;
		}
		else if(type.equalsIgnoreCase("reverse"))
		{
			codeType = AnimatedTileManager.REVERSE;
		}
		
		
		if(!animRequest.equals("false"))
		{
			//r�cup�ration de toutes les tiles n�cessaires � l'animation
			ArrayList<Integer> timekeys = new ArrayList<Integer>();
			int i = idTile;
			int time = 0;
			int timekey = 0;
			do
			{
				timekey = parseTile(i, map);
				if(timekey != -1)
				{
					time += timekey;
					timekeys.add(time);
				}
				i++;
			}
			while(timekey != -1);
			return new AnimatedTileManager(codeType, timekeys , idTile);
		}
		else
		{
			return null;
		}
		
	}
	
	/**
	 * Analyse une tile pour retourner la tile suivante de son animation.
	 * Retourne -1 si c'est la derni�re tile.
	 * @param idTile
	 * 		Tile qui doit �tre analys�.
	 * @param map
	 * 		Map o� se trouve la tile � analyser.
	 * @return
	 * 		L'id du tile suivant dans l'animation ou -1 si c'est le dernier tile de l'animation.
	 */
	private static int parseTile(int idTile, TiledMap map)
	{
		String tid = map.getTileProperty(idTile, "animation", "false");
		
		try{
			int next = Integer.parseInt(tid);
			return next;
		}
		catch(NumberFormatException e)
		{
			return -1;
		}
		
	}
	
	

}
