package com.badlogic.neogenesis;
/**
 * For storing values to use for debugging, demoing, and testing purposes.
 */
public class DebugValues {

	/** Turn on to enter debug mode, using debug values, off to use default production values. */
	public static Boolean debug = false;
	
	/* debug values */
	
	/** Eve's starting biomass. */
	private static int eveStartingBiomass = 200;
	/** The spawn rate. */
	private static int spawnRate = 2;
	/** The initial camera zoom factor. */
	private static float cameraZoomStart = 1;
	/** The camera zoom rate. */
	private static int cameraZoomRate = 1;
	/**  Toggls magnitude checks for consuming. */
	private static boolean magnitudeColliding = false;
	/**  The amount of starting creatures. */
	private static int creatureAmount = 300;
	/**  The amount of starting food. */
	private static int foodAmount = 500;
	/**  The amount of starting rocks. */
	private static int rockAmount = 200;
	
	/* default values */
	
	/** The default eve starting biomass. */
	private static int defaultEveStartingBiomass = 10;
	/** The default spawn rate. */
	private static int defaultSpawnRate = 1;
	/** The default initial camera zoom. */
	private static float defaultCameraZoomStart = .25f;
	/** The default camera zoom rate. */
	private static int defaultCameraZoomRate = 1;
	/**  Toggls magnitude checks for consuming. */
	private static boolean defaultMagnitudeColliding = true;
	/**  The default amount of starting creatures. */
	private static int defaultCreatureAmount = 400;
	/**  The default amount of starting food. */
	private static int defaultFoodAmount = 500;
	/**  The default amount of starting rocks. */
	private static int defaultRockAmount = 200;
	
	/**
	 * Populate debug values.  For programmatically setting debug values.  This should implement builder eventually, obviating the need for these methods.
	 * @param eveStartBio set Eve's starting biomass
	 * @param spawn set the spawn rate
	 * @param cameraStart set the starting camera zoom factor
	 * @param cameraRate set the camera zoom rate
	 * @param magnitudeCollide the magnitude collide
	 */
	public static void populateDebugValues(int eveStartBio, int spawn, float cameraStart, int cameraRate, boolean magnitudeCollide){
		eveStartingBiomass=eveStartBio;
		spawnRate = spawn;
		cameraZoomStart = cameraStart;
		cameraZoomRate = cameraRate;
		magnitudeColliding = magnitudeCollide;
	}
	
	/**
	 * Populate debug values.
	 * @param parameter pass this an integer (for now) argument and it will set up a specific debug scenario
	 */
	public static void populateDebugValues(int parameter){
		switch (parameter){
			// godzilla eve
			case 1:
				eveStartingBiomass = 200;
				spawnRate = 3;
				cameraZoomStart = 2;
				cameraZoomRate = 1;
				magnitudeColliding = false;
				foodAmount = 500;
				break;
			// quick start
			case 2:
				eveStartingBiomass = 30;
				spawnRate = 1;
				cameraZoomStart = .25f;
				cameraZoomRate = 1;
				magnitudeColliding = false;
				foodAmount = 1000;
				break;
			default:
		}
	}
	
	/**
	 * Gets the eve starting biomass.
	 * @return eve's starting biomass
	 */
	public static int getEveStartingBiomass() {
		return debug ? eveStartingBiomass : defaultEveStartingBiomass;
	}
	
	/**
	 * Gets the spawn rate.
	 * @return the spawn rate
	 */
	public static int getSpawnRate() {
		return debug ? spawnRate : defaultSpawnRate;
	}
	
	/**
	 * Gets the initial camera zoom.
	 * @return the initial camera zoom factor
	 */
	public static float getCameraZoomStart() {
		return debug ? cameraZoomStart : defaultCameraZoomStart;
	}
	
	/**
	 * Gets the camera zoom rate.
	 * @return the camera zoom rate
	 */
	public static int getCameraZoomRate() {
		return debug ? cameraZoomRate : defaultCameraZoomRate;
	}
	
	/**
	 * Gets the magnitude colliding.
	 *
	 * @return the magnitude colliding
	 */
	public static boolean getMagnitudeColliding() {
		return debug ? magnitudeColliding : defaultMagnitudeColliding;
	}
	
	/**
	 * Gets the starting creature amount.
	 *
	 * @return the creature amount
	 */
	public static int getCreatureAmount(){
		return debug ? creatureAmount : defaultCreatureAmount;
	}
	
	/**
	 * Gets the starting food amount.
	 *
	 * @return the food amount
	 */
	public static int getFoodAmount(){
		return debug ? foodAmount : defaultFoodAmount;
	}
	
	/**
	 * Gets the starting rock amount.
	 *
	 * @return the rock amount
	 */
	public static int getRockAmount(){
		return debug ? rockAmount : defaultRockAmount;
	}
}
