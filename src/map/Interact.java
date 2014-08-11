package map;


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
	
	public Interact(int x, int y, int z, int change){
		this.x = x;
		this.y = y;
		this.z = z;
		this.idTile = idTile;
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
		
		map.reloadCollisions();
	}
	
	public int getX()
	{
		return x;
	}
	
	
	public int getY(){
		return y;
	}
	
	public int getZ(){
		return z;
	}
	
	public void setTileId(int idTile){
		this.idTile = idTile;
	}
	
}
