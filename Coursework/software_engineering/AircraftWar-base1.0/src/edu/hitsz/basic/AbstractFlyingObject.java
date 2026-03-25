package edu.hitsz.basic;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;

import java.awt.image.BufferedImage;

/**
 * 所有可飞行对象的抽象基类
 * 提供游戏中所有飞行物体的通用属性和行为：
 * 1. 位置坐标和移动速度管理
 * 2. 图片资源的懒加载
 * 3. 碰撞检测逻辑
 * 4. 对象有效性标记（用于对象池清理）
 * @author hitsz
 */
public abstract class AbstractFlyingObject {

    /** X 坐标，表示对象中心的水平位置 */
    protected int locationX;
    /** Y 坐标，表示对象中心的垂直位置 */
    protected int locationY;

    /** X 轴方向的移动速度 */
    protected int speedX;
    /** Y 轴方向的移动速度 */
    protected int speedY;

    /** 
     * 对象的图片资源
     * 初始为 null，通过 getImage() 方法懒加载
     */
    protected BufferedImage image = null;

    /** 
     * 对象在 X 轴的宽度
     * 根据图片尺寸自动获取，-1 表示尚未初始化
     */
    protected int width = -1;

    /** 
     * 对象在 Y 轴的高度
     * 根据图片尺寸自动获取，-1 表示尚未初始化
     */
    protected int height = -1;

    /** 
     * 对象有效性标记
     * true 表示对象有效（存活），false 表示对象已失效（应被清除）
     * 标记为 false 的对象会在下次游戏刷新时被清除
     */
    protected boolean isValid = true;

    /**
     * 默认构造函数
     */
    public AbstractFlyingObject() {
    }

    /**
     * 带参数的构造函数：初始化飞行对象的基本属性
     * @param locationX 初始 X 坐标
     * @param locationY 初始 Y 坐标
     * @param speedX X 方向速度
     * @param speedY Y 方向速度
     */
    public AbstractFlyingObject(int locationX, int locationY, int speedX, int speedY) {
        this.locationX = locationX;
        this.locationY = locationY;
        this.speedX = speedX;
        this.speedY = speedY;
    }

    /**
     * 根据速度更新飞行对象的位置
     * 当飞行对象触碰到左右横向边界时，自动反向飞行
     */
    public void forward() {
        // 根据速度增量更新坐标
        locationX += speedX;
        locationY += speedY;
        
        // 检测是否触碰左右边界
        if (locationX <= 0 || locationX >= Main.WINDOW_WIDTH) {
            // 横向超出边界后反向飞行
            speedX = -speedX;
        }
    }

    /**
     * 碰撞检测方法：判断当前对象是否与另一个飞行对象发生碰撞
     * 判定规则：当对方坐标进入我方范围，且覆盖区域有交叉即判定为撞击
     * <br>
     * 非飞机对象（如子弹）的碰撞区域：
     *   横向：[x - width/2, x + width/2]
     *   纵向：[y - height/2, y + height/2]
     * <br>
     * 飞机对象（如英雄机、敌机）的碰撞区域（缩小 Y 轴范围以提高游戏体验）：
     *   横向：[x - width/2, x + width/2]
     *   纵向：[y - height/4, y + height/4]
     *
     * @param flyingObject 撞击的对方飞行对象
     * @return true: 我方被击中/发生碰撞; false: 我方未被击中
     */
    public boolean crash(AbstractFlyingObject flyingObject) {
        // 缩放因子，用于控制 Y 轴方向的碰撞区域范围
        // 飞机类型使用 factor=2，使碰撞区域在 Y 轴方向缩小一半（更宽松的判断）
        int factor = this instanceof AbstractAircraft ? 2 : 1; // 我方缩放因子
        int fFactor = flyingObject instanceof AbstractAircraft ? 2 : 1; // 对方缩放因子

        // 获取对方的位置、宽度和高度信息
        int x = flyingObject.getLocationX();
        int y = flyingObject.getLocationY();
        int fWidth = flyingObject.getWidth();
        int fHeight = flyingObject.getHeight();

        // 判断两个矩形区域是否有重叠
        // X 轴方向：检查横向范围是否有交集
        // Y 轴方向：检查纵向范围是否有交集（考虑了飞机和非飞机的不同缩放因子）
        return x + (fWidth + this.getWidth()) / 2 > locationX
                && x - (fWidth + this.getWidth()) / 2 < locationX
                && y + (fHeight / fFactor + this.getHeight() / factor) / 2 > locationY
                && y - (fHeight / fFactor + this.getHeight() / factor) / 2 < locationY;
    }

    /**
     * 获取 X 坐标
     * @return 当前 X 坐标值
     */
    public int getLocationX() {
        return locationX;
    }

    /**
     * 获取 Y 坐标
     * @return 当前 Y 坐标值
     */
    public int getLocationY() {
        return locationY;
    }

    /**
     * 设置新的位置坐标
     * @param locationX 新的 X 坐标
     * @param locationY 新的 Y 坐标
     */
    public void setLocation(double locationX, double locationY){
        this.locationX = (int) locationX;
        this.locationY = (int) locationY;
    }

    /**
     * 获取 Y 轴方向的速度
     * @return Y 轴速度值
     */
    public int getSpeedY() {
        return speedY;
    }

    /**
     * 获取对象的图片资源
     * 采用懒加载策略：首次调用时从 ImageManager 获取并缓存
     * @return 对象的 BufferedImage 图片，若未找到则返回 null
     */
    public BufferedImage getImage() {
        if (image == null){
            // 懒加载：从 ImageManager 根据类名获取对应图片
            image = ImageManager.get(this);
        }
        return image;
    }

    /**
     * 获取对象的宽度
     * 采用懒加载策略：首次调用时从图片中获取宽度并缓存
     * @return 对象的宽度（像素）
     */
    public int getWidth() {
        if (width == -1){
            // 若未设置，则查询图片宽度并设置
            width = ImageManager.get(this).getWidth();
        }
        return width;
    }

    /**
     * 获取对象的高度
     * 采用懒加载策略：首次调用时从图片中获取高度并缓存
     * @return 对象的高度（像素）
     */
    public int getHeight() {
        if (height == -1){
            // 若未设置，则查询图片高度并设置
            height = ImageManager.get(this).getHeight();
        }
        return height;
    }


    /**
     * 标记对象为失效状态（消失）
     * 调用后 isValid = false，notValid() 将返回 true
     * 被标记的对象会在游戏循环的后处理阶段被清除
     */
    public void vanish() {
        isValid = false;
    }

    /**
     * 检查对象是否已失效
     * @return true: 对象已失效（应被清除）; false: 对象仍然有效
     */
    public boolean notValid() {
        return !this.isValid;
    }

}

