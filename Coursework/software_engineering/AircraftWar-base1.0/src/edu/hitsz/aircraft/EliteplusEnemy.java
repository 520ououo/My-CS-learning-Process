package edu.hitsz.aircraft;

import edu.hitsz.application.Main;
import edu.hitsz.application.ImageManager;
import edu.hitsz.strategy.DoubleShootStrategy;

import java.util.LinkedList;
import java.util.List;

public class EliteplusEnemy extends EnemyAircraft {

    private static final int DEFAULT_SCORE = 20;

    public EliteplusEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp, DEFAULT_SCORE);
        this.shootStrategy = new DoubleShootStrategy();
    }

    @Override
    public void forward() {
        super.forward();
        if (locationY >= Main.WINDOW_HEIGHT) {
            vanish();
        }
    }

    @Override
    public EnemyAircraft createInstance(int locationX, int locationY) {
        int width = ImageManager.ELITEPLUS_ENEMY_IMAGE.getWidth();
        int x = (int) (Math.random() * (Main.WINDOW_WIDTH - width));
        int y = (int) (Math.random() * Main.WINDOW_HEIGHT * 0.05);
        int speedX = Math.random() < 0.5 ? -6 : 6;
        return new EliteplusEnemy(x, y, speedX, 5, 80);
    }

    @Override
    public int onFreezeEffect() {
        applyFreeze(3000);
        return 3000;
    }

}
