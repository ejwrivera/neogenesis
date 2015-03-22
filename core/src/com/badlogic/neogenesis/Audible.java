package com.badlogic.neogenesis;

public class Audible implements IAudible {

	private boolean emitting;
	
	public Audible(){
		emitting = false;
	}
	
	@Override
	public void emitSound() {
		emitting = false;
	}

	public boolean emittingSound(){
		return emitting;
	}

	public void setSound(boolean emitting) {
		this.emitting = emitting;
	}
	
}
