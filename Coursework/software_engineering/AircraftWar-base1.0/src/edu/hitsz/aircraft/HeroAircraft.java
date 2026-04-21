package edu.hitsz.aircraft;

import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;
import edu.hitsz.strategy.SingleShootStrategy;
import edu.hitsz.strategy.SpreadShootStrategy;
import edu.hitsz.strategy.CircleShootStrategy;

import java.util.LinkedList;
import java.util.List;

public class HeroAircraft extends AbstractAircraft {

    private static final HeroAircraft instance = new HeroAircraft(
            Main.WINDOW_WIDTH / 2,
            Main.WINDOW_HEIGHT - ImageManager.HERO_IMAGE.getHeight(),
            0, 0, 10000);

    private long powerUpStartTime = 0;
    private boolean hasActivePowerUp = false;

    private HeroAircraft(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
        this.shootStrategy = new SingleShootStrategy();
    }

    public static HeroAircraft getInstance() {
        return instance;
    }

    @Override
    public void forward() {
    }

    public void sethp(int hp) {
        this.hp = hp;
    }

    public void activateSpreadFire() {
        this.shootStrategy = new SpreadShootStrategy();
        this.powerUpStartTime = System.currentTimeMillis();
        this.hasActivePowerUp = true;
        System.out.println("英雄机切换为散射模式！");
    }

    public void activateCircleFire() {
        this.shootStrategy = new CircleShootStrategy();
        this.powerUpStartTime = System.currentTimeMillis();
        this.hasActivePowerUp = true;
        System.out.println("英雄机切换为环射模式！");
    }

    public void checkPowerUpExpiration() {
        if (hasActivePowerUp && System.currentTimeMillis() - powerUpStartTime >= 3000) {
            this.shootStrategy = new SingleShootStrategy();
            this.hasActivePowerUp = false;
            this.powerUpStartTime = 0;
            System.out.println("道具效果结束，恢复为直射模式！");
        }
    }

    public boolean isPoweredUp() {
        return hasActivePowerUp;
    }
}
