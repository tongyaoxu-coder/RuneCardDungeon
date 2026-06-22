ppackage com.runecarddungeon.model;

public class Skeleton extends Enemy {
    private static final int SHIELD_PER_TURN = 4;

    public Skeleton(String name, int maxHp, int attackDamage) {
        super(name, maxHp, attackDamage);
        rollIntent();
    }

    @Override
    public void onTurnStart() {
        // 每回合获得4点护盾
        addBlock(SHIELD_PER_TURN);
        System.out.println("🛡️ " + getName() + " Gain  " + SHIELD_PER_TURN + " shield points！");
    }

    @Override
    public void takeTurn(Player target) {
        // 每回合固定攻击
        System.out.println("⚔️ " + getName() + " Launch an attack, dealing " + getAttackDamage() + " damage！");
        attack(target);
        rollIntent();
    }

    @Override
    public void rollIntent() {
        this.setCurrIntent("⚔️ Attack: Deals " + getAttackDamage() + " damage and grants  " + SHIELD_PER_TURN + " shield points");
    }
}
