package edu.hitsz.item;

import edu.hitsz.aircraft.HeroAircraft;

/**
 * 冰冻道具类，用于冻结敌机使其停止移动
 * 继承自 BaseItem，具有以下特性：
 * 1. 被英雄机获得后使所有敌机停止移动一段时间
 * 2. 向下自由掉落
 * @author hitsz
 */
public class FreezeItem extends BaseItem {

    /** 冰冻持续时间（毫秒） */
    private int freezeDuration;

    /**
     * 构造函数：初始化冰冻道具
     * @param locationX 初始 X 坐标
     * @param locationY 初始 Y 坐标
     * @param speedX X 方向速度
     * @param speedY Y 方向速度
     * @param freezeDuration 冰冻持续时间（毫秒）
     */
    public FreezeItem(int locationX, int locationY, int speedX, int speedY, int freezeDuration) {
        super(locationX, locationY, speedX, speedY);
        this.freezeDuration = freezeDuration;
    }

    /**
     * 道具生效方法：冻结屏幕上所有敌机
     * @param hero 获得道具的英雄机对象
     */
    @Override
    public void active(HeroAircraft hero) {
        System.out.println("Freeze active!");
    }

}
