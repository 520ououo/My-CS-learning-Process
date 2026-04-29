package edu.hitsz.aircraft;

import edu.hitsz.application.Main;
import edu.hitsz.application.ImageManager;
import edu.hitsz.strategy.CircleShootStrategy;

public class Boss extends EnemyAircraft {

    private static final int DEFAULT_SCORE = 150;

    public Boss(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp, DEFAULT_SCORE);
        this.shootStrategy = new CircleShootStrategy();
    }

    @Override
    public void forward() {
        locationX += speedX;
        if (locationX <= 0 || locationX >= Main.WINDOW_WIDTH) {
            speedX = -speedX;
        }
    }

    @Override
    public EnemyAircraft createInstance(int locationX, int locationY) {
        int width = ImageManager.BOSS_ENEMY_IMAGE.getWidth();
        int x = (int) (Math.random() * (Main.WINDOW_WIDTH - width));
        int y = 100;
        int speedX = Math.random() < 0.5 ? -3 : 3;
        return new Boss(x, y, speedX, 0, 300);
    }

    @Override
    public int onBombEffect() {
        return 0;
    }

    @Override
    public int onFreezeEffect() {
        return 0;
    }

}
