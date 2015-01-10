package com.badlogic.neogenesis;

// TODO: Auto-generated Javadoc
// for storing values to use for debugging, demoing, and testing purposes
/**
 * The Class DebugValues.
 */
public class DebugValues {

	/** Turn on to enter debug mode, using debug values, off to use default production values. */
	public static Boolean debug = false;
	// debug values
	/** Eve's starting biomass. */
	private static int eveStartingBiomass = 200;
	/** The spawn rate. */
	private static int spawnRate = 2;
	/** The initial camera zoom factor. */
	private static float cameraZoomStart = 2;
	/** The camera zoom rate. */
	private static int cameraZoomRate = 1;
	// default values
	/** The default eve starting biomass. */
	private static int defaultEveStartingBiomass = 10;
	/** The default spawn rate. */
	private static int defaultSpawnRate = 1;
	/** The default initial camera zoom. */
	private static float defaultCameraZoomStart = .5f;
	/** The default camera zoom rate. */
	private static int defaultCameraZoomRate = 1;
	
	
	/**
	 * Populate debug values.  For programmatically setting debug values
	 * @param eveStartBio set Eve's starting biomass
	 * @param spawn set the spawn rate
	 * @param cameraStart set the starting camera zoom factor
	 * @param cameraRate set the camera zoom rate
	 */
	public static void populateDebugValues(int eveStartBio, int spawn, float cameraStart, int cameraRate){
		eveStartingBiomass=eveStartBio;
		spawnRate = spawn;
		cameraZoomStart = cameraStart;
		cameraZoomRate = cameraRate;
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
				break;
			// quick start
			case 2:
				eveStartingBiomass = 30;
				spawnRate = 1;
				cameraZoomStart = .5f;
				cameraZoomRate = 1;
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
	
}
