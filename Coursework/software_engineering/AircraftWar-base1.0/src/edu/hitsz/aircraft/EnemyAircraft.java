package edu.hitsz.aircraft;

import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;
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
     * 工厂方法：创建敌机实例（工厂方法模式）
     * 由具体子类实现，返回对应类型的敌机对象
     * @param locationX X 坐标（未使用，由子类内部生成随机位置）
     * @param locationY Y 坐标（未使用，由子类内部生成随机位置）
     * @return 创建的敌机对象
     */
    public abstract EnemyAircraft createInstance(int locationX, int locationY);

    /**
     * 获取击毁该敌机可获得的分数
     * @return 分数值
     */
    public int getScore() {
        return score;
    }
}

