package characters;


import game.Config;
import game.battle.IBattle;
import game.system.Configurations;
import game.system.application.Application;

import java.util.ArrayList;
import java.util.Iterator;

import map.Map;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import util.Random;
import bag.Bag;
import bag.IItems;
import bag.item.stuff.Stuff;



import data.DataManager;



/**
 * Une �quipe est compos�e de personnages.
 * 
 * Une �quipe ne peut comporter qu'un certain nombre de personnages.
 * 
 * @author Darklev
 *
 */
public class Party implements Iterable<Character>
{
	public static final int SOUTH = 0;
	public static final int NORTH = 1;
	public static final int EAST = 2;
	public static final int WEST = 3;
	
	
	//COORDONNEES
	private double relativeX; // par rapport � l'�cran
	private double relativeY;
	
	private double absoluteX; // par rapport � la Map
	private double absoluteY;
	
	private Map map;
	//___________

	//liste des id des ennemis analys�s
	private ArrayList<String> analysedEnnemis;
	private Bag sac;
	private int money;
	
	/**
	 * Direction dans laquelle regarde l'�quipe.
	 */
	private int direction;
	private boolean moving = false;
	
	private Character[] party;
	private int slots;
	
	
	/**
	 * Constructeur de Equipe.
	 * 
	 * @param slots
	 * 		Le nombre de personnages autoris� dans l'�quipe
	 * @throws SlickException 
	 */
	public Party(int slots) throws SlickException
	{
		party = new Character[slots];
		this.slots = slots;
		
		relativeX = Configurations.SCREEN_WIDTH/2;
		relativeY = Configurations.SCREEN_HEIGHT/2;
		
		absoluteX = 100;
		absoluteY = 100;
				
		analysedEnnemis = new ArrayList<String>();
		
		map = DataManager.loadMap("test1", - absoluteX + relativeX, - absoluteY + relativeY, true);
		money = 10;
		
		sac = new Bag();
		
		
		//Correcteur de coordonn�es relatives initiales
		/*
		if(absolueX<Map.LONG/2)
		{
			relativeX = absolueX;
		}
		if(absolueY<Map.LARG/2)
		{
			relativeY = absolueY;
		}
		*/
		//Fin correcteur de coordonn�es relatives initiales
	}
	 

	/**
	 * Ajoute un Personnage dans l'�quipe s'il y a assez de place dans l'�quipe.
	 * 
	 * @param Le personnage � ajouter.
	 * @return Vrai si le personnage a pu �tre ajout�, sinon faux.
	 * 
	 */
	public boolean add(Character character)
	{
		int i=0;
		while(i<slots && party[i]!=null)
		{
			i++;
		}
		
		if(i < slots)
		{
			party[i]=character;
			return true;
		}
		return false;
	}
	
	/**
	 * Retourne l'animation du premier personnage vivant de l'�quipe en fonction
	 * de la direction dans laquelle il regarde.
	 * 
	 * @return l'animation de l'�quipe.
	 */
	public Animation getAnimation()
	{
		return firstAliveCharacter().getAnimation(direction);
	}
	
	/**
	 * Retourne le premier personnage vivant de l'�quipe.
	 * @return
	 */
	private Character firstAliveCharacter() {
		for(Character c : this)
		{
			if(c.isAlive()){
				return c;
			}
		}
		return null;
	}


	/**
	 * Retourne la direction dans la quelle regarde l'�quipe.
	 * 
	 * @return la direction dans la quelle regarde l'�quipe.
	 */
	public int getDirection()
	{
		return direction;
	}
	
	/**
	 * Iterator permet de parcourir le tableau de personnages equipe
	 * avec une boucle foreach.
	 */
	@Override
	public Iterator<Character> iterator() 
	{
		ArrayList<Character> col = new ArrayList<Character>();
		for(Character p : party)
		{
			col.add(p);
		}		
		
		return col.iterator();
	}

	/**
	 * Retourne la map sur laquelle se trouve l'equipe.
	 * @return la map sur laquelle se trouve l'�quipe.
	 */
	public Map getMap() 
	{
		return map;
	}
	
	/**
	 * Retourne le sac de l'�quipe o� se trouve les objets 
	 * de l'�quipe.
	 * 
	 * @return le sac de l'�quipe.
	 */
	public Bag getBag()
	{
		return sac;
	}

	/**
	 * Retourne la monaie de l'�quipe.
	 * 
	 * @return la monaie de l'�quipe.
	 */
	public int getMoney()
	{
		return money;
	}
	
	/**
	 * Retourne la citesse de d�placement en pixel/frame de l'�quipe.
	 * Il s'agit de la vitesse du personnage � l'index 0.
	 * 
	 * @return la vitesse de l'�quipe.
	 */
	public double speed()
	{
		//Modifier vitesse 10 default 
		return 15 + party[0].getSpeed();
	}

