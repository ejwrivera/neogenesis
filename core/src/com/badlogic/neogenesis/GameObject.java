package com.badlogic.neogenesis;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.utils.Array;

public class GameObject {
	
	protected IDrawable drawLogic;
	protected IAudible soundLogic;
	protected IMobile moveLogic;
	protected ICollidable collideLogic;
	protected ILiving metabolicLogic;
	
	// this can probably be refactored to use builder, and then default implementations for each of these can be made for any new types
	public GameObject(IDrawable drawLogic, IAudible soundLogic, IMobile moveLogic, ICollidable collideLogic, ILiving metabolicLogic) {
        this.drawLogic 		= drawLogic;
        this.soundLogic		= soundLogic;
        this.moveLogic 		= moveLogic;
        this.collideLogic 	= collideLogic;
        this.metabolicLogic = metabolicLogic;
    }
 
	public Shape2D getShape(){
		return drawLogic.getShape();
	}
	public Texture getTexture(){
		return drawLogic.getTexture();
	}
	
	public boolean emittingSound() {
		return soundLogic.emittingSound();
	}
	
	public void emitSound() {
		soundLogic.emitSound();
    }
		
    public void move() {
        moveLogic.move();
    }
    
    public Array<GameObject> collidesWith(Array<GameObject> otherCollidables) {
		return collideLogic.collidesWith(otherCollidables);
	}

    public boolean collidesWith(ICollidable collidable){
    	return collideLogic.collidesWith(collidable);
    }
    
	public boolean collidesWith(GameObject collidable){
		return collideLogic.collidesWith(collidable);
	}
	
	public void collidedWith(ICollidable collidable){
		collideLogic.collidedWith(collidable);
	}
	
	public void collidedWith(GameObject collidable){
		collideLogic.collidedWith(collidable);
	}
	
	public void collidedWith(Rock rock) {
		collideLogic.collidedWith(rock);
	}
	
	public void collidedWith(Devourer devourer) {
		collideLogic.collidedWith(devourer);
	}
	
	public void collidedWith(Devourable devourable) {
		collideLogic.collidedWith(devourable);
	}
    
    public void live() {
    	metabolicLogic.live();
    }

	public boolean stillCollidable() {
		return collideLogic.stillCollidable();
	}

	public boolean isAlive(){
		return metabolicLogic.isAlive();
	}
	
	public Corpse getCorpse(){
		return metabolicLogic.getCorpse();
	}
    
}
