package edu.hitsz.application;

import javax.swing.*;
import java.awt.*;

/**
 * 飞机大战游戏的主程序入口类
 * 负责创建和初始化游戏窗口，设置窗口属性，并启动游戏
 * @author hitsz
 */
public class Main {

    /** 游戏窗口的宽度（像素） */
    public static final int WINDOW_WIDTH = 512;

    /** 游戏窗口的高度（像素） */
    public static final int WINDOW_HEIGHT = 768;

    /**
     * 程序主方法 - 游戏启动的入口点
     * @param args 命令行参数（未使用）
     */
    public static void main(String[] args) {

        System.out.println("Hello Aircraft War");

        // 获得屏幕的分辨率，用于计算窗口居中位置
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        // 创建游戏主窗口框架
        JFrame frame = new JFrame("Aircraft War");

        // 设置窗口的固定大小，禁止用户调整
        frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        frame.setResizable(false);

        // 设置窗口的位置：水平居中，垂直方向靠上（距离顶部 0 像素）
        frame.setBounds(((int) screenSize.getWidth() - WINDOW_WIDTH) / 2, 0,
                WINDOW_WIDTH, WINDOW_HEIGHT);

        // 设置窗口关闭操作：点击关闭按钮时退出整个应用程序
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 创建游戏面板对象
        Game game = new Game();

        // 将游戏面板添加到窗口中
        frame.add(game);

        // 设置窗口可见，显示窗口
        frame.setVisible(true);

        // 启动游戏循环，开始游戏逻辑
        game.action();
    }
}

