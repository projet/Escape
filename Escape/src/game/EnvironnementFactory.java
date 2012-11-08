package game;

import java.util.Random;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.World;

import entities.Entities;
import entities.maps.Earth;
import entities.maps.Map;
import entities.ships.Player;
import entities.ships.enemies.Enemy;
import entities.ships.enemies.EnnemyBehavior;
import gestures.Gesture;
import hud.Hud;


public class EnvironnementFactory {
	
	/*
	 * TODO: set the gravity to 0 (set to 10 for test only)
	 */

	private static final boolean DO_SLEEP = false;
	
	public static Environnement WORLD1(World world){
		Entities entities = new Entities(world);		
		Player playerShip = new Player(entities);
		EnnemyBehavior enemyBehavior;
		Map map = new Earth();		
		Hud hud = new Hud();

		/*for(int i=0;i<10; i++)
			entities.createEntity(EntityFactoryType.Ennemy);*/
		
		
		Environnement env = new Environnement(map, playerShip, entities, null, hud);		
		return env;
	}
	
	
	public static Environnement factory(World world){
		if(world==null){
			world = new World(new Vec2(Variables.WORLD_GRAVITY_X, Variables.WORLD_GRAVITY_Y), DO_SLEEP);
		}
		else{
			world.setGravity(new Vec2(Variables.WORLD_GRAVITY_X, Variables.WORLD_GRAVITY_Y));
			world.setAllowSleep(DO_SLEEP);
		}

		setWorldLimit(world);
		
		
		/*
		 * TODO: Mettre ICI tout les niveaux du jeu!
		 * 
		 * if(lvl1)
		 * 		return world1()
		 * ...
		 *  
		 */		
		return WORLD1(world);
	}
	
	public static Environnement factory(){
		return factory(null);
	}
	
	
	
	
	/*
	 * Set the limit of our world
	 */
	private static void setWorldLimit(World world){
		BodyDef bd = new BodyDef();
		Body ground = world.createBody(bd);

		float worldWidth = Variables.SCREEN_WIDTH/Variables.WORLD_SCALE;
		float worldHeight = Variables.SCREEN_HEIGHT/Variables.WORLD_SCALE;
		
		PolygonShape shape = new PolygonShape();
		//0,0->width,0
		shape.setAsEdge(new Vec2(0f, 0f), new Vec2(worldWidth, 0.0f));
		ground.createFixture(shape, 0.0f);
		//Width,0->width,height
		shape.setAsEdge(new Vec2(worldWidth, 0f), new Vec2(worldWidth, worldHeight));
		ground.createFixture(shape, 0.0f);
		//width,height->0,height
		shape.setAsEdge(new Vec2(worldWidth, worldHeight), new Vec2(0, worldHeight));
		ground.createFixture(shape, 0.0f);
		//0,height->0,0
		shape.setAsEdge(new Vec2(0, worldHeight), new Vec2(0, 0));
		ground.createFixture(shape, 0.0f);
	}
	
}
