package edu.hitsz.aircraft;

import edu.hitsz.bullet.BaseBullet;
import java.util.List;

/**
 * 敌方飞机的抽象基类，继承自 AbstractAircraft
 * 定义了敌方飞机的通用属性和行为：
 * 1. 提供基础的移动逻辑
 * 2. 定义敌机特有的属性（如得分值）
 * 3. 为不同类型的敌机提供扩展基础
 * @author hitsz
 */
public abstract class EnemyAircraft extends AbstractAircraft {

    /** 击毁该敌机可获得的分数 */
    protected int score;

    /**
     * 构造函数：初始化敌方飞机
     * @param locationX 初始 X 坐标
     * @param locationY 初始 Y 坐标
     * @param speedX X 方向的速度
     * @param speedY Y 方向的速度
     * @param hp 初始生命值
     * @param score 击毁该敌机可获得的分数
     */
    public EnemyAircraft(int locationX, int locationY, int speedX, int speedY, int hp, int score) {
        super(locationX, locationY, speedX, speedY, hp);
        this.score = score;
    }

    /**
     * 获取击毁该敌机可获得的分数
     * @return 分数值
     */
    public int getScore() {
        return score;
    }

    /**
     * 敌机射击方法
     * 默认返回空列表，表示无法射击或不需要射击
     * 具有射击能力的敌机类型应重写此方法
     * @return 返回射击产生的子弹列表
     */
    @Override
    public List<BaseBullet> shoot() {
        return new java.util.LinkedList<>();
    }

}