	/**
	 * Met � jour la position relative (coordonn�es par rapport � la fen�tre) sur l'axe X de l'�quipe.
	 * 
	 * @param dx
	 * 		D�placement horizontal.
	 * 
	 */
	public void setRelativeX(double dx)
	{	
		party[0].getSkin().moveX((float) dx);
		relativeX += dx;
	}
	
	/**
	 * Met � jour la position relative (coordonn�es par rapport � la fen�tre) sur l'aye Y de l'�quipe.
	 * 
	 * @param dy
	 * 		D�placement vertical
	 * 
	 */
	public void setRelativeY(double dy)
	{		
		party[0].getSkin().moveY((float) dy);
		relativeY += dy;		
	}
	
	/**
	 * Change la direction dans laquelle regarde l'�quipe.
	 * 
	 * @param la nouvelle direction (NORTH,SOUTH,EAST,WEST)
	 */
	public void setDirection(int direction) 
	{
		this.direction = direction;
	}

	/**
	 * Retourne la position relative (par rapport � la fenetre) de l'�quipe sur l'axe X.
	 * 
	 * @return la position relative (par rapport � la fenetre) de l'�quipe sur l'axe X.
	 */
	public double getRelativeX() 
	{
		return relativeX;
	}
	
	/**
	 * Retourne la position relative (par rapport � la fenetre) de l'�quipe sur l'axe Y.
	 * 
	 * @return la position relative (par rapport � la fenetre) de l'�quipe sur l'axe Y.
	 */
	public double getRelativeY() 
	{
		return relativeY;
	}
	
	/**
	 * Retourne la position absolue (par rapport � la map) de l'�quipe sur l'axe X.
	 * 
	 * @return la position absolue (par rapport � la map) de l'�quipe sur l'axe X.
	 */
	public double getAbsoluteX()
	{
		return absoluteX;
	}

	/**
	 * Met � jour la position absolue (coordonn�es par rapport � la map) sur l'aye X de l'�quipe.
	 * 
	 * @param dx
	 * 		D�placement sur l'axe X
	 * 
	 */
	public void setAbsoluteX(double dx) 
	{
		this.absoluteX += dx;
	}
	
	
	/**
	 * Retourne la position absolue (par rapport � la map) de l'�quipe sur l'axe Y.
	 * 
	 * @return la position absolue (par rapport � la map) de l'�quipe sur l'axe Y.
	 */	
	public double getAbsoluteY() 
	{
		return absoluteY;
	}


	/**
	 * Met � jour la position absolue (coordonn�es par rapport � la map) sur l'aye Y de l'�quipe.
	 * 
	 * @param dy
	 * 		D�placement sur l'axe Y
	 */
	public void setAbsoluteY(double dy) 
	{
		this.absoluteY += dy;
	}

	
	/**
	 * Retourne le personnage � la position pass� en param�tre.
	 * 
	 * @param i
	 * 		Index du personnage � retourner.
	 * @return
	 * 		le personnage de l'�quipe � l'index i.
	 */
	public Character get(int i) 
	{
		return party[i];
		
	}
	
	/**
	 * Retourne le personnage � l'index pass� en param�tre ou retourne
	 * null si le personnage n'existe pas.
	 * 
	 * @param i
	 * 	Index du personnage.
	 * @return
	 * 	le personnage � l'indexe i sauf, ou null s'il n'existe pas.
	 */
	public Character getIfExists(int i) {
		Character res;
		try{
			res = party[i];
		}
		catch(IndexOutOfBoundsException e){
			res = null;
		}

		return res;
	}
	
	/**
	 * Modifie l'ordonn�e relative de l'�quipe (par raport � l'�cran).
	 * 
	 * @param y
	 * 		Nouvelle ordonn�e relative.
	 */
	public void setValRelativeY(double y) 
	{
		this.relativeY = y;
		party[0].getSkin().setY((float) y);
	}


	/**
	 * Modifie l'abscisse relative de l'�quipe (par raport � l'�cran).
	 * 
	 * @param x
	 * 		Nouvelle abscisse relative.
	 */
	public void setValRelativeX(double x) {
		this.relativeX = x;
		party[0].getSkin().setX((float) x);
	}


	/**
	 * Affiche sur la fen�tre un r�sum� de chaque personnage de l'�quipe (nom, xp, pv ...)
	 *  
	 * @param g
	 * 		Graphics
	 * @param selection
	 * 		Index du personnage s�lectionn� par le joueur.
	 */
	public void renderTeamList(Graphics g, int selection)
	{
		for(int i=0;i<party.length;i++)
		{
			party[i].renderCharacterPanel(g,0,160*i,selection == i);
		}
		
	}


