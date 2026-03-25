package edu.hitsz.application;

import edu.hitsz.aircraft.HeroAircraft;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * 英雄机控制类，负责处理玩家与游戏的交互
 * 通过监听鼠标事件来控制英雄机的移动：
 * 1. 监听鼠标拖拽事件
 * 2. 根据鼠标位置更新英雄机坐标
 * 3. 防止英雄机移出游戏窗口边界
 * @author hitsz
 */
public class HeroController {
    /** 游戏主面板对象引用 */
    private Game game;
    /** 玩家控制的英雄机对象 */
    private HeroAircraft heroAircraft;
    /** 鼠标适配器，用于处理鼠标事件 */
    private MouseAdapter mouseAdapter;

    /**
     * 构造函数：初始化控制器并注册鼠标监听器
     * @param game 游戏主面板对象
     * @param heroAircraft 要控制的英雄机对象
     */
    public HeroController(Game game, HeroAircraft heroAircraft){
        this.game = game;
        this.heroAircraft = heroAircraft;

        // 创建鼠标适配器，处理鼠标拖拽事件
        mouseAdapter = new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                // 获取鼠标当前位置坐标
                int x = e.getX();
                int y = e.getY();
                if ( x<0 || x>Main.WINDOW_WIDTH || y<0 || y>Main.WINDOW_HEIGHT){
                    // 防止超出边界
                    return;
                }
                
                // 更新英雄机位置到鼠标所在位置
                heroAircraft.setLocation(x, y);
            }
        };

        // 将鼠标监听器注册到游戏面板
        game.addMouseListener(mouseAdapter);
        game.addMouseMotionListener(mouseAdapter);
    }

}
