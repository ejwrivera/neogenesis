package com.badlogic.neogenesis;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Shape2D;

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
	
	public void emitSound() {
		soundLogic.emitSound();
    }
		
    public void move() {
        moveLogic.move();
    }
 
    public void collide() {
    	collideLogic.Collide();
    }
    
    public void live() {
    	metabolicLogic.live();
    }
    
}
