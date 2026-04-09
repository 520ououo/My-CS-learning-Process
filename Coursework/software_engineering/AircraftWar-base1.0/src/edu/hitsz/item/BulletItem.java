package edu.hitsz.item;

import edu.hitsz.aircraft.HeroAircraft;

/**
 * 火力道具类，用于增强英雄机的子弹数量
 * 继承自 BaseItem，具有以下特性：
 * 1. 被英雄机获得后增加射击子弹数量
 * 2. 向下自由掉落
 * @author hitsz
 */
public class BulletItem extends BaseItem {

    /** 增加的子弹数量 */
    private int bulletIncrease;

    /**
     * 构造函数：初始化火力道具
     * @param locationX 初始 X 坐标
     * @param locationY 初始 Y 坐标
     * @param speedX X 方向速度
     * @param speedY Y 方向速度
     * @param bulletIncrease 增加的子弹数量
     */
    public BulletItem(int locationX, int locationY, int speedX, int speedY, int bulletIncrease) {
        super(locationX, locationY, speedX, speedY);
        this.bulletIncrease = bulletIncrease;
    }

    /**
     * 道具生效方法：增加英雄机的射击子弹数量
     * @param hero 获得道具的英雄机对象
     */
    @Override
    public void active(HeroAircraft hero) {
        System.out.println("FireSupply active!");
    }

}
