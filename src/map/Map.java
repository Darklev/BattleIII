package map;



import game.system.application.Application;

import java.util.ArrayList;
import java.util.HashMap;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.tiled.TiledMap;

import util.PolygonFactory;
import characters.EnnemisParty;
import characters.NonPlayerCharacter;
import characters.Party;
import data.DataManager;


/**
 * 
 * Classe repr�sentant une Map.
 * 
 * @author Darklev
 *
 */
public class Map 
{
	//largeur et longueur affich�s de la map.
	public static int WIDTH;
	public static int HEIGHT;
	
	//coordonn�es d'affichage
	private double x;
	private double y;
	
	//Id de la map
	private String id;
	private String name;
	private TiledMap map;
	private Hitbox hitbox;
	private Image backgroundBattle;
	private ArrayList<Gate> gates;
	private HashMap<Integer, AnimatedTileManager> animatedTiles;
	
	private ArrayList<Chest> chests;
	
	private boolean outDoor;
	

	private int[][][] originalTilesId;
	
	private ArrayList<String> ennemis;
	
	private String music;
	
	private ArrayList<NonPlayerCharacter> NPC;
	
	private ArrayList<Command> commands;
	
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
	public Map(String id, String nom, String pathMap, String pathTerrain, double x, double y, 
			String musique, boolean exterieur, ArrayList<String> ennemis, ArrayList<Gate> portails, ArrayList<Chest> coffres, ArrayList<NonPlayerCharacter> pnj, ArrayList<Command> commandes, boolean load) throws SlickException
	{
		this.id = id;
		this.name = nom;
		this.outDoor = exterieur;
		this.x=x;
		this.y=y;
		this.ennemis = ennemis;
		this.commands = commandes;

		
		backgroundBattle = new Image(pathTerrain);
		if(load)
		{
			map = new TiledMap(pathMap);
			calculateHitbox();
			animatedTiles = new HashMap<Integer, AnimatedTileManager>();
			
			//Propri�t�s des tiles:
			
			originalTilesId = new int[map.getWidth()][map.getHeight()][map.getLayerCount()];
			for(int axeX=0; axeX<map.getWidth();axeX++)
			{
				for(int axeY=0; axeY<map.getHeight();axeY++)
				{

					for(int i=0;i<map.getLayerCount();i++)
					{
						int tileID = map.getTileId((int) axeX,(int) axeY, i);
						originalTilesId[axeX][axeY][i] = tileID;

						
						/* Test animation de tiles */
						if(!animatedTiles.containsKey(tileID))
						{
							AnimatedTileManager atm = FactoryAnimatedTileManager.make(map, tileID);
							if(atm != null)
							{
								animatedTiles.put(tileID, atm);
							}
						}
					}
				}
				
				//init commandes
				for(Command  c : commandes){
					for(Interact i : c.getInteracts()){
						i.setTileId(map.getTileId(i.getX(), i.getY(), i.getZ()));
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
							if(Application.application().getGame().isChestOpened(new Chest(id, axeX, axeY)))
							{
								map.setTileId((int) axeX,(int) axeY, i, tileID+1);  //coffre ouvert
							}
						}
					}
				}
			}
		}
		
		this.gates = portails;
		
		this.chests = coffres;
		
