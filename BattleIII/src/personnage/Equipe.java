package personnage;


import game.Config;

import java.util.ArrayList;
import java.util.Iterator;
import map.Map;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Graphics;

import sac.IObjet;
import sac.Sac;


import donnees.GenerateurDonnees;



/**
 * Une �quipe est compos�e de personnages.
 * 
 * Une �quipe ne peut comporter qu'un certain nombre de personnages.
 * 
 * @author Darklev
 *
 */
public class Equipe implements Iterable<Personnage>
{
	public static final int BAS = 0;
	public static final int HAUT = 1;
	public static final int DROITE = 2;
	public static final int GAUCHE = 3;
	
	
	//COORDONNEES
	private double relativeX; // par rapport � l'�cran
	private double relativeY;
	
	private double absolueX; // par rapport � la Map
	private double absolueY;
	
	private Map map;
	//___________
	
	private ArrayList<String> recherches;
	private Sac sac;
	private int argent;
	
	/**
	 * Direction dans laquelle regarde l'�quipe.
	 */
	private int direction;
	
	
	private Personnage[] equipe;
	private int nbPlaces;
	
	
	/**
	 * Constructeur de Equipe.
	 * 
	 * @param nbSlots
	 * 		Le nombre de personnages autoris� dans l'�quipe
	 */
	public Equipe(int nbSlots)
	{
		equipe = new Personnage[nbSlots];
		nbPlaces = nbSlots;
		
		relativeX = Config.LONGUEUR/2;
		relativeY = Config.LARGEUR/2;
		
		absolueX = 100;
		absolueY = 100;
				
		recherches = new ArrayList<String>();
		
		map = GenerateurDonnees.genererMap("test1", - absolueX + relativeX, - absolueY + relativeY);
		argent = 10;
		
		sac = new Sac();
		
		
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
	public boolean ajouter(Personnage personnage)
	{
		int i=0;
		while(i<nbPlaces && equipe[i]!=null)
		{
			i++;
		}
		
		if(i < nbPlaces)
		{
			equipe[i]=personnage;
			return true;
		}
		return false;
	}
	
	/**
	 * Retourne l'animation du premier personnage de l'�quipe en fonction
	 * de la direction dans laquelle il regarde.
	 */
	public Animation getAnimation()
	{
		return equipe[0].getAnimation(direction);
	}
	
	/**
	 * Retourne la direction dans la quelle regarde l'�quipe.
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
	public Iterator<Personnage> iterator() 
	{
		ArrayList<Personnage> col = new ArrayList<Personnage>();
		for(Personnage p : equipe)
		{
			col.add(p);
		}		
		
		return col.iterator();
	}

	/**
	 * Retourne la map sur laquelle se trouve l'equipe.
	 * @return
	 */
	public Map getMap() 
	{
		return map;
	}
	
	public Sac getSac()
	{
		return sac;
	}

	public int getArgent()
	{
		return argent;
	}
	
	public double vitesse()
	{
		//Modifier vitesse 10 default 
		return 15 + equipe[0].getVitesse();
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
		relativeY += dy;		
	}
	
	/**
	 * Change la direction dans laquelle regarde l'�quipe.
	 * 
	 * @param la nouvelle direction (Equipe.GAUCHE par exemple)
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
	public double getAbsolueX()
	{
		return absolueX;
	}

	/**
	 * Met � jour la position absolue (coordonn�es par rapport � la map) sur l'aye X de l'�quipe.
	 * 
	 * @param dx
	 * 		D�placement sur l'axe X
	 * 
	 */
	public void setAbsolueX(double dx) 
	{
		this.absolueX += dx;
	}
	
	
	/**
	 * Retourne la position absolue (par rapport � la map) de l'�quipe sur l'axe Y.
	 * 
	 * @return la position absolue (par rapport � la map) de l'�quipe sur l'axe Y.
	 */	
	public double getAbsolueY() 
	{
		return absolueY;
	}


	/**
	 * Met � jour la position absolue (coordonn�es par rapport � la map) sur l'aye Y de l'�quipe.
	 * 
	 * @param dy
	 * 		D�placement sur l'axe Y
	 */
	public void setAbsolueY(double dy) 
	{
		this.absolueY += dy;
	}

	
	/**
	 * Retourne le personnage � la position pass� en param�tre.
	 * 
	 * @param i
	 * 		Index du personnage � retourner.
	 * @return
	 * 		le personnage de l'�quipe � l'index i.
	 */
	public Personnage get(int i) 
	{
		return equipe[i];
		
	}	
	
	
	public void setValRelativeY(double i) 
	{
		this.relativeY = i;
		
	}



	public void setValRelativeX(int i) {
		this.relativeX = i;
		
	}


	/**
	 * Affiche sur la fen�tre un r�sum� de chaque personnage de l'�quipe (nom, xp, pv ...)
	 *  
	 * @param g
	 * 		Graphics
	 * @param selection
	 * 		Index du personnage s�lectionn� par le joueur.
	 */
	public void afficherListeEquipe(Graphics g, int selection)
	{
		for(int i=0;i<equipe.length;i++)
		{
			equipe[i].afficherGestionPerso(g,0,160*i,selection == i);
		}
		
	}


	/**
	 * Retourne le nombre de personnages dans l'�quipe.
	 * 
	 * @return
	 * 		Le nombre de personnage dans l'�qupe.
	 */
	public int nbPersonnages() 
	{
		int total = 0;
		for(Personnage p : equipe)
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
		Personnage temp = equipe[p0];
		equipe[p0] = equipe[pF];
		equipe[pF] = temp;
	}
	
	/**
	 * Retourne l'index du personnage pass� en param�tre ou -1 si le personnage n'est pas dans l'�quipe.
	 * @param personnage
	 * 		Personnage dont l'index est � retourner.
	 * @return
	 * 		l'index du personnage pass� en param�tre ou -1 si le personnage n'est pas dans l'�quipe.
	 */
	public int indexOf(Personnage personnage)
	{
		for(int i = 0; i < nbPersonnages(); i++)
		{
			if(personnage.equals(equipe[i]))
			{
				return i;
			}
		}
		return -1;
	}



	public int nombrePersonnagesVivants() 
	{
		int total = 0;
		for(Personnage p : equipe)
		{
			if(p.estVivant())
			{
				total++;
			}
		}
		return total;
	}
	
	public boolean estPrete()
	{
		for(Personnage p : equipe)
		{
			if(!p.estActionPreparee());
			{
				return false;
			}
		}
		return true;
	}



	public Object getPersonnageAlea() 
	{
		if(nombrePersonnagesVivants() == 0)
		{
			return null;
		}
		else
		{
			int a = (int) (Math.random() * nombrePersonnagesVivants());
			while (!equipe[a].estVivant())
			{
				a = (a + 1) % nbPersonnages();
				System.out.println("Test b2 oo : " + a + " - "  + !equipe[a].estVivant());
			}
			return equipe[a];
		}
	}


	/**
	 * Retourne vrai si l'�quipe a d�j� analys� l'ennemi dont l'id est pass� en param�tre, sinon faux.
	 * @param id
	 * 		L'id de l'ennemi.
	 * @return
	 * 		 vrai si l'�quipe a d�j� analys� l'ennemi dont l'id est pass� en param�tre, sinon faux.
	 */
	public boolean estAnalyse(String id) 
	{
		return recherches.contains(id);
	}

	/**
	 * Retourne le mage de l'�quipe ou null s'il n'y a pas de mage.
	 * 
	 * @return 
	 * 		le mage de l'�quipe ou null s'il n'y a pas de mage.
	 */
	public Mage getMage()
	{
		for(Personnage p : equipe)
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
	public Rodeur getRodeur()
	{		
		for(Personnage p : equipe)
		{
			if(p instanceof Rodeur)
			{
				return (Rodeur)p;
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
	public Guerrier getGuerrier()
	{		
		for(Personnage p : equipe)
		{
			if(p instanceof Guerrier)
			{
				return (Guerrier)p;
			}
		}
		return null;
	}
	
	public void ajouterObjet(IObjet objet,int qte)
	{
		sac.ajouter(objet, qte);
	}

	/**
	 * Ajoute un ennemi � la liste des ennemis analys�s.
	 * (Comp�tence "Recherche" du mage)
	 * 
	 * @param id
	 *		L'id de l'ennemi analys�.
	 */
	public void analyser(String id)
	{
		recherches.add(id);
	}


	public void setMap(Map map) 
	{
		this.map = map;
	}


	public void setValAbsolueX(double x)
	{
		absolueX = x;	
	}
	
	public void setValAbsolueY(double y)
	{
		absolueY = y;	
	
	}


	public void updatePO(int po)
	{
		argent += po;
	}
}
