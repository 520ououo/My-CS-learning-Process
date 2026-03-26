package edu.hitsz.item;

import edu.hitsz.aircraft.HeroAircraft;

/**
 * 炸弹道具类，用于消灭屏幕上的所有敌机
 * 继承自 BaseItem，具有以下特性：
 * 1. 被英雄机获得后消灭所有敌机
 * 2. 向下自由掉落
 * @author hitsz
 */
public class BombItem extends BaseItem {

    /** 炸弹伤害值 */
    private int bombDamage;

    /**
     * 构造函数：初始化炸弹道具
     * @param locationX 初始 X 坐标
     * @param locationY 初始 Y 坐标
     * @param speedX X 方向速度
     * @param speedY Y 方向速度
     * @param bombDamage 炸弹伤害值
     */
    public BombItem(int locationX, int locationY, int speedX, int speedY, int bombDamage) {
        super(locationX, locationY, speedX, speedY);
        this.bombDamage = bombDamage;
    }

    /**
     * 道具生效方法：对屏幕上所有敌机造成伤害
     * @param hero 获得道具的英雄机对象
     */
    @Override
    public void active(HeroAircraft hero) {
        // TODO: 实现全屏炸弹伤害逻辑
    }

}
