package edu.hitsz.application;


import edu.hitsz.aircraft.Boss;
import edu.hitsz.aircraft.EliteEnemy;
import edu.hitsz.aircraft.EliteplusEnemy;
import edu.hitsz.aircraft.EliteproEnemy;
import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.aircraft.MobEnemy;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.bullet.HeroBullet;
import edu.hitsz.item.BloodItem;
import edu.hitsz.item.BulletItem;
import edu.hitsz.item.SuperBulletItem;
import edu.hitsz.item.BombItem;
import edu.hitsz.item.FreezeItem;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 图片资源管理器，统一管理游戏中的所有图片资源
 * 提供以下功能：
 * 1. 在游戏启动时一次性加载所有图片资源到内存
 * 2. 通过类名或对象获取对应的图片，支持多态访问
 * 3. 提供公共图片资源的静态访问方法
 * @author hitsz
 */
public class ImageManager {

    /**
     * 类名到图片的映射表
     * 存储各基类（飞机、子弹等）与其对应图片的映射关系
     * 可通过 CLASSNAME_IMAGE_MAP.get(obj.getClass().getName()) 获得 obj 所属类型对应的图片
     */
    private static final Map<String, BufferedImage> CLASSNAME_IMAGE_MAP = new HashMap<>();

    /** 游戏背景图片 */
    public static BufferedImage BACKGROUND_IMAGE;
    /** 英雄机图片 */
    public static BufferedImage HERO_IMAGE;
    /** 英雄机子弹图片 */
    public static BufferedImage HERO_BULLET_IMAGE;
    /** 敌机子弹图片 */
    public static BufferedImage ENEMY_BULLET_IMAGE;
    /** 普通敌机（MobEnemy）图片 */
    public static BufferedImage MOB_ENEMY_IMAGE;
    /** 精英敌机（EliteEnemy）图片 */
    public static BufferedImage ELITE_ENEMY_IMAGE;
    /** 高级精英敌机（EliteplusEnemy）图片 */
    public static BufferedImage ELITEPLUS_ENEMY_IMAGE;
    /** 超级精英敌机（EliteproEnemy）图片 */
    public static BufferedImage ELITEPRO_ENEMY_IMAGE;
    /** Boss敌机图片 */
    public static BufferedImage BOSS_ENEMY_IMAGE;
    /** 加血道具图片 */
    public static BufferedImage BLOOD_ITEM_IMAGE;
    /** 火力道具图片 */
    public static BufferedImage BULLET_ITEM_IMAGE;
    /** 超级火力道具图片 */
    public static BufferedImage SUPER_BULLET_ITEM_IMAGE;
    /** 炸弹道具图片 */
    public static BufferedImage BOMB_ITEM_IMAGE;
    /** 冰冻道具图片 */
    public static BufferedImage FREEZE_ITEM_IMAGE;

    // 静态代码块：在类加载时执行，负责初始化所有图片资源
    static {
        try {
            // 加载背景图片
            BACKGROUND_IMAGE = ImageIO.read(new FileInputStream("src/images/bg.jpg"));

            // 加载各种游戏对象的图片
            HERO_IMAGE = ImageIO.read(new FileInputStream("src/images/hero.png"));
            MOB_ENEMY_IMAGE = ImageIO.read(new FileInputStream("src/images/mob.png"));
            HERO_BULLET_IMAGE = ImageIO.read(new FileInputStream("src/images/bullet_hero.png"));
            ENEMY_BULLET_IMAGE = ImageIO.read(new FileInputStream("src/images/bullet_enemy.png"));
            ELITE_ENEMY_IMAGE = ImageIO.read(new FileInputStream("src/images/elite.png"));
            ELITEPLUS_ENEMY_IMAGE = ImageIO.read(new FileInputStream("src/images/elitePlus.png"));
            ELITEPRO_ENEMY_IMAGE = ImageIO.read(new FileInputStream("src/images/elitePro.png"));
            BOSS_ENEMY_IMAGE = ImageIO.read(new FileInputStream("src/images/boss.png"));
            BLOOD_ITEM_IMAGE = ImageIO.read(new FileInputStream("src/images/prop_blood.png"));
            BULLET_ITEM_IMAGE = ImageIO.read(new FileInputStream("src/images/prop_bullet.png"));
            SUPER_BULLET_ITEM_IMAGE = ImageIO.read(new FileInputStream("src/images/prop_bulletPlus.png"));
            BOMB_ITEM_IMAGE = ImageIO.read(new FileInputStream("src/images/prop_bomb.png"));
            FREEZE_ITEM_IMAGE = ImageIO.read(new FileInputStream("src/images/prop_freeze.png"));

            // 建立类名与图片的映射关系，便于后续通过反射获取
            CLASSNAME_IMAGE_MAP.put(HeroAircraft.class.getName(), HERO_IMAGE);
            CLASSNAME_IMAGE_MAP.put(MobEnemy.class.getName(), MOB_ENEMY_IMAGE);
            CLASSNAME_IMAGE_MAP.put(EliteEnemy.class.getName(), ELITE_ENEMY_IMAGE);
            CLASSNAME_IMAGE_MAP.put(EliteplusEnemy.class.getName(), ELITEPLUS_ENEMY_IMAGE);
            CLASSNAME_IMAGE_MAP.put(EliteproEnemy.class.getName(), ELITEPRO_ENEMY_IMAGE);
            CLASSNAME_IMAGE_MAP.put(Boss.class.getName(), BOSS_ENEMY_IMAGE);
            CLASSNAME_IMAGE_MAP.put(HeroBullet.class.getName(), HERO_BULLET_IMAGE);
            CLASSNAME_IMAGE_MAP.put(EnemyBullet.class.getName(), ENEMY_BULLET_IMAGE);
            CLASSNAME_IMAGE_MAP.put(BloodItem.class.getName(), BLOOD_ITEM_IMAGE);
            CLASSNAME_IMAGE_MAP.put(BulletItem.class.getName(), BULLET_ITEM_IMAGE);
            CLASSNAME_IMAGE_MAP.put(SuperBulletItem.class.getName(), SUPER_BULLET_ITEM_IMAGE);
            CLASSNAME_IMAGE_MAP.put(BombItem.class.getName(), BOMB_ITEM_IMAGE);
            CLASSNAME_IMAGE_MAP.put(FreezeItem.class.getName(), FREEZE_ITEM_IMAGE);

        } catch (IOException e) {
            // 图片加载失败时打印错误信息并退出程序
            e.printStackTrace();
            System.exit(-1);
        }
    }

    /**
     * 根据类名获取对应的图片
     * @param className 类的完整名称（包含包名）
     * @return 对应的 BufferedImage 图片对象，若不存在则返回 null
     */
    public static BufferedImage get(String className){
        return CLASSNAME_IMAGE_MAP.get(className);
    }

    /**
     * 根据对象实例获取其对应的图片
     * 利用反射机制自动获取对象的类名并查找图片
     * @param obj 游戏对象实例（如 HeroAircraft、MobEnemy 等）
     * @return 对应的 BufferedImage 图片对象，若 obj 为 null 或不存在对应图片则返回 null
     */
    public static BufferedImage get(Object obj){
        if (obj == null){
            return null;
        }
        return get(obj.getClass().getName());
    }

}
