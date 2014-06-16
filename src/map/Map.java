package map;


import game.Jeu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.newdawn.slick.Image;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

import donnees.GenerateurDonnees;
import personnage.Ennemi;
import personnage.Equipe;
import personnage.EquipeEnnemis;
import personnage.PNJ;
import audio.GestionnaireMusique;



// TODO Particules : pluie neige OU filtres / particles

/**
 * Une Map poss�de les carat�ristiques :
 * <ul>
 * <li>x;y : les coordon�es d'afffichage de la Map</li>
 * <li>map : la TiledMap associ�e � la map</li>
 * <li>xTh�orique;yTh�orique : Coordonn�es utilis�es pour le SAB</li>
 * </ul>
 * 
 * @author Darklev
 * 
 * @see TiledMap
 *
 */
public class Map 
{
	public static int LONG;
	public static int LARG;
	
	private double x;
	private double y;
	
	private double xTheorique;
	private double yTheorique;
	
	private String id;
	private TiledMap map;
	private Image terrain;
	private ArrayList<Portail> portails;
	private HashMap<Integer, AnimatedTileManager> tilesAnimes;
	
	private ArrayList<Coffre> coffres;
	
	private boolean exterieur;
	
	private boolean[][] collisions;
	
	private ArrayList<String> ennemis;
	
	private String musique;
	
	private ArrayList<PNJ> pnj;
	
	/**
	 * Constructeur de Map.
	 * 
	 * @param pathMap
	 * 		Le chemin qui pointe sur la tiledMap.
	 * @param pathTerrain
	 * 		Le chemin qui pointe sur l'image du terrain.
	 * @param x
	 * 		L'abscisse de l'affichage de la Map au d�part.
	 * @param y
	 * 		L'ordonn�e de l'affichage de la Map au d�part.
	 * @param ennemis
	 * 		Liste des groupe d'ennemis de la carte.
	 * 
	 * @throws SlickException
	 * @see SlickException
	 */
	public Map(String id, String pathMap, String pathTerrain, double x, double y, 
			String musique, boolean exterieur, ArrayList<String> ennemis, ArrayList<Portail> portails, ArrayList<Coffre> coffres, ArrayList<PNJ> pnj) throws SlickException
	{
		this.id = id;
		this.exterieur = exterieur;
		this.x=x;
		this.y=y;
		xTheorique=x;
		yTheorique=y;
		this.ennemis = ennemis;
		

		
		terrain = new Image(pathTerrain);
		map = new TiledMap(pathMap);
		this.portails = portails;
		
		this.coffres = coffres;
		
		this.musique = musique;
		
		
		this.pnj = pnj;
		
		tilesAnimes = new HashMap<Integer, AnimatedTileManager>();
		
		/*
		if(musique == null)
		{
			GestionnaireMusique.stopper();
		}
		else
		{
			GestionnaireMusique.jouerEnBoucle(musique);
		}
		*/
		
		//Correction coordonn�es initiales
		/*
		double margeX=0;
		
		if(x>0)
		{
			this.x=0;
		}
		
		if(y>0)
		{
			this.y=0;
		}
		
		if(x<LONG-map.getWidth()*32)
		{
			this.x=LONG-map.getWidth()*32;
		}
		
		if(y<LARG-map.getHeight()*32)
		{
			this.y=LARG-map.getHeight()*32;
		}
		*/
		//Fin correction coordon�es initiales
		
		
		//Propri�t�s des tiles:
		collisions = new boolean[map.getWidth()][map.getHeight()];
		for(int axeX=0; axeX<map.getWidth();axeX++)
		{
			for(int axeY=0; axeY<map.getHeight();axeY++)
			{
				collisions[axeX][axeY]=false;
				for(int i=0;i<map.getLayerCount();i++)
				{
					//current tile
					int tileID=map.getTileId((int) axeX,(int) axeY, i);
					
					/* Test collision */
					boolean collision = "true".equals(map.getTileProperty(tileID, "collision", "false"));
					boolean portail = "true".equals(map.getTileProperty(tileID, "portail", "false"));
					if(portail)
					{
						collisions[axeX][axeY] = false;
						break;
					}
					else if(collision)
					{
						collisions[axeX][axeY] = true;
					}
					
					/* Test animation de tiles */
					if(!tilesAnimes.containsKey(tileID))
					{
						AnimatedTileManager atm = FactoryAnimatedTileManager.make(map, tileID);
						if(atm != null)
						{
							tilesAnimes.put(tileID, atm);
						}
					}
				}
			}
		}
		//Fin proprietes tiles
		

		for(int axeX=0; axeX<map.getWidth();axeX++)
		{
			for(int axeY=0; axeY<map.getHeight();axeY++)
			{
				for(int i=0;i<map.getLayerCount();i++)
				{
					int tileID=map.getTileId((int) axeX,(int) axeY, i);
					if("true".equals(map.getTileProperty(tileID, "coffre", "false")))
					{
						if(Jeu.estCoffreOuvert(new Coffre(id, axeX, axeY)))
						{
							map.setTileId((int) axeX,(int) axeY, i, tileID+1);  //coffre ouvert
						}
					}
				}
			}
		}
		

	}
	
