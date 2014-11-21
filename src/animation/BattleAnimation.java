package animation;

import game.Combat;

import java.awt.Point;
import java.util.ArrayList;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import personnage.IBattle;

/**
 * Animation pour les combats.
 * 
 * @author Darklev
 *
 */
public class BattleAnimation 
{
	private int frame;
	private IAnimation animation;
	
	/**
	 * Les instances d'Animation sont cr��es � partir de 
	 * la classe AnimationFactory.
	 */
	protected BattleAnimation(IAnimation animation)
	{
		this.animation = animation;
		frame = 0;
	}
	
	/**
	 * Joue la frame (affichage et son) en cours de l'Animation 
	 * 
	 * @param g
	 * 		Grahics � utiliser
	 * @param casterPosition
	 * 		Coordonn�es du lanceur
	 * @param targetsPositions
	 * 		Coordonn�es des cibles
	 * @param caster
	 * 		Le lanceur
	 * @param targets
	 * 		Les cibles
	 * @param battle
	 * 		Combat en cours.
	 * @return
	 * 		Vrai si l'animation est termin�e, faux sinon.
	 * 
	 * @throws SlickException
	 */
	public boolean playFrame(Graphics g, Point casterPosition, ArrayList<Point> targetsPositions, 
			IBattle caster, ArrayList<IBattle> targets , Combat battle) throws SlickException
	{
		boolean res =  animation.playFrame(g, casterPosition, targetsPositions, caster, targets, battle, frame);
		frame ++;
		if(res)
			frame = 0;
		return res;
	}
}
