package bag;

import bag.item.stuff.Weapon;
import bag.item.stuff.Armor;

/**
 * Classe repr�sentant un sac permettant de contenir
 * des objets et de les trier.
 * 
 * @author Darklev
 *
 */
public class Bag 
{

	private Category miscellaneous; //objets divers (ingr�dient ..)
	private Category battle; //objets utilisables en combat (potions, ...)
	private Category stuff; //armes et armures
	private Category rares; //objet de qu�te  
	
	/**
	 * Constructeur
	 */
	public Bag()
	{
		miscellaneous = new Category();
		battle = new Category();
		stuff = new Category();
		rares = new Category();
	}
	
	/**
	 * Retourne la cat�gorie "divers".
	 * 
	 * @return la cat�gorie "divers".
	 * 
	 */
	public Category getMiscellaneous()
	{
		return miscellaneous;
	}

	/**
	 * Retourne la cat�gorie "battle".
	 * 
	 * @return la cat�gorie "battle".
	 */
	public Category getCombat() 
	{
		return battle;
	}

	/**
	 * Retourne la cat�gorie "stuff".
	 * 
	 * @return la cat�gorie "stuff".
	 */
	public Category getStuff() 
	{
		return stuff;
	}
	
	/**
	 * Retourne la cat�gorie "rare".
	 * 
	 * @return la cat�gorie "rare".
	 */
	public Category getRares()
	{
		return rares;
	}
	
	/**
	 * Ajoute une objet en le classant automatique dans une cat�gorie.
	 * 
	 * @param item
	 * 		Objet � ajouter.
	 * @param quantity
	 * 		Quantit� de l'objet � ajouter.
	 */
	public void add(IItems item, int quantity)
	{
		getItemCategory(item).add(item, quantity);
	}
	
	/**
	 * Supprimer un objet du sac.
	 * 
	 * @param item
	 * 		Objet � supprimer.
	 * @param quantity
	 * 		Quantit� � supprimer.
	 */
	public void supprimer(IItems item,int quantity)
	{
		getItemCategory(item).remove(item, quantity);
	}
	
	/**
	 * Retourne la cat�gorie d'un objet.
	 * 
	 * @param item
	 * 		L'objet dont la cat�gorie est � retourner.
	 * @return
	 * 		La cat�gorie dont l'objet pass� en param�tre appartient.
	 */
	private Category getItemCategory(IItems item)
	{
		if(item instanceof Weapon || item instanceof Armor)
		{
			return stuff;
		}
		else if(item.isUsableInBattle())
		{
			return battle;
		}
		else if(item.isRare())
		{
			return rares;
		}
		else
		{
			return miscellaneous;
		}
	}
	
	/**
	 * Retourne la quatit� restante d'un objet.
	 * 
	 * @param item
	 * 		Objet dont la quantit� restante est � retourner.
	 * @return la quantit� de l'objet dans le sac.
	 */
	public int getQuantity(IItems item)
	{
		return getItemCategory(item).getQuantity(item);
	}
}