	/**
	 * Retourne le nombre de personnages dans l'�quipe.
	 * 
	 * @return
	 * 		Le nombre de personnage dans l'�qupe.
	 */
	public int numberOfCharacters() 
	{
		int total = 0;
		for(Character p : party)
		{
			if(p!=null)
			{
				total++;
			}
		}
		return total;
	}


	/**
	 * Permutte la position d'un personnage de l'�quipe avec celle d'un autre.
	 * 
	 * @param p0
	 * 		Position initiale du personnage.
	 * @param pF
	 * 		Nouvelle poistion du personnage.
	 */
	public void swap(int p0, int pF) 
	{
		Character temp = party[p0];
		party[p0] = party[pF];
		party[pF] = temp;
	}
	
	/**
	 * Retourne l'index du personnage pass� en param�tre ou -1 si le personnage n'est pas dans l'�quipe.
	 * @param personnage
	 * 		Personnage dont l'index est � retourner.
	 * @return
	 * 		l'index du personnage pass� en param�tre ou -1 si le personnage n'est pas dans l'�quipe.
	 */
	public int indexOf(Character personnage)
	{
		for(int i = 0; i < numberOfCharacters(); i++)
		{
			if(personnage.equals(party[i]))
			{
				return i;
			}
		}
		return -1;
	}



	/**
	 * Retourne le nombre de personnage dont les PV sont >= 1.
	 * 
	 * @return le nombre de personnages vivants dans l'�quipe.
	 */
	public int numberOfAliveCharacters() 
	{
		int total = 0;
		for(Character p : party)
		{
			if(p.isAlive())
			{
				total++;
			}
		}
		return total;
	}
	
	/**
	 * Retourne vrai si tous les membres de l'�quipe pr�parent une action 
	 * lors d'un combat, sinon retourne faux.
	 * 
	 * @return vrai si tous les personnages de l'�quipe sont pr�ts, faux sinon.
	 */
	public boolean isReady()
	{
		for(Character p : party)
		{
			if(!p.estActionPreparee());
			{
				return false;
			}
		}
		return true;
	}


	/**
	 * Retourne un personnage al�atoire parmis les personnages vivants de l'�quipe.
	 * 
	 * @return un personnage al�atoire vivant de l'�quipe.
	 */
	public Object getRandomCharacter() 
	{
		if(numberOfAliveCharacters() == 0)
		{
			return null;
		}
		else
		{
			int a = (int) (Math.random() * numberOfAliveCharacters());
			while (!party[a].isAlive())
			{
				a = (a + 1) % numberOfCharacters();
			}
			return party[a];
		}
	}


	/**
	 * Retourne vrai si l'�quipe a d�j� analys� l'ennemi dont l'id est pass� en param�tre, sinon faux.
	 * @param id
	 * 		L'id de l'ennemi.
	 * @return
	 * 		 vrai si l'�quipe a d�j� analys� l'ennemi dont l'id est pass� en param�tre, sinon faux.
	 */
	public boolean isAnalysed(String id) 
	{
		return analysedEnnemis.contains(id);
	}

	/**
	 * Retourne le mage de l'�quipe ou null s'il n'y a pas de mage.
	 * 
	 * @return 
	 * 		le mage de l'�quipe ou null s'il n'y a pas de mage.
	 */
	public Mage getMage()
	{
		for(Character p : party)
		{
			if(p instanceof Mage)
			{
				return (Mage)p;
			}
		}
		return null;
	}

