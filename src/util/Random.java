package util;

public class Random {
	/**
	 * G�n�re un entier al�atoire entre les bornes incluses
	 * pass�es en param�tre.
	 * 
	 * @param min
	 * 		Borne minimale.
	 * @param max
	 * 		Borne maximale.
	 */
	public static int randInt(int min, int max){
		java.util.Random rand = new java.util.Random();
	    int randomNum = rand.nextInt((max - min) + 1) + min;
	    
	    return randomNum;
	}
}
