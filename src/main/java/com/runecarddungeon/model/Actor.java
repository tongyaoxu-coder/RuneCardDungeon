package com.runecarddungeon.model;

public abstract class Actor{//base class
	private String name;
	private int hp;
	private int maxHp;
	private int block;
	
	public Actor(String name, int maxHp) {
		this.name=name;
		this.maxHp=maxHp;
		this.hp=maxHp;
		this.block=0;
	}
	
	//Attacked logic
	public void takeDamage(int dmg) {
		if(this.block >=dmg) {
			this.block-=dmg;
		}else {
			int remainingDmg=dmg-this.block;
			this.block=0;
			this.hp=Math.max(0, this.hp-remainingDmg);
		}
	}
	
	//Block accumulate logic
	public void addBlock(int amount) {
		this.block+=amount;
	}
	
	public void resetBlock() {
		this.block=0;
	}
	
	public String getName() {return name;}
	public int getHp() {return hp;}
	public int getMaxHp() {return maxHp;}
	public int getBlock() {return block;}
	//hp cant exceed maxHp and cant be negative;
	public void setHp(int hp) {this.hp=Math.max(0, Math.max(maxHp, hp));}
	
}