	/**
	 * Retourne le rodeur de l'�quipe ou null s'il n'y a pas de rodeur.
	 * 
	 * @return 
	 * 		le rodeur de l'�quipe ou null s'il n'y a pas de rodeur.
	 */
	public Ranger getRanger()
	{		
		for(Character p : party)
		{
			if(p instanceof Ranger)
			{
				return (Ranger)p;
			}
		}
		return null;
	}
	
	
	/**
	 * Retourne le guerrier de l'�quipe ou null s'il n'y a pas de guerrier.
	 * 
	 * @return 
	 * 		le guerrier de l'�quipe ou null s'il n'y a pas de guerrier.
	 */
	public Warrior getWarrior()
	{		
		for(Character p : party)
		{
			if(p instanceof Warrior)
			{
				return (Warrior)p;
			}
		}
		return null;
	}
	
	
	/**
	 * Retourne vrai si l'equipable pass� en param�tre est �quip�
	 * par l'un des personnage de l'�quipe, sinon faux.
	 * 
	 * @param equipable
	 * 		Stuff � tester
	 * 
	 * @return vrai si l'equipable pass� en param�tre est �quip�
	 * par l'un des personnage de l'�quipe, sinon faux.
	 */
	public boolean isEquiped(Stuff equipable)
	{
		for(Character p : this)
		{
			for(Stuff e : p.armors.values())
			{
				if(e != null)
				{
					if(e.equals(equipable))
					{
						return true;
					}
				}
			}
			
			if(p.mainWeapon != null)
			{
				if(p.mainWeapon.equals(equipable))
				{
					return true;
				}
			}
			
			if(p.secondWeapon != null)
			{
				if(p.secondWeapon.equals(equipable))
				{
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Ajoute un objet en quantit� pass� en param�tre dans le sac de l'�quipe.
	 * 
	 * @param item
	 * 		Objet � ajouter.
	 * @param quantity
	 * 		Quantit� de l'objet � ajouter.
	 */
	public void addItemInBag(IItems item,int quantity)
	{
		sac.add(item, quantity);
	}
	
	/**
	 * Retourne un tableau avec tous les personnages de l'�quipe.
	 * 
	 * @return un tableau des personnages de l'�quipe.
	 */
	public Character[] getCharacters()
	{
		return party;
	}

	/**
	 * Ajoute un ennemi � la liste des ennemis analys�s.
	 * (Comp�tence "Recherche" du mage)
	 * 
	 * @param id
	 *		L'id de l'ennemi analys�.
	 */
	public void analyse(String id)
	{
		analysedEnnemis.add(id);
	}

	/**
	 * CHange la map o� se trouve l'�quipe.
	 * 
	 * @param map
	 * 		Nouvelle map o� se trouve l'�quipe.
	 */
	public void setMap(Map map) 
	{
		this.map = map;
	}

	/**
	 * Modifie l'absisse absolue de l'�quipe. (par rapport � la map)
	 * 
	 * @param x
	 * 		Nouvelle abscisse absolue
	 */
	public void setValAbsoluteX(double x)
	{
		absoluteX = x;	
	}
	
	/**
	 * Modifie l'ordonn�e absolue de l'�quipe. (par rapport � la map)
	 * 
	 * @param x
	 * 		Nouvelle ordonn�e absolue
	 */
	public void setValAbsoluteY(double y)
	{
		absoluteY = y;	
	
	}

	/**
	 * Met � jour la monaie de l'�quipe en ajoutant la valeur pass� en param�tre.
	 * @param money
	 * 		Valeur � ajouter � la monaie de l'�quipe.
	 */
	public void updateMoney(int money)
	{
		this.money += money;
	}

	
	/**
	 * Dessine l'�quipe.
	 */
	public void draw() {
		draw(1, false);
	}
	
	/**
	 * Affiche l'�quipe avec une certiane opacit�
	 * 	
	 * @param alpha
	 * 		Opacit� entre 0 et 1
	 * @param absolute
	 * 		Vrai s'il faut utiliser les coordonn�es absolues. Sinon faux.
	 */
	public void draw(float alpha, boolean absolute) {
		Animation animation =  get(0).getAnimation(direction).copy();
		animation.setAutoUpdate(true);
		for(int i=0; i<animation.getFrameCount() ;i++){
			animation.getImage(i).setAlpha(alpha);
		}
		
		int x = (int)(relativeX - animation.getWidth()/2);
		int y = (int)(relativeY - animation.getHeight()/2);
		
		if(absolute)
		{
			x = (int) (Application.application().getGame().getParty().getMap().getX() + absoluteX);
			y = (int) (Application.application().getGame().getParty().getMap().getY() + absoluteY);

		}
		
		if(moving){
			animation.draw(x, y);
		}
		else{
			animation.getImage(2).draw(x, y);
		}
	}
	
	public void update(Input in){
		
	}


	/**
	 * Indique si l'�quipe est en mouvement ou non.
	 * @param moving
	 * 		Vrai si l'�quipe est en mouvement, sinon faux.
	 */
	public void setMoving(boolean moving) {
		this.moving = moving;
	}


	/**
	 * Retourne un personnage al�atoire pouvant �tre cibl�.
	 * 
	 * @return un personnage valide de fa�on al�atoire.
	 */
	public IBattle getRandomValidTarget() {
		
		ArrayList<IBattle> validTargets = getValidTargets();
		
		return validTargets.get(Random.randInt(0, validTargets.size() - 1));
	}


	/**
	 * Retourne les personnages pouvant �tre cibl� par une comp�tence ou un objet ...
	 * 
	 * @return les personnages pouvant �tre cibl�s.
	 */
	public ArrayList<IBattle> getValidTargets() {
		
		ArrayList<IBattle> validTargets = new ArrayList<IBattle>();
		for(Character c : this){
			if(c.isAlive()){
				validTargets.add(c);
			}
		}
		return validTargets;
	}
	
	public void drawHitbox(Color color){
		party[0].getSkin().drawHitbox(color);
	}
}
