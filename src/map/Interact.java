package map;

/**
 * Cette classe permet d'�x�cution des actions sur un tile
 * comme le supprimer ou le r�afficher par exemple.
 * 
 * @author Darklev
 *
 */
public class Interact {
	
	//id du tile
	private int idTile;
	
	//valeur � ajouter � l'id du tile lors de l'interact.
	private int change;
	
	//coordonn�es du tile cible
	private int x;
	private int y;
	private int z;
	
	private boolean active = false;
	
	/**
	 * 
	 * @param x
	 * 		Abscisse du tile cible.
	 * @param y
	 * 		Ordonn�e du tile cible.
	 * @param z
	 * 		Layer du tile cible.
	 * @param change
	 * 		Valeur � ajouter � l'id du tile cible.
	 */
	public Interact(int x, int y, int z, int change){
		this.x = x;
		this.y = y;
		this.z = z;
		this.change = change;
	}
	
	/**
	 * Supprime ou r�affiche le tile cible de l'interact courant.
	 * @param map
	 */
	public void toggleActive(Map map)
	{
		active = !active;
		
		if(active){
			map.getTiledMap().setTileId(x, y, z, 0); //suppression
		}
		else{
			map.getTiledMap().setTileId(x, y, z, idTile); //r�affichage
		}
		

	}
	
	/**
	 * Retourne l'abscisse du tile cible.
	 * 
	 * @return l'abscisse du tile cible.
	 */
	public int getX()
	{
		return x;
	}
	
	/**
	 * Retourne l'ordonn�e du tile cible.
	 * 
	 * @return l'ordonn�e du tile cible.
	 */
	public int getY(){
		return y;
	}
	
	/**
	 * Retourne le layer du tile cible.
	 * 
	 * @return le layer du tile cible.
	 */
	public int getZ(){
		return z;
	}
	
	/**
	 * Parm�tre le tile cible.
	 * @param idTile
	 * 		L'id du tile cible.
	 */
	public void setTileId(int idTile){
		this.idTile = idTile;
	}
	
}