		this.music = musique;
		
		
		this.NPC = pnj;
		
		
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

	}
	
	/*
	public Map initialiser_map(String pathMap, String pathTerrain, double x, double y, String musique, boolean exterieur) throws SlickException
	{
		Map _map = new Map(pathMap,pathTerrain,x,y,musique,exterieur);
		
		return _map;
	}
	*/
	
	/**
	 * Initialisation de la map.
	 * @param width
	 * @param height
	 */
	public static void init(int width, int height)
	{
		WIDTH = width;
		HEIGHT = height;
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
	
	public Image getBackgroundBattle()
	{
		return backgroundBattle;
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
		hitbox.moveX(-dx);
		
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
		hitbox.moveY(-dy);
		
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
	 * Retourne l'abscisse de l'affichage de la map.
	 * @return l'abscisse de la map
	 */
	public double getX(){
		return x;
	}
	
	/**
	 * Retourne l'ordonn�e de l'affichage de la map.
	 * @return l'ordonn�e de la map.
	 */
	public double getY(){
		return y;
	}
	
	/**
	 * Afficher le calque de la map dont l'index est pass� en param�tre.
	 * @param layer
	 * 		L'index du calque � afficher (premier index = 0)		
	 */
	public void drawLayer(int layer) 
	{
		map.render((int) x, (int) y, layer);
	}
	
	/**
	 * Met � jour les tiles anim�s.
	 */
	public void updateAnimatedTiles(int delta)
	{
		for(AnimatedTileManager atm : animatedTiles.values()){
			atm.update(delta);
		}
		for(int x=0;x<map.getWidth();x++)
		{
			for(int y=0;y<map.getHeight();y++)
			{
				for(int z=0;z<map.getLayerCount();z++)
				{
					for(Integer atm : animatedTiles.keySet())
					{
						if(animatedTiles.get(atm).containsTile(map.getTileId(x, y, z)))
						{
							map.setTileId(x, y, z, animatedTiles.get(atm).getCurrentID());
						}
					}
				}
			}
		}
	}
	
	/**
	 * Affiche les personnages non joueurs
	 */
	public void renderNPC()
	{
		for(NonPlayerCharacter pnj : this.NPC)
		{
			if(pnj.isMoving())
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
	 * Affiche les personnages joueurs
	 */
	public void renderOtherPlayers()
	{
		for(Party p : Application.application().getConnector().getPlayers().values())
		{
			if(p.getMap().id.equals(this.id)){
				p.draw(0.5f, true);
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
		return outDoor;
	}
	

	
	public void calculateHitbox(){
		hitbox = new Hitbox();
		for(int axeX=0; axeX<map.getWidth();axeX++)
		{
			for(int axeY=0; axeY<map.getHeight();axeY++)
			{
				for(int i=0;i<map.getLayerCount();i++)
				{
					int tileID = map.getTileId((int) axeX,(int) axeY, i);
					/* Test collision */
					boolean collision = "true".equalsIgnoreCase(map.getTileProperty(tileID, "collision", "false"));
					if(collision)
					{
						hitbox.addShape(PolygonFactory.createRectangle((float)(axeX*map.getTileWidth() + x), (float) (axeY*map.getTileHeight() + y), map.getTileWidth(), map.getTileHeight()));
					}
				}
			}
		}
	}
	
	public void drawHitbox(Color color) {
		hitbox.draw(color);
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
	 * G�n�re un groupe d'ennemi pr�sent sur la maps selon l'indice de raret� des groupes.
	 * Retourne null si aucun n'ennemi n'est g�n�r�.
	 * 
	 * @return
	 * 		Groupe d'ennemis g�n�r�s ou null.
	 * @throws SlickException 
	 */
	public EnnemisParty generateEnnemis() throws SlickException
	{
		if(ennemis.size()>0)
		{
			double r = Math.random();
			if(r>0.99)
			{			
				return DataManager.loadEnnemisParty(ennemis.get(0));
			}
		}
		
		return null;
		
	}
	
	/**
	 * Retourne le portail aux coordonn�es sp�cifi�es.
	 * Retourne null s'il n'y a pas de portail � cet endroit.
	 * 
	 * @param x
	 * 		Abscisse du portail. (en tile)
	 * @param y
	 * 		ordonn�e du portail. (en tile)
	 * @return
	 * 		Portail aux coordonn�es en param�tre, null s'il n'y a pas de portail.
	 */
	public Gate getGate(int x, int y)
	{		
		for(Gate p : gates)
		{
			if(x == p.getX() && y == p.getY())
			{
				return p;
			}
		}
		return null;
	}

	/**
	 * Retourne le coffre aux coordonn�es sp�cifi�es.
	 * Retourne null s'il n'y a pas de portail � cet endroit.
	 * 
	 * @param x
	 * 		Abscisse du coffre. (en tile)
	 * @param y
	 * 		ordonn�e du coffre. (en tile)
	 * @return
	 * 		Portail aux coordonn�es en param�tre, null s'il n'y a pas de coffre.
	 */
	public Chest getChest(int x, int y) 
	{
		for(Chest c : chests)
		{
			if(x == c.getX() && y == c.getY())
			{
				return c;
			}
		}
		return null;
	}
	
	/**
	 * Met � jour les sprites des coffres.
	 * (Si le coffre est ouvert, affiche le sprite de coffre ouvert).
	 * @param x
	 * 		Abscisse du coffre � mettre � jour.
	 * @param y
	 * 		Ordonn�es du coffre � mettre � jour.
	 */
	public void updateChestSprite(int x , int y)
	{
		
		
		for(int i = 0 ; i<map.getLayerCount(); i++)
		{
			int tileID = map.getTileId(x,y,i);
			
			if("true".equals(map.getTileProperty(tileID, "coffre", "false")))
			{
				map.setTileId(x,y, i, tileID+1);  //coffre ouvert
			}
		}

	}

	/**
	 * Retourne le nom de la musique de la map.
	 */
	public String getMusic()
	{
		return music;
	}

	/**
	 * R�cup�re le PNJ(NPC) aux coordonn�es pass�es en param�tre.
	 * Renvoie null s'il n'y a pas de PNJ � ces coordonn�es.
	 * 
	 * @param x
	 * 		Abscisse du PNJ(NPC).
	 * @param y
	 * 		Ordonn�es du PNJ(NPC).
	 * @return
	 * 		Le PNJ(NPC) aux coordonn�es pas�es en param�tre ou null s'il n'y a pas de PNJ � ces coordonn�es.
	 */
	public NonPlayerCharacter getNPC(int x, int y) 
	{
		for(NonPlayerCharacter p : NPC)
		{
			if(p.getX() == x && p.getY() == y)
			{
				return p;
			}
		}
		return null;
	}

	/**
	 * Retourne l'id de la map.
	 * 
	 * @return l'id de la map.
	 */
	public String getId() {	
		return id;
	}

	/**
	 * Retourne le nom de la map.
	 * @return le nom de la map.
	 */
	public String getName() {
		return name;
	}

	/**
	 * R�cup�re la commande aux coordonn�es sp�cifi�es.
	 * @param x
	 * 		Abscisse de la commande.
	 * @param y
	 * 		ordonn�e de la commande.
	 * @return
	 * 		La commande aux coordonn�es pass�es en param�tre ou null s'il n'y a
	 * pas de commande � ces coordonn�es.
	 */
	public Command getCommande(int x, int y) {
		for(Command c : commands){
			if(c.getX() == x && c.getY() == y){
				return c;
			}
		}
		return null;
	}
	

	
	/**
	 * Supprime ou r�affiche une tile dont les coordonn�es sont pass�es
	 * en param�tre.
	 * 
	 * @param x
	 * 		Abscisse de la tile cible.
	 * @param y
	 * 		Ordonn�es de la tile cible.
	 * @param z
	 * 		Couche de la tile cible.
	 */
	public void toggleTile(int x, int y, int z){
		if(map.getTileId(x, y, z) == 0){
			map.setTileId(x, y, z, originalTilesId[x][y][z]);
		}
		else{
			map.setTileId(x, y, z, 0);
		}
	}
	
	/**
	 * Retourne l'id de la tile dont les coordonn�es sont pass�es en 
	 * pram�tres.
	 * 
	 * @param x
	 * 		Abscisse de la tile cible.
	 * @param y
	 * 		Ordonn�es de la tile cible.
	 * @param z
	 * 		Couche de la tile cible.
	 * 
	 * @return l'id de la tile dont les coordonn�es sont pass�es en param�tres.
	 */
	public int getCurrentTileId(int x, int y, int z){
		return map.getTileId(x, y, z);
	}
	
	/**
	 * Retourne l'id de la tile de base (avant les modifications 
	 * comme "toggle" ...) dont les coordonn�es sont pass�es en 
	 * pram�tres.
	 * 
	 * @param x
	 * 		Abscisse de la tile cible.
	 * @param y
	 * 		Ordonn�es de la tile cible.
	 * @param z
	 * 		Couche de la tile cible.
	 * 
	 * @return l'id de la tile de base dont les coordonn�es sont pass�es en param�tres.
	 */
	public int getOriginalTileId(int x, int y, int z){
		return originalTilesId[x][y][z];
	}

	public Hitbox getHitbox() {
		return hitbox;
	}


}
