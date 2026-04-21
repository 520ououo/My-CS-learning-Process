package edu.hitsz.application;

import javax.sound.sampled.*;
import java.io.InputStream;

public class SoundManager {

    private static Clip bgmClip;
    private static Clip bossBgmClip;
    
    private static volatile boolean isBgmPlaying = false;
    private static volatile boolean isBossBgmPlaying = false;

    static {
        try {
            InputStream bgmStream = SoundManager.class.getClassLoader().getResourceAsStream("videos/bgm.wav");
            if (bgmStream != null) {
                bgmClip = AudioSystem.getClip();
                bgmClip.open(AudioSystem.getAudioInputStream(bgmStream));
            } else {
                System.err.println("无法加载 bgm.wav");
            }
            
            InputStream bossBgmStream = SoundManager.class.getClassLoader().getResourceAsStream("videos/bgm_boss.wav");
            if (bossBgmStream != null) {
                bossBgmClip = AudioSystem.getClip();
                bossBgmClip.open(AudioSystem.getAudioInputStream(bossBgmStream));
            } else {
                System.err.println("无法加载 bgm_boss.wav");
            }
        } catch (Exception e) {
            System.err.println("背景音乐加载失败");
            e.printStackTrace();
        }
    }

    public static synchronized void playBGM() {
        stopBossBGM();
        if (isBgmPlaying || bgmClip == null) {
            return;
        }
        
        Thread bgmThread = new Thread(() -> {
            try {
                synchronized (bgmClip) {
                    bgmClip.setFramePosition(0);
                    bgmClip.loop(Clip.LOOP_CONTINUOUSLY);
                    bgmClip.start();
                }
                isBgmPlaying = true;
                Thread.currentThread().join();
            } catch (Exception e) {
                System.err.println("普通 BGM 播放失败");
                e.printStackTrace();
            }
        });
        bgmThread.setDaemon(false);
        bgmThread.setName("BGM-Thread");
        bgmThread.start();
    }

    public static synchronized void playBossBGM() {
        stopBGM();
        if (isBossBgmPlaying || bossBgmClip == null) {
            return;
        }
        
        Thread bossBgmThread = new Thread(() -> {
            try {
                synchronized (bossBgmClip) {
                    bossBgmClip.setFramePosition(0);
                    bossBgmClip.loop(Clip.LOOP_CONTINUOUSLY);
                    bossBgmClip.start();
                }
                isBossBgmPlaying = true;
                Thread.currentThread().join();
            } catch (Exception e) {
                System.err.println("Boss BGM 播放失败");
                e.printStackTrace();
            }
        });
        bossBgmThread.setDaemon(false);
        bossBgmThread.setName("Boss-BGM-Thread");
        bossBgmThread.start();
    }

    public static synchronized void stopBGM() {
        if (isBgmPlaying && bgmClip != null) {
            bgmClip.stop();
            bgmClip.setFramePosition(0);
            isBgmPlaying = false;
        }
    }

    public static synchronized void stopBossBGM() {
        if (isBossBgmPlaying && bossBgmClip != null) {
            bossBgmClip.stop();
            bossBgmClip.setFramePosition(0);
            isBossBgmPlaying = false;
        }
    }

    public static void playSound(String filePath) {
        Thread soundThread = new Thread(() -> {
            Clip clip = null;
            try {
                InputStream stream = SoundManager.class.getClassLoader().getResourceAsStream(filePath);
                if (stream == null) {
                    System.err.println("无法加载音效: " + filePath);
                    return;
                }
                
                clip = AudioSystem.getClip();
                clip.open(AudioSystem.getAudioInputStream(stream));
                clip.start();
                
                while (clip.isRunning()) {
                    Thread.sleep(10);
                }
            } catch (Exception e) {
                System.err.println("音效播放失败: " + filePath);
                e.printStackTrace();
            } finally {
                if (clip != null) {
                    clip.close();
                }
            }
        });
        soundThread.setDaemon(false);
        soundThread.setName("Sound-Thread-" + filePath);
        soundThread.start();
    }

    public static void playBulletHit() {
        playSound("videos/bullet_hit.wav");
    }

    public static void playBombExplosion() {
        playSound("videos/bomb_explosion.wav");
    }

    public static void playGetSupply() {
        playSound("videos/get_supply.wav");
    }

    public static void playGameOver() {
        stopBGM();
        stopBossBGM();
        playSound("videos/game_over.wav");
    }

    public static void stopAll() {
        stopBGM();
        stopBossBGM();
    }
}
