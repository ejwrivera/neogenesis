package com.badlogic.neogenesis;

/**
 * The Audible interface.  Used to determine what sounds something is trying to emit.
 */
public interface Audible {
	
	/**
	 * Emitting sound.
	 * @return true, if successful
	 */
	public boolean emittingSound();
	
	/**
	 * Emit sound.
	 */
	public void emitSound();
	
}
