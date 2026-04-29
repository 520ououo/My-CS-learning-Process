package edu.hitsz.aircraft;

import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.item.ItemObserver;
import java.util.List;

public abstract class EnemyAircraft extends AbstractAircraft implements ItemObserver {

    protected int score;
    
    protected boolean isFrozen = false;
    protected int originalSpeedY;
    protected long freezeEndTime = 0;
    protected long slowEndTime = 0;
    protected int slowSpeedY = 0;

    public EnemyAircraft(int locationX, int locationY, int speedX, int speedY, int hp, int score) {
        super(locationX, locationY, speedX, speedY, hp);
        this.score = score;
        this.originalSpeedY = speedY;
    }

    public abstract EnemyAircraft createInstance(int locationX, int locationY);

    public int getScore() {
        return score;
    }
    
    @Override
    public int onBombEffect() {
        decreaseHp(9999);
        if (notValid()) {
            return score;
        }
        return 0;
    }
    
    @Override
    public int onFreezeEffect() {
        return 0;
    }
    
    public void applyFreeze(int durationMs) {
        this.isFrozen = true;
        this.freezeEndTime = System.currentTimeMillis() + durationMs;
        this.speedY = 0;
    }
    
    public void applySlow(int durationMs) {
        this.slowEndTime = System.currentTimeMillis() + durationMs;
        this.slowSpeedY = (int) (this.originalSpeedY * 0.3);
        this.speedY = this.slowSpeedY;
    }
    
    public void updateFreezeState() {
        long currentTime = System.currentTimeMillis();
        
        if (isFrozen && currentTime >= freezeEndTime) {
            isFrozen = false;
            if (slowEndTime > currentTime) {
                speedY = slowSpeedY;
            } else {
                speedY = originalSpeedY;
            }
        }
        
        if (slowEndTime > 0 && currentTime >= slowEndTime) {
            slowEndTime = 0;
            if (!isFrozen) {
                speedY = originalSpeedY;
            }
        }
    }
    
    @Override
    public void forward() {
        if (isFrozen) {
            return;
        }
        super.forward();
    }
}