	/*
	public Map initialiser_map(String pathMap, String pathTerrain, double x, double y, String musique, boolean exterieur) throws SlickException
	{
		Map _map = new Map(pathMap,pathTerrain,x,y,musique,exterieur);
		
		return _map;
	}
	*/
	
	public static void init(int longEcran, int largEcran)
	{
		LONG=longEcran;
		LARG=largEcran;
	}
	
	
	/*  
	          ^
              |
              |
     <  X    >|
    +---------+
  ^	|         |
  Y	|   1     |
  V	|         |
----+---------+------+-------------------------+----------------->
		(0;0) |  3   |                         |
		      |      |                         |
		      +------+                         |
		      |             2                  |
		      |                                |
		      |                                |
		      +--------------------------------+
		      |
		      |				


1. L'ensemble des points (x;y) 
   sur les quels ont peut "poser" la map (coin sup�rieur gauche).
   
   DIMENSIONS : map.getWidth() - LONG   *     map.getHeight() - LARG
   
2. La map affich�e.

3. L'�cran.
	
	DIMENSIONS : LONG * LARG
	*/
	
	public Image getTerrain()
	{
		return terrain;
	}
	
	/**
	 * <h1>SAB</h1>
	 * 
	 * Met � jour l'abscisse de la Map. Si le joueur est trop haut ou trop bas ou trop haut
	 * pour d�placer la carte alors la m�thode renvoie le nombre de pixel n�cessaire pour d�placer
	 * le personnage/l'�quipe.
	 * 
	 * @param dx
	 * 		La valeur de deplacement sur l'axe X.
	 * @return
	 * 		La quantit� de pixel n�cessaire pour mettre � jour la position du personnage/de l'�quipe
	 */
	
	public double scrollX(double dx)
	{
		
		//TODO
		
		/*
        xTheorique -= dx;
       
        
        if(LONG-map.getWidth()*32>0)
        {
        	//x-= dx;
        	return dx;
        }
        
       
        
        if(xTheorique>0 )
        {
        	x=0;
            return dx;
        }
        
        if(xTheorique<LONG - map.getWidth()*32)
        {
        	x = LONG-map.getWidth()*32;
        	return dx;
        }
       
        x-=dx;
        if(x>0)
        {
            x=0;
            return dx;
        }
        else if(x<LONG-map.getWidth()*32)
        {
            x = LONG-map.getWidth()*32;
            return dx;
        }
        return 0;
        */
		
		x-=dx;
		
		return 0;
		
	}

	
	/**
	 * <h1>SAB</h1>
	 * 
	 * Met � jour l'ordonn�e de la Map. Si le joueur est trop haut ou trop bas ou trop haut
	 * pour d�placer la carte alors la m�thode renvoie le nombre de pixels n�cessaires pour d�placer
	 * le personnage/l'�quipe.
	 * 
	 * @param dy
	 * 		La valeur de deplacement sur l'aye Y.
	 * @return
	 * 		La quantit� de pixels n�cessaires pour mettre � jour la position du personnage/de l'�quipe
	 */		
	public double scrollY(double dy)
	{
		//TODO
		/*
        yTheorique -= dy;
        

        
        if(LARG-map.getHeight()*32>0)
        {
        	//y-= dy;
        	return dy;
        }
        
        if(yTheorique>0)
        {
        	y=0;
            return dy;
        }
        else if(yTheorique<LARG - map.getHeight()*32)
        {
        	y = LARG-map.getHeight()*32;
        	return dy;
        }
       
        y-=dy;
        if(y>0)
        {
            y=0;
            return dy;
        }
        else if(y<LARG-map.getHeight()*32)
        {
            y = LARG-map.getHeight()*32;
            return dy;
        }
        return 0;
        */
		y-=dy;
		return 0;
	}

