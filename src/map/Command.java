package map;

import java.util.ArrayList;

/**
 * Commande sp�ciale pour la map.
 * La commande se trouve sur une map.
 * @author Sulivan
 *
 */
public class Command {
	
	//coordonn�es de la commande.
	private int x;
	private int y;
	private int z;
	
	private ArrayList<Interact> targets;

	/**
	 * Constructeur
	 * @param x
	 * 	Abscisse de la commande
	 * @param y
	 * 	Ordonn�e de la commande
	 * @param z
	 * 	Layer de la commande
	 * @param targets
	 * 	Cibles de la commande
	 */
	public Command(int x,int y, int z, ArrayList<Interact> targets){
		this.targets = targets;
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	/**
	 * Ex�cute la commande
	 * @param map
	 * 	Map sur la quelle la commande doit �tre ex�cut�e.
	 */
	public void run(Map map)
	{
		for(Interact i : targets){
			i.toggleActive(map);
		}
	}
	
	/**
	 * Retourne l'abscisse de la commande.
	 * @return
	 * 	l'abscisse de la commande.
	 */
	public int getX(){
		return x;
	}
	
	/**
	 * Retourne l'ordonn�e de la commande.
	 * @return
	 * 	l'ordonn�e de la commande.
	 */
	public int getY(){
		return y;
	}
	
	/**
	 * Retourne une arraylist de tous les interacts cibles de la commande.
	 * @return
	 * 	une arraylist de tous les interacts cibles de la commande.
	 */
	public ArrayList<Interact> getInteracts(){
		return targets;
	}
	
	
}
