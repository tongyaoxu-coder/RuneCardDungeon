package com.runecarddungeon.model;

public class Skeleton extends Enemy {
	private int turnCount =0;
	private int attackPower = 10;
	
	public Skeleton() {
		super("Skeleton", 60);
		rollIntent();
	}
	
	@Override
	public void takeTurn(Player target) {
		turnCount++;
		if(turnCount%3==0) {// 3 behaviors
			this.attackPower+=4;
		}else if(turnCount%3==1) {
			target.takeDamage(attackPower);//strike
		}else {
			this.addBlock(8);
		}
		rollIntent();
	}
	
	@Override
	public void rollIntent() {
		int nextTurn=(turnCount+1)%3;
		if(nextTurn==0) {
			this.setCurrIntent("Enraged: Attack +4");
		}else if(nextTurn==1) {
			this.setCurrIntent("Strike: hit "+ attackPower);
		}else {
			this.setCurrIntent("Bone block: block 8");
		}
	}
	

}
