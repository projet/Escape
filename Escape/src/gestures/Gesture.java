package gestures;

import entities.ships.Player;
import entities.ships.Ship;
import fr.umlv.zen2.MotionEvent;
import fr.umlv.zen2.MotionEvent.Kind;
import gestures.filters.Backoff;
import gestures.filters.Drift;
import gestures.filters.Filter;
import gestures.filters.Looping;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import org.jbox2d.common.Vec2;


/*
 * LF:
 * TODO: JAVADOC
 * TODO: Am�liorer lisibilit� !!
 * 
 * QB:
 * DO : Lisibilite & debut javadoc
 * TODO : Probleme de gravite quand on trace une nouvelle courbe apre une reussie -> le vaisseau tombe, et ne continu pas l'ancienne trajectoire
 *
 *	
 * TODO : Faire un setDebug pour afficher les courbes approximer ou non :) -> pour demain
 * //QB: Pa mal , mai fo voir pour deplacer limage mnt selon un angle en fonction(pour le loop surtout)
 * Gerer l'acceleration
 */


public class Gesture {

	private TraceStack traceStack;
	private List<Filter> filters;
	private final Player controlledShip;
	

	public Gesture(Player controlledShip){
		this.controlledShip = controlledShip;
		traceStack = new TraceStack();
		filters = initFilters();
	}

	/*
	 * Pourait être implémenté dans une factory...
	 * Il faut trouver un moyen d'initialier la liste
	 */
	public List <Filter> initFilters(){
		List<Filter> filtersList = new ArrayList<>();
		filtersList.add(new Backoff());
		filtersList.add(new Drift());
		filtersList.add(new Looping());
		return filtersList;
	}
	

	/**
	 * Display the Gesture, which is a trace of the movement printing by the mouse
	 * @param Graphics2D graphics
	 */
	public void render(Graphics2D graphics){
		if(traceStack.isEmpty()){
			controlledShip.setVelocity(0, 0);
			return;
		}
		
		traceStack.render(graphics);
	}



	/**
	 * The event launched by the mouse, which is described by zen2 Libraries
	 * @param MotionEvent event : the event of the mouse
	 * @see Kind
	 */
	public void event(MotionEvent event){
		switch(event.getKind()){	
		case ACTION_UP : 			
			for(Filter filter : filters){
				if(traceStack.check(filter)){
					filter.apply(controlledShip);
				}
			}
			traceStack.finishCurrentTrace();
			break;

		case ACTION_DOWN :
			/*Normalement besoin de rien mais d�s fois il semble que que la trace se finit mal au bouton up*/
			break;

		case ACTION_MOVE :
			traceStack.getCurrentTrace().addPoint(new Vec2(event.getX(), event.getY()));
			break;

		default:
			break;
		}
	}
	

	public void compute() {
		/*
		 * LF:
		 * C'est la qu'on fait tout les calculs! 
		 * render se charge uniquement de l'affichage!!!		
		 */		
	}
}