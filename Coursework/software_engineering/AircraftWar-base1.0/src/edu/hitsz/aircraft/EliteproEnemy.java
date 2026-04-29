package edu.hitsz.aircraft;

import edu.hitsz.application.Main;
import edu.hitsz.application.ImageManager;
import edu.hitsz.strategy.SpreadShootStrategy;

import java.util.LinkedList;
import java.util.List;

public class EliteproEnemy extends EnemyAircraft {

    private static final int DEFAULT_SCORE = 25;

    public EliteproEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp, DEFAULT_SCORE);
        this.shootStrategy = new SpreadShootStrategy();
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
        int width = ImageManager.ELITEPRO_ENEMY_IMAGE.getWidth();
        int x = (int) (Math.random() * (Main.WINDOW_WIDTH - width));
        int y = (int) (Math.random() * Main.WINDOW_HEIGHT * 0.05);
        int speedX = Math.random() < 0.5 ? -6 : 6;
        return new EliteproEnemy(x, y, speedX, 5, 100);
    }

    @Override
    public int onBombEffect() {
        decreaseHp(100);
        return 0;
    }

    @Override
    public int onFreezeEffect() {
        applySlow(5000);
        return -5000;
    }

}
