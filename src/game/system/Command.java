package game.system;

import game.Config;
import game.Exploration;
import game.dialogue.Dialogue;
import game.system.application.Application;

import java.awt.Dialog;
import java.util.ArrayList;

import personnage.EnnemisParty;

import data.DataManager;

import util.Regex;

public class Command {
	
	
	private String command;
	private String[] parameters;
	
	/**
	 * Constructuer
	 * 
	 * @param command
	 * 		La ligne de commande
	 * @throws CommandParseException
	 * 		Si une mauvaise syntaxe
	 */
	public Command(String command) throws CommandParseException{
		String[] elements = parse(command);
		if(elements.length == 1){
			this.command = elements[0];
			parameters = new String[0];
		}
		else if(elements.length > 0){
			this.command = elements[0];
			parameters = new String[elements.length-1];
			for(int i=1; i<elements.length;i++){
				parameters[i-1] = elements[i];
			}
		}
	}
	
	/**
	 * Ananlyse une cha�ne de caract�re.
	 * 
	 * @param line
	 * 		La ligne de caract�re � analyser.
	 * @throws CommandParseException 
	 * 		Si mauvaise syntaxe.
	 */
	public String[] parse(String line) throws CommandParseException{
		String res[] = line.trim().split(" ");
		String quote = "";
		boolean findQuote = false;
		ArrayList<String> parse = new ArrayList<String>();
		if(res.length == 0){
			throw new CommandParseException("Empty command");
		}
		for(String s : res){
			if(!s.contains("\"")){ //pas de quote
				if(findQuote){
					quote += s + " ";
				}
				else{
					parse.add(s);
				}
			}
			else if(s.matches("$\"(.*)\"^")){ //quotes sans espace
				parse.add(s);
			}
			else if(s.matches("^\".*") && !findQuote){ // Une quote detect�
				quote += s.replace("\"","") + " ";
				findQuote = true;
			}
			else if(s.matches(".*\"$") && findQuote){ //Fin de quote
				quote += s.replace("\"","");
				findQuote = false;
				parse.add(quote);
				quote="";
			}
		}
		return parse.toArray(new String[parse.size()]);
	}
	
	/**
	 * Execute la commande
	 */
	public String execute(){
		if(command.equalsIgnoreCase("dialogue")){
			return createDialogue();
		}
		else if(command.equalsIgnoreCase("toggletile")){
			return toggleTile();
		}
		else if(command.equalsIgnoreCase("tileid")){
			return tileId();
		}
		else if(command.equalsIgnoreCase("battle")){
			return battle();
		}
		return "Command does not exist";
	}
	
	
	//---------------------------------------------------------
	private String createDialogue(){
		if(parameters.length == 0 || parameters.length > 1){
			return "dialogue <id>";
		}
		else{
			if(Application.application().getGame().getCurrentStateID() == Config.EXPLORATION){
				Dialogue dialogue = DataManager.loadDialogue(parameters[0]);
				if(dialogue == null){
					return "Dialogue not found (wrong id)";
				}
				else{
					((Exploration)Application.application().getGame().getState(Config.EXPLORATION)).setDialogue(dialogue);
					return "Dialogue is loaded.";
				}
			}
			else{
				return "State should be exploration.";
			}
		}
	}
	
	
	private String toggleTile(){
		if(parameters.length != 3){
			return "toggletile <x> <y> <z>";
		}
		else{
			if(Application.application().getGame().getCurrentStateID() == Config.EXPLORATION){
				if(!parameters[0].matches("^[0-9]*$") || !parameters[1].matches("^[0-9]*$") || !parameters[2].matches("^[0-9]*$")){
					return "Parameters should be integer.";
				}
				else{
					int x = Integer.parseInt(parameters[0]);
					int y = Integer.parseInt(parameters[1]);
					int z = Integer.parseInt(parameters[2]);
					
					Application.application().getGame().getParty().getMap().toggleTile(x, y, z);
					
					return "Tile has been toggled";
				}
			}
			else{
				return "State should be exploration.";
			}
		}
	}
	
	private String tileId(){
		if(parameters.length != 3){
			return "toggletile <x> <y> <z>";
		}
		else{
			if(Application.application().getGame().getCurrentStateID() == Config.EXPLORATION){
				if(!parameters[0].matches("^[0-9]*$") || !parameters[1].matches("^[0-9]*$") || !parameters[2].matches("^[0-9]*$")){
					return "Parameters should be integer.";
				}
				else{
					int x = Integer.parseInt(parameters[0]);
					int y = Integer.parseInt(parameters[1]);
					int z = Integer.parseInt(parameters[2]);
					
					int currentTileId = Application.application().getGame().getParty().getMap().getCurrentTileId(x, y, z);
					int originalTileId = Application.application().getGame().getParty().getMap().getOriginalTileId(x, y, z);
					
					return "current:"+currentTileId+" orginal:"+originalTileId;
				}
			}
			else{
				return "State should be exploration.";
			}
		}
	}
	
	private String battle(){
		if(parameters.length != 1){
			return "battle <id>";
		}
		else{
			if(Application.application().getGame().getCurrentStateID() == Config.EXPLORATION){
				String id = parameters[0];
				EnnemisParty ennemis = DataManager.loadEnnemisParty(id);
				if(ennemis  == null){
					return "Ennemis party not found (wrong id)";
				}
				Application.application().getGame().launchBattle(ennemis);
				return "Battle launched !";
			}
			else{
				return "State should be exploration.";
			}
		}
	}
}