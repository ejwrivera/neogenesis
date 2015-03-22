package com.badlogic.neogenesis;

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
 
	public void draw() {
		drawLogic.draw();
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
