package game;

import game.launcher.Launcher;
import game.system.Configurations;
import game.system.KeyboardControlsConfigurations;
import game.system.application.Application;


import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;





public abstract class Top  extends BasicGameState
{
	private boolean console = false;
	private boolean directionKeyPressedOrDown = false;
	
	@Override
	public void render(GameContainer container, StateBasedGame sbg, Graphics g)
			throws SlickException 
	{
		if(console){
			Application.application().console().render();
		}
	}

	@Override
	public void update(GameContainer container, StateBasedGame arg1, int arg2)
			throws SlickException 
	{
		Input in = container.getInput();
		directionKeyPressedOrDown = false;
		if(in.isKeyPressed(Input.KEY_0)){
			toggleConsole();
		}
		if(console && Configurations.DEBUG){
			Application.application().console().setFocus(true);
			if(in.isKeyPressed(Input.KEY_ENTER)){
				Application.application().console().validate();
			}
			in.clearKeyPressedRecord();
		}
		if(in.isKeyPressed(KeyboardControlsConfigurations.VALIDATE_KEY)){
			onValidate();
		}
		if(in.isKeyPressed(KeyboardControlsConfigurations.BACK_KEY)){
			onBack();
		}
		//KEY PRESSED
		if(in.isKeyPressed(KeyboardControlsConfigurations.DOWN_KEY)){
			onDown();
			directionKeyPressedOrDown = true;
		}
		if(in.isKeyPressed(KeyboardControlsConfigurations.UP_KEY)){
			onUp();
			directionKeyPressedOrDown = true;
		}
		if(in.isKeyPressed(KeyboardControlsConfigurations.LEFT_KEY)){
			onLeft();
			directionKeyPressedOrDown = true;
		}
		if(in.isKeyPressed(KeyboardControlsConfigurations.RIGHT_KEY)){
			onRight();
			directionKeyPressedOrDown = true;
		}
		if(in.isKeyPressed(KeyboardControlsConfigurations.START_KEY)){
			onStart();
		}
		//KEY DOWN
		if(in.isKeyDown(KeyboardControlsConfigurations.DOWN_KEY) && !console){
			onHoldDown();
			directionKeyPressedOrDown = true;
		}
		if(in.isKeyDown(KeyboardControlsConfigurations.UP_KEY) && !console){
			onHoldUp();
			directionKeyPressedOrDown = true;
		}
		if(in.isKeyDown(KeyboardControlsConfigurations.LEFT_KEY) && !console){
			onHoldLeft();
			directionKeyPressedOrDown = true;
		}
		if(in.isKeyDown(KeyboardControlsConfigurations.RIGHT_KEY) && !console){
			onHoldRight();
			directionKeyPressedOrDown = true;
		}
		if(!directionKeyPressedOrDown){
			onNoDirectionKeyPressedOrDown();
		}
	}
	
	/**
	 * Affiche ou cache la console de d�buggage.
	 */
	private void toggleConsole(){
		console = !console;
		Application.application().console().setFocus(console);
	}

	//----------------------------------------------------------------
	//----------------------------EVENTS------------------------------
	//----------------------------------------------------------------
	
	
	/**
	 * Ev�nement "validate"
	 */
	public void onValidate(){}
	/**
	 * Ev�nement "back"
	 */
	public void onBack(){}
	/**
	 * Ev�nement "left"
	 */
	public void onLeft(){}
	/**
	 * Ev�nement "right"
	 */
	public void onRight(){}
	/**
	 * Ev�nement "up"
	 */
	public void onUp(){}
	/**
	 * Ev�nement "down"
	 */
	public void onDown(){}
	/**
	 * Ev�nement "hold left"
	 */
	public void onHoldLeft(){}
	/**
	 * Ev�nement "hold right"
	 */
	public void onHoldRight(){}
	/**
	 * Ev�nement "hold up"
	 */
	public void onHoldUp(){}
	/**
	 * Ev�nement "hold down"
	 */
	public void onHoldDown(){}
	/**
	 * Pas de direction.
	 */
	public void onNoDirectionKeyPressedOrDown(){}
	/**
	 * Ev�nement "start"
	 */
	public void onStart() {}
}
