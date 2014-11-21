package ui.ListRenderer;

public interface ElementRenderer {

	/**
	 * Afficher un �l�ment d'une List aux coordonn�es sp�cifi�es.
	 * 
	 * @param x
	 * 		Abscisse du curseur � afficher.
	 * @param y
	 * 		Ordonn�es du curseur � afficher.
	 * @param element
	 * 		Element courant � afficher
	 * @param index
	 * 		index de l'�l�ment courant � afficher.
	 */
	public void render(int x, int y, Object element, int index);
}
