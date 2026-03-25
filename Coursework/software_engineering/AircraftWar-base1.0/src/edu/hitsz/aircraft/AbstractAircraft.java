package edu.hitsz.aircraft;

import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.basic.AbstractFlyingObject;
import java.util.List;

/**
 * 所有类型飞机的抽象基类，继承自 AbstractFlyingObject
 * 定义了飞机的通用属性和行为：
 * 1. 生命值管理（当前生命值、最大生命值）
 * 2. 受到伤害和死亡逻辑
 * 3. 射击能力（由子类具体实现）
 * @author hitsz
 */
public abstract class AbstractAircraft extends AbstractFlyingObject {

    /** 飞机的最大生命值 */
    protected int maxHp;
    /** 飞机当前的生命值 */
    protected int hp;

    /**
     * 构造函数：初始化飞机的位置和状态
     * @param locationX 初始 X 坐标
     * @param locationY 初始 Y 坐标
     * @param speedX X 方向的速度
     * @param speedY Y 方向的速度
     * @param hp 初始生命值（同时设置为最大生命值）
     */
    public AbstractAircraft(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY);
        this.hp = hp;
        this.maxHp = hp;
    }

    /**
     * 减少生命值方法
     * 当生命值降为 0 或更低时，标记对象为无效（调用 vanish）
     * @param decrease 要减少的生命值
     */
    public void decreaseHp(int decrease){
        hp -= decrease;
        if(hp <= 0){
            hp = 0;      // 确保生命值不为负数
            vanish();    // 生命值为 0 时，标记为无效，等待被清理
        }
    }

    /**
     * 获取当前生命值
     * @return 当前生命值
     */
    public int getHp() {
        return hp;
    }


    /**
     * 飞机射击方法（抽象方法）
     * 用于发射子弹，由各子类根据具体需求实现
     * @return 返回射击产生的子弹列表
     *         - 可射击的飞机类型需要实现此方法，返回包含子弹的列表
     *         - 不可射击的飞机类型可以返回空列表
     */
    public abstract List<BaseBullet> shoot();

}


