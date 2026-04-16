package edu.hitsz.aircraft;

import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;
import edu.hitsz.strategy.SingleShootStrategy;
import edu.hitsz.strategy.SpreadShootStrategy;
import edu.hitsz.strategy.CircleShootStrategy;

import java.util.LinkedList;
import java.util.List;

/**
 * 英雄飞机类，代表玩家操控的战机
 * 继承自 AbstractAircraft，具有以下特性：
 * 1. 由玩家通过鼠标控制移动
 * 2. 可以发射子弹攻击敌机
 * 3. 支持多子弹射击和火力升级
 * @author hitsz
 */
public class HeroAircraft extends AbstractAircraft {

    private static final HeroAircraft instance = new HeroAircraft(
            Main.WINDOW_WIDTH / 2,
            Main.WINDOW_HEIGHT - ImageManager.HERO_IMAGE.getHeight(),
            0, 0, 100);

    /**
     * 构造函数：初始化英雄机
     */
    private HeroAircraft(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
        // 默认使用单发策略
        this.shootStrategy = new SingleShootStrategy();
    }

    public static HeroAircraft getInstance() {
        return instance;
    }

    @Override
    public void forward() {
        // 英雄机由鼠标控制，不通过 forward 函数移动
    }

    public void sethp(int hp) {
        this.hp = hp;
    }

    /**
     * 激活火力道具效果：切换为散射模式（3发）
     */
    public void activateSpreadFire() {
        this.shootStrategy = new SpreadShootStrategy();
        System.out.println("英雄机切换为散射模式！");
    }

    /**
     * 激活超级火力道具效果：切换为环射模式（20发）
     */
    public void activateCircleFire() {
        this.shootStrategy = new CircleShootStrategy();
        System.out.println("英雄机切换为环射模式！");
    }
}
