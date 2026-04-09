package edu.hitsz.item;

import edu.hitsz.aircraft.HeroAircraft;

/**
 * 超级火力道具类，用于大幅增强英雄机的火力
 * 继承自 BaseItem，具有以下特性：
 * 1. 被英雄机获得后大幅增加射击子弹数量和威力
 * 2. 向下自由掉落
 * @author hitsz
 */
public class SuperBulletItem extends BaseItem {

    /** 增加的子弹数量 */
    private int bulletIncrease;
    /** 增加的子弹威力 */
    private int powerIncrease;

    /**
     * 构造函数：初始化超级火力道具
     * @param locationX 初始 X 坐标
     * @param locationY 初始 Y 坐标
     * @param speedX X 方向速度
     * @param speedY Y 方向速度
     * @param bulletIncrease 增加的子弹数量
     * @param powerIncrease 增加的子弹威力
     */
    public SuperBulletItem(int locationX, int locationY, int speedX, int speedY, int bulletIncrease, int powerIncrease) {
        super(locationX, locationY, speedX, speedY);
        this.bulletIncrease = bulletIncrease;
        this.powerIncrease = powerIncrease;
    }

    /**
     * 道具生效方法：大幅增加英雄机的子弹数量和威力
     * @param hero 获得道具的英雄机对象
     */
    @Override
    public void active(HeroAircraft hero) {
        hero.activateCircleFire();
        System.out.println("超级火力道具生效！英雄机切换为环射模式（20发）");
    }

}
