package game;

import java.awt.Graphics2D;
import java.util.LinkedList;
import java.util.List;

import listeners.EntitiesListener;
import listeners.CollisionListener.EntityType;
import listeners.EnvironnementListener;
import listeners.EnvironnementListener.GameState;
import maps.Map;
import effects.Effects;
import entities.Entities;
import entities.ships.Player;
import entities.ships.enemies.EnemiesLoader;
import fr.umlv.zen2.MotionEvent;
import gestures.Gesture;
import hud.Hud;


public class Environnement implements EntitiesListener {



	private final Map map;				//The background map
	private final Entities entities;
	private final Gesture gesture;		//Gesture/Event manager
	private final Player player;
	private final List<EnvironnementListener> gameListener;
	private final EnemiesLoader enemiesLoader;
	private GameState gameState;


	/**
	 * Create the environnement with the associated world 
	 * @param world Jbox2d world
	 */
	public Environnement(Entities entities, Map map, Player player, EnemiesLoader enemiesLoader){
		this.gameListener = new LinkedList<>();
		this.map = map;
		this.player = player;
		this.entities=entities;
		this.enemiesLoader=enemiesLoader;
		this.gesture = new Gesture(player);
		Hud.get().setPlayer(player);
		entities.addEntitiesListener(this);
	}


	public void addListener(EnvironnementListener listener) {
		this.gameListener.add(listener);		
	}
	
	public void removeListener(EnvironnementListener listener) {
		this.gameListener.remove(listener);		
	}
	
	
	public Player getPlayer() {
		return player;
	}

	public Entities getEntities() {
		return entities;
	}
	
	/*
	 * Entities Listener: detect the death of each Entity
	 * @see entities.EntitiesListener#entityRemoved(entities.Entity.EntityType)
	 */
	@Override
	public void entityRemoved(EntityType type) {
		
		switch (type){
		case Boss:
			gameState = GameState.Win;
			break;
		case Joueur:
			gameState = GameState.Loose;
			break;
		default:
			gameState=null;
			break;
		}
		if(gameState!=null){
			for(EnvironnementListener listener : gameListener){
				listener.stateChanged(gameState);
			}
		}
	}
	
	/**
	 * Render all entities associated
	 * @param graphics draw area
	 */
	public void render(Graphics2D graphics){			
		map.render(graphics);				//The ground (the planet)
		entities.render(graphics);			//All the entities (player too)
		gesture.render(graphics);			//Gesture movements (circle)
		Effects.render(graphics);			//Effect (explosion, cloud, ..)
		Hud.get().render(graphics);				//Health, score, amo
	}

	/**
	 * Send the event to the gesture manager
	 * @param event the event to be handled
	 */
	public void event(MotionEvent event) {
		gesture.event(event);
		Hud.get().event(event);
	}

	
	/**
	 * Methode compute appellee par TestBed: step()
	 */
	public void compute() {
		entities.compute();
		entities.step(Variables.WORLD_TIME_STEP, Variables.WORLD_VELOCITY_ITERATION, Variables.WORLD_POSITION_ITERATION);
		enemiesLoader.compute();
		map.compute();
		Effects.compute();
	}

}




