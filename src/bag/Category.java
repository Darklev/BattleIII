package bag;

import java.util.ArrayList;

import org.newdawn.slick.Image;

/**
 * Repr�sente une cat�gorie d'objet pour le sac.
 * 
 * @author Darklev
 *
 */
public class Category 
{
	private ArrayList<IItems> items;
	private ArrayList<Integer> quantities;

	/**
	 * Constructeur
	 */
	public Category()
	{
		items = new ArrayList<IItems>();
		quantities = new ArrayList<Integer>();
	}
	
	/**
	 * Ajoute un objet en quantit� pass� en param�tre.
	 * 
	 * @param item
	 * 		Objet � ajouter.
	 * @param quantity
	 * 		Quantit� de l'objet � ajouter.
	 */
	public void add(IItems item, int quantity)
	{
		if(items.contains(item))
		{
			int index = -1;
			for(int i = 0; i<items.size();i++)
			{
				if(items.get(i).equals(item))
				{
					index = i;
					break;
				}
			}
			quantities.set(index, quantities.get(index) + quantity);
		}
		else
		{
			items.add(item);
			quantities.add(quantity);
		}
	}
	
	/**
	 * Supprime un objet en quantit� pass� en param�tre.
	 * 
	 * @param item
	 * 		Objet � supprimer.
	 * @param quantity
	 * 		Quantit� � supprimer.
	 */
	public void remove(IItems item, int quantity)
	{
		if(items.contains(item))
		{
			int index = -1;
			for(int i = 0; i<items.size();i++)
			{
				if(items.get(i).equals(item))
				{
					index = i;
					break;
				}
			}
			if(quantities.get(index) - quantity <= 0)
			{
				quantities.remove(index);
				items.remove(index);
			}
			else
			{
				quantities.set(index, quantities.get(index) - quantity);
			}
		}
	}
	
	/**
	 * Retourne le nombre d'objets diff�rents stocker dans la cat�gorie.
	 * 
	 * @return	le nombre d'objets diff�rents stock�s dans la cat�gorie.
	 */
	public int size()
	{
		return items.size();
	}

	/**
	 * Retourne l'objet � l'index pass� en param�tre.
	 * 
	 * @param i
	 * 		Indexe de l'objet � retourner.
	 * @return l'objet � l'index pass� en param�tre.
	 */
	public IItems getItem(int i) 
	{
		return items.get(i);
	}
	
	/**
	 * Retourne la quantit� restante de l'objet pass� en param�tre.
	 * @param item
	 * 		Objet dont la quantit� restante est pass�e en param�tre.
	 * @return
	 * 		La quantit� restante de l'objet pass� en param�tre.
	 */
	public int getQuantity(IItems item) 
	{
		int i = 0;
		for(IItems o : items)
		{
			if(o == item)
			{
				return quantities.get(i);
			}
			i++;
		}
		return 0;
	}
	
	/**
	 * Retourne la quantit� restante de l'objet dont l'indexe est pass� en param�tre.
	 * @param i
	 * 		Index de l'objet dont la quantit� est � retourner.
	 * @return
	 * 		La quantit� restante de l'objet � l'indexe i.
	 */
	public int getQuantity(int i) 
	{
		return quantities.get(i);
	}



}
