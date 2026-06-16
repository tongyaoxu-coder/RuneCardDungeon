package model;

public class Player {

    private int hp = 30;

    public int getHp() {
        return hp;
    }

    public void takeDamage(int damage) {
        hp -= damage;
    }
}