	/**
	 * Retourne la hauteur de la carte en tiles.
	 * 
	 * @return
	 * 		la hauteur de la carte en tiles.
	 */
	public int getHeight() 
	{
		return map.getHeight();
	}

	/**
	 * Retourne la longueur de la carte en tiles.
	 * 
	 * @return
	 * 		la longueur de la carte en tiles.
	 */
	public int getWidth() 
	{
		return map.getWidth();
	}

	/**
	 * Retourne la TiledMap associ�e � la Map.
	 * 
	 * @return TiledMap associ�e � la Map.
	 */
	public TiledMap getTiledMap() 
	{
		return map;		
	}
	
	/**
	 * Afficher le calque de la map dont l'index est pass� en param�tre.
	 * @param layer
	 * 		L'index du calque � afficher (premier index = 0)		
	 */
	public void afficherLayer(int layer) 
	{
		map.render((int) x, (int) y, layer);
	}
	
	/**
	 * Met � jour les tiles anim�s.
	 */
	public void updateAnimatedTile()
	{
		for(int x=0;x<map.getWidth();x++)
		{
			for(int y=0;y<map.getWidth();y++)
			{
				for(int z=0;z<map.getLayerCount();z++)
				{
					for(Integer atm : tilesAnimes.keySet())
					{
						if(tilesAnimes.get(atm).containsTile(map.getTileId(x, y, z)))
						{
							map.setTileId(x, y, z, tilesAnimes.get(atm).next());
						}
					}
				}
			}
		}
	}
	
	public void afficherPNJ()
	{
		for(PNJ pnj : this.pnj)
		{
			if(pnj.enMouvement())
			{
				pnj.getAnimation().draw((float) x + pnj.getX()*32, (float) y + pnj.getY()*32);
			}
			else
			{
				pnj.getAnimation().getImage(2).draw((float) x + pnj.getX()*32, (float) y + pnj.getY()*32);
			}
		}
	}
	
	/**
	 * Retourne vrai si la carte est � l'ext�rieur,
	 * sinon retourne faux.
	 * 
	 * @return
	 * 		Vrais si la carte est � l'ext�rieur,
	 * 		sinon faux.
	 */
	public boolean getExterieur()
	{
		return exterieur;
	}
	
	/**
	 * Retourne vrai si la tile dont les coordonn�es sont en param�tre
	 * est un obstacle, sinon retourne faux.
	 * 
	 * @param xC
	 * 		l'abscisse du tile.
	 * @param yC
	 * 		l'ordonn�e du tile.
	 * @return
	 *	 	vrai si la tile dont les coordonn�es sont en param�tre
	 *		est un obstacle, sinon retourne faux.
	 */
	private boolean estObstacle(int xC, int yC)
	{
		//System.out.println(xC+"  "+yC);
		try
		{
			boolean perso = false;
			for(PNJ p : pnj)
			{
				perso = p.getX() == xC && p.getY() == yC;
				if(perso) break;
			}
			return collisions[xC][yC] || perso;
		}
		catch(IndexOutOfBoundsException e)
		{
			return false;
		}
	}
	
