package game.dialogue;

import game.settings.Settings;
import game.system.Configurations;
import game.system.KeyboardControlsConfigurations;
import game.system.application.Application;

import java.util.ArrayList;

import org.newdawn.slick.Input;

import characters.Character;
import ui.Panel;
import util.Regex;

/**
 * Classe permettant de cr�er un dialogue.
 * 
 * @author Darklev
 *
 */
public class Dialogue implements Cloneable
{
	private ArrayList<Line> dialogue;
	private int index;
	private Select select;
	private boolean notificationEnd = false; //vaut true si $end est rencontr� dans la r�plique en cours.
	
	private int time = 0;
	
	public Dialogue(ArrayList<Line> dialogue)
	{
		this.dialogue = dialogue;
		index = -1;
	}
	
	/**
	 * Passe � la r�plique suivante.
	 * @return
	 * 	Vrai s'il y a une r�plique suivante ou faux s'il n'y en a pas ou une notification
	 * end a �t� rencotr�e.
	 */
	public boolean next()
	{
		time = 0;
		index++;
		if(index >= dialogue.size())
		{
			return false;
		}
		while(dialogue.get(index).getLine().matches(".*\\$if\\[.*\\].*"))
		{
			String line = dialogue.get(index).getLine();
			String condition = Regex.eval(line, ".*\\$if\\[(.*)\\].*")[1];
			if(!condition(condition))
			{
				index++;
			}
			else
			{
				break;
			}
		}

		//Si notifEnd vaut true alors mettre fin au dialogue
		if(notificationEnd == true){
			notificationEnd = false;
			return false;
		}
		
		if(dialogue.get(index).getLine().contains("\\$end"))
		{
			notificationEnd = true;
		}
		else{
			notificationEnd = false;
		}
		return index < dialogue.size();
	}
	
	/**
	 * Analyse une condition pos�e dans une r�plique et retourne vraie
	 * si la condition est vraie, faux sinon.
	 * @param condition
	 * @return
	 * 		Vrai si la condition est vraie, faux sinon.
	 */
	private boolean condition(String condition)
	{
		boolean res = false;
		String[] eval = Regex.eval(condition,"([^>|<]*)(=|>=?|<=?)(.*)|(.*)"); 
		String[] membres = new String[2];
		String operator = null;
		if(eval.length > 1)
		{
			membres[0] = eval[1];
			
			if(condition.matches("([^>|<]*)(=|>=?|<=?)(.*)|(.*)"))
			{
				operator = eval[2];
				membres[1] = eval[3];
			}
		}
		
		
		Character character0 = null;
		Character character1 = null;
		for(String m : membres){
			if(m.matches("^equipe(\\d+)$")){
				character0 = Application.application().getGame().getParty().get(Integer.parseInt(Regex.eval(m, "^equipe(\\d+)$")[1]));
			}
			else if(m.equalsIgnoreCase("mage")){
				character1 = Application.application().getGame().getParty().getMage();
			}
			else if(m.equalsIgnoreCase("rodeur")){
				character1 = Application.application().getGame().getParty().getRanger();
			}
			else if(m.equals("guerrier")){
				character1 = Application.application().getGame().getParty().getWarrior();
			}
		}
		
		if(character0 != null && character1 != null)
		{
			if(operator.equals("=")){
				res = character0 == character1;
			}
		}
		
		return res;
	}
	
	/**
	 * Retoune la r�plqiue courrante.
	 * 
	 * @return
	 * 	La r�plique courante
	 */
	public Line get()
	{
		if(dialogue.get(index).getLine().matches(".*\\$select\\[.*\\].*")) //cr�ation du select.
		{
			String rep = dialogue.get(index).getLine();
			select = createSelect(rep.substring(rep.indexOf("[",rep.indexOf("$select"))+1, rep.indexOf("]", rep.indexOf("$select"))));
		}
		return dialogue.get(index);
	}
	
	/**
	 * Retourne le select courant.
	 * @return
	 * 	Le select courant ou null s'il n'y en a pas.
	 */
	public Select getSelect()
	{
		return select;
	}
	
	/**
	 * Traduit une cha�ne de caract�res de type [choix1; choix2{1}] en selectionneur.
	 * 
	 * @param syntaxe
	 * 		Cha�ne de caract�res de type [choix1; choix2{n}] .
	 * 		
	 * @return
	 * 		Le s�lectionneur correspondant � la cha�ne de caract�res pass�e en param�tre.
	 */
	private Select createSelect(String syntaxe)
	{
		ArrayList<String> choice = new ArrayList<String>();
		ArrayList<Integer> goTo = new ArrayList<Integer>();
		
		for(String c : syntaxe.split(";"))
		{
			if(c.matches(".*\\{([0-9]*|end)\\}"))
			{
				int n = 0;
				try
				{
					n = Integer.parseInt(c.substring((c.indexOf("{")+1),c.indexOf("}")));
				}
				catch (NumberFormatException e)
				{
					n = dialogue.size(); //end
				}
				goTo.add(n);
			}
			else
			{
				goTo.add(index + 1);
			}
			
			choice.add(c.replaceAll("\\{([0-9]*|end)\\}", ""));
		}
		
		return new Select(choice, goTo);
	}
	
	/**
	 * Valide le s�lecteur. Ex�cute le goto associer au Select � l'index passer en param�tre.
	 * Le select n'existe plus.
	 * @param i
	 * 		Index � valider.
	 */
	public void validateSelect(int i)
	{
		if(select != null)
		{
			index = select.getGoTo(i);
			select = null;
		}
	}
	
	/**
	 * Retourne vrai si le dialogue est fini. Faux sinon.
	 * @return
	 * 		vrai si le dialogue est fini. Faux sinon.
	 */
	public boolean isEnd()
	{
		return index >= dialogue.size();
	}
	
	/**
	 * Clone le dialogue.
	 */
	@Override
	public Dialogue clone()
	{
		try 
		{
			return (Dialogue) super.clone();
		} 
		catch (CloneNotSupportedException e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Affiche le dialogue.
	 */
	public void render(){
		int width = Configurations.SCREEN_WIDTH;
		int height = 90;
		int x = 0;
		int y = Configurations.SCREEN_HEIGHT - height;
		
		Panel dialoguePanel = new Panel(width, height, 2, Settings.BACKGROUND_COLOR, Settings.BORDER_COLOR);
		dialoguePanel.render(x, y);
		Application.application().drawString((get().getSpeaker() == null ? "" :  get().getSpeaker()+":\n") + get().getLine().substring(0,(int)((float)time/30f)+1), x + 10, y + 5);
	}
	
	/**
	 * Met � jour le dialogue.
	 * 
	 * @param delta
	 * 		Temps depuis la derni�re mise � jour en millisecondes.
	 */
	public void update(int delta){
		int maxTime = (get().getLine().length()-1)*30;
		time = Math.min(time + delta, maxTime);
		Input in = Application.application().getContainer().getInput();
		if(in.isKeyPressed(KeyboardControlsConfigurations.VALIDATE_KEY)){
			if(time == maxTime){
				next();
			}
			else{
				time = maxTime;
			}
		}
	}
}
