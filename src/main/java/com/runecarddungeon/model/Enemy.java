package com.runecarddungeon.model;

public abstract class Enemy extends Actor {
	private String currIntent;
	private int attackDamage;
	
	public Enemy(String name, int maxHp, int attackDamage) {
		super(name, maxHp);
		this.attackDamage=attackDamage;
		
	}
	
	public void initial() {
		//initialize enemy, calling methods in Actor
		super.setHp(super.getMaxHp());
		System.out.println(this.getName()+" is coming... Hp: "+super.getHp());
		
	}
	
	public int getAttackDamage() {
		return this.attackDamage;
	}
	
	public void attack(Player player) {
		if(player!=null) {
			player.takeDamage(this.attackDamage);
		}
	}
	
	public abstract void rollIntent();
	public abstract void takeTurn(Player target);
	public String getCurrentIntent() {return currIntent;}
	public void setCurrIntent(String intent) {this.currIntent=intent;}
   
}
