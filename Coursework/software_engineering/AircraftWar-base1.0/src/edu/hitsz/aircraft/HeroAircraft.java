package edu.hitsz.aircraft;

import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.HeroBullet;

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

    private static final HeroAircraft instance =new HeroAircraft(
            Main.WINDOW_WIDTH / 2,
            Main.WINDOW_HEIGHT - ImageManager.HERO_IMAGE.getHeight(),
            0, 0, 100);
    /** 每次射击时发射的子弹数量，初始为 1 发 */
    private int shootNum = 1;

    /** 子弹的威力值，决定对敌机造成的伤害 */
    private int power = 30;

    /** 
     * 子弹射击方向系数
     * -1 表示向上发射（英雄机向屏幕上方射击）
     * 1 表示向下发射
     */
    private int direction = -1;

    /**
     * 构造函数：初始化英雄机
     * @param locationX 初始 X 坐标
     * @param locationY 初始 Y 坐标
     * @param speedX X 方向速度（英雄机由鼠标控制，此参数通常设为 0）
     * @param speedY Y 方向速度（英雄机由鼠标控制，此参数通常设为 0）
     * @param hp 初始生命值
     */
    private HeroAircraft(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
    }

    public static HeroAircraft getInstance(){
        return instance;
    }

    /**
     * 重写父类的 forward() 方法
     * 英雄机的位置由鼠标控制器（HeroController）直接管理，
     * 因此该方法为空实现，不执行任何移动操作
     */
    @Override
    public void forward() {
        // 英雄机由鼠标控制，不通过 forward 函数移动
    }

    /**
     * 实现射击方法，生成并发射子弹
     * 根据 shootNum 设置，可一次发射多发子弹
     * 子弹呈扇形或直线分布，横向分散排列
     * @return 包含所有发射出的子弹对象的列表
     */
    @Override
    public List<BaseBullet> shoot() {
        List<BaseBullet> res = new LinkedList<>();
        
        // 获取当前英雄机的位置
        int x = this.getLocationX();
        // 子弹生成的 Y 坐标，相对飞机位置向前偏移
        int y = this.getLocationY() + direction * 2;
        
        // 子弹的速度设置
        int speedX = 0;  // 子弹水平方向无初速度
        int speedY = this.getSpeedY() + direction * 5;  // 子弹垂直方向速度
        
        BaseBullet bullet;
        
        // 循环生成指定数量的子弹
        for(int i = 0; i < shootNum; i++){
            // 子弹发射位置相对飞机位置向前偏移
            // 多个子弹横向分散：(i*2 - shootNum + 1)*10 计算每发子弹的水平偏移量
            // 例如：shootNum=1 时，偏移为 0；shootNum=3 时，偏移分别为 -20, 0, 20
            bullet = new HeroBullet(x + (i * 2 - shootNum + 1) * 10, y, speedX, speedY, power);
            res.add(bullet);
        }
        return res;
    }

    public void sethp(int hp){
        this.hp=hp;
    }

}
