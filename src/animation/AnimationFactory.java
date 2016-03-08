package animation;

import game.BattleWithATB;
import game.battle.IBattle;
import game.system.application.Application;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Point;

import characters.Character;
import animation.action.FireBallAnimation;
import bag.item.stuff.Weapon;
import data.DataManager;



/**
 * Classe permettant d'instancier les Animations pour le combat.
 * 
 * @author Darklev
 *
 */
public class AnimationFactory 
{
	
	/**
	 * Retourne une Animation pour le combat en fonction de l'id pass� en param�tre (id d'un skill/objet, "attaquer", "d�fense" ...).
	 * 
	 * @param id
	 * 		L'id de l'animation � retourner.
	 * @returnk
	 * 		L'animation correspondant � l'id.
	 */
	public static BattleAnimation createAnimation(IBattle caster, ArrayList<IBattle> targets)
	{
		return new FireBallAnimation(caster, targets);
	}
}
