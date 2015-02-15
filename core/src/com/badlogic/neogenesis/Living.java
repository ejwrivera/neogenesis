package com.badlogic.neogenesis;

public interface Living extends Identifiable{
	public void live();
	public void die();
	public boolean isAlive();
	public Corpse getCorpse();
}
