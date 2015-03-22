package com.badlogic.neogenesis;

public class GameObject {
	
	protected IDrawable drawLogic;
	protected IAudible soundLogic;
	protected IMobile moveLogic;
	protected ICollidable collideLogic;
	protected ILiving metabolicLogic;
	
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
