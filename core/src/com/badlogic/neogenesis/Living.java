package com.badlogic.neogenesis;

public class Living implements ILiving {

	private boolean alive;
	private int clocktick;
	private int health;
	
	public Living(){
		alive = true;
		clocktick = 0;
		health = 10;
	}
	
	
	/* (non-Javadoc)
	 * @see com.badlogic.neogenesis.Living#isAlive()
	 */
	@Override
	public boolean isAlive() {
		return alive;
	}
	
	/* (non-Javadoc)
	 * @see com.badlogic.neogenesis.Living#die()
	 */
	public void die(){
		alive=false;
	}
	
	/* (non-Javadoc)
	 * @see com.badlogic.neogenesis.Living#getCorpse()
	 */
	public Corpse getCorpse(){
		return null;
	}
	
	/* (non-Javadoc)
	 * @see com.badlogic.neogenesis.Living#live()
	 */
	@Override
	public void live() {
		clocktick++;
		
		if (health <= 0){
			die();
		}
		//if (abilities.get("photosynthesis") && clocktick%25==0){
		//	digest(new Food(1));
		//}
	}	
}