	/*
	public boolean mouvementAutorise(int x0, int y0, int width, int height, int dx, int dy)
	{
		boolean autorise = true;


		
		//width/=2;
		//height/=2;
		
		
		//Droite
		if(dx>0)
		{
			autorise &= !estObstacle((int) (((x0+width+dx)/32)-1), (int) ((y0/32))) && !estObstacle((int) (((x0+width+dx)/32)-1), (int) (((y0+16)/32)));
		}
		//Gauche
		else if(dx<0)
		{
			autorise &= !estObstacle((int) (((x0+dx)/32)-1), (int) ((y0/32))) && !estObstacle((int) (((x0+dx)/32)), (int) (((y0+16)/32)));
		}

		
		//Bas
		if(dy>0)
		{
			autorise &= !estObstacle((int) (((x0)/32)), (int) ((y0+height+dy-16)/32)) && !estObstacle((int) (((x0+width)/32-0.5)), (int) ((y0+height+dy-16)/32));
		}
		
		//Haut
		else if(dy<0)
		{
			autorise &= !estObstacle((int) (((x0)/32)+0.2), (int) ((y0+dy)/32)) && !estObstacle((int) (((x0+width)/32)-0.5), (int) ((y0+dy)/32));
		}
		return autorise;
	}
	*/
	
	/**
	 * Retourne la distance que peut parcourir le joueur
	 * avant de rencontrer un obstacle.
	 * 
	 * @return
	 * 		la distance pouvant �tre parcourue avec de rencontrer un obstacle.
	 */
	public int distance(int x0, int y0, int width, int height, int dx, int dy)
	{
		int i = 0;
		
		//Droite
		if(dx>0)
		{
			for(i=0; i<dx;i++)
			{
				if(estObstacle((int)(x0+i+15)/32,(int)(y0+height-19)/32))
				{
					break;					
				}
			}
		}
		
		//Gauche
		if(dx<0)
		{
			for(i=0; i>dx;i--)
			{
				if(estObstacle((int)(x0+i-16)/32,(int)(y0+height-19)/32))
				{
					break;					
				}
			}
		}
		
		//Bas
		if(dy>0)
		{
			for(i=0; i<dy;i++)
			{
				if(estObstacle((int)(x0)/32,(int)(y0+15+i)/32) || estObstacle((int)(x0-15)/32,(int)(y0+15+i)/32))
				{
					break;					
				}
			}
		}
		
		//haut
		if(dy<0)
		{
			for(i=0; i>dy;i--)
			{
				if(estObstacle((int)(x0)/32,(int)(y0+height+i-20)/32) || estObstacle((int)(x0-15)/32,(int)(y0+height+i-20)/32))
				{
					break;					
				}
			}
		}
		return i;
	}
	
	/**
	 * G�n�re un groupe d'ennemi pr�sent sur la maps selon l'indice de raret� des groupes.
	 * Retourne null si aucun n'ennemi n'est g�n�r�.
	 * 
	 * @return
	 * 		Groupe d'ennemis g�n�r�s ou null.
	 */
	public EquipeEnnemis genererEnnemis()
	{
		if(ennemis.size()>0)
		{
			double r = Math.random();
			if(r>0.99)
			{			
				return GenerateurDonnees.genererGroupeEnnemis(ennemis.get(0));
			}
		}
		
		return null;
		
	}
	
	public Portail getPortail(int tilex, int tiley)
	{		
		for(Portail p : portails)
		{
			if(tilex == p.getX() && tiley == p.getY())
			{
				return p;
			}
		}
		return null;
	}

	public Coffre getCoffre(int tilex, int tiley) 
	{
		System.out.println("TEST COFFRE "+ tilex + "  -   " + tiley);
		for(Coffre c : coffres)
		{
			if(tilex == c.getX() && tiley == c.getY())
			{
				return c;
			}
		}
		return null;
	}
	
	public void updateCoffreSprite(int x , int y)
	{
		
		
		for(int i = 0 ; i<map.getLayerCount(); i++)
		{
			int tileID = map.getTileId(x,y,i);
			
			if("true".equals(map.getTileProperty(tileID, "coffre", "false")))
			{
				map.setTileId(x,y, i, tileID+1);  //coffre ouvert
				
				System.out.println(map.getTileId(x,y,i));
			}
		}

	}

	/**
	 * Retourne le nom de la musique de la map.
	 */
	public String getMusic()
	{
		return musique;
	}

	public PNJ getPNJ(int x, int y) 
	{
		for(PNJ p : pnj)
		{
			if(p.getX() == x && p.getY() == y)
			{
				return p;
			}
		}
		return null;
	}

	public String getId() {	
		return id;
	}
}
