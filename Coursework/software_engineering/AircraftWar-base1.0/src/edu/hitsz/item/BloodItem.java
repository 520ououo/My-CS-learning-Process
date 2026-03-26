package edu.hitsz.item;

import edu.hitsz.aircraft.HeroAircraft;

/**
 * 加血道具类，用于恢复英雄机的生命值
 * 继承自 BaseItem，具有以下特性：
 * 1. 被英雄机获得后恢复一定生命值
 * 2. 向下自由掉落
 * @author hitsz
 */
public class BloodItem extends BaseItem {

    /** 恢复的生命值 */
    private int healValue;

    /**
     * 构造函数：初始化加血道具
     * @param locationX 初始 X 坐标
     * @param locationY 初始 Y 坐标
     * @param speedX X 方向速度
     * @param speedY Y 方向速度
     * @param healValue 恢复的生命值
     */
    public BloodItem(int locationX, int locationY, int speedX, int speedY, int healValue) {
        super(locationX, locationY, speedX, speedY);
        this.healValue = healValue;
    }

    /**
     * 道具生效方法：恢复英雄机的生命值
     * @param hero 获得道具的英雄机对象
     */
    @Override
    public void active(HeroAircraft hero) {
        // TODO: 实现加血逻辑
    }

}
