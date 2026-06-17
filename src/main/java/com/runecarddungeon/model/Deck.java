package com.runecarddungeon.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck{
	private List<Card> cards;
	
	public Deck() {
		this.cards= new ArrayList<>();
	}
	
	public void addCard(Card card) {
		if(card!= null) {
			this.cards.add(card);
		}
	}
	
	public void shuffle() {
		Collections.shuffle(this.cards);
	}
	
	public int getRemainingCount() {
		return this.cards.size();
	}
	
	public void clear() {
		this.cards.clear();
	}
	
	//getter, for UI and initializing
	public List<Card> getCards(){
		return cards;
	}
	
}