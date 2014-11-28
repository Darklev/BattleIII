package map;

import game.launcher.Launcher;
import game.system.application.Application;
import bag.IItems;
import personnage.Party;

/**
 * Coffre pouvant se trouver sur une map.
 * 
 * @author Darklev
 *
 */
public class Chest
{
	
	//coordonn�es du coffre.
	private String idMap;
	private int x;
	private int y;
	
	//Contenu du coffre.
	private IItems content;
	private int quantity;
	private int money;
	
	/**
	 * Constructeur
	 * @param idMap
	 * 	Id de la map
	 * @param x
	 * 	Abscisse du coffre
	 * @param y
	 * 	Ordonn�es du coffre
	 * @param content
	 * 	Contenu du coffre
	 * @param quantity
	 * 	Quantit� de contenus du coffre
	 * @param money
	 * 	Monaie dans le coffre
	 */
	public Chest(String idMap, int x, int y, IItems content,int quantity, int money)
	{
		this.idMap = idMap;
		this.x = x;
		this.y = y;
		this.quantity = quantity;
		
		this.content = content;
		this.money = money;
	}
	
	/**
	 * Coffre de comparaison.
	 * @param idMap
	 * @param x
	 * @param y
	 */
	protected Chest(String idMap, int x, int y)
	{
		this.idMap = idMap;
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Retourne le contenu du coffre.
	 * @return
	 * 	Le contenu du coffre.
	 */
	public IItems getContenu()
	{
		return content;
	}
	
	/**
	 * Retourne la quantit� du contenu du coffre.
	 * @return
	 * 	La quantit� du contenu du coffre.
	 */
	public int getQuantity()
	{
		return quantity;
	}
	
	/**
	 * Retourne la quantit� de monaie dans le coffre.
	 * @return
	 * 		Quantit� de monaie dans le coffre.
	 */
	public int getMoney()
	{
		return money;
	}
	
	/**
	 * L'Equipe pass� en param�tre r�cup�re le contenu (dont PO) du coffre
	 * et le place dans le sac.
	 * 
	 * @param party
	 * 		Equipe de la session du jeu.
	 */
	public void gainContent(Party party)
	{
		if(content != null)
		{
			party.addItemInBag(content, quantity);
		}
		else if(money > 0)
		{
			party.updateMoney(money);
		}
		
		Application.application().getGame().addOpenedChest(this);
	}
	
	/*
	public boolean equals(Coffre coffre)
	{		
		return
			coffre.x == x &&
			coffre.y == y &&
			coffre.idMap == idMap;
	}
	*/
	
	@Override
	public boolean equals(Object coffre)
	{
		if(!(coffre instanceof Chest))
		{
			return false;
		}
		
		return
			((Chest)coffre).x == x &&
			((Chest)coffre).y == y &&
			((Chest)coffre).idMap.equals(idMap);
	}

	/**
	 * Retourne l'abscisse du coffre.
	 * @return
	 * 	l'abscisse du coffre.
	 */
	public int getX() 
	{
		return x;
	}
	
	/**
	 * Retourne l'ordonn�e du coffre.
	 * 
	 * @return
	 * 	L'ordonn�e du coffre.
	 */
	public int getY() 
	{
		return y;
	}
}
