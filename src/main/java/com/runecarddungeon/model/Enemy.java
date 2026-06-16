package model;

public class Enemy {

    private int hp = 20;

    public int getHp() {
        return hp;
    }

    public void takeDamage(int damage) {
        hp -= damage;
    }
}
