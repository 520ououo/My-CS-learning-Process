package edu.hitsz.item;

/**
 * 道具效果观察者接口
 * 实现此接口的对象可以接收炸弹和冰冻道具的效果通知
 */
public interface ItemObserver {

    /**
     * 响应炸弹效果
     * @return 获得的分数（坠毁敌机）或造成的伤害（王牌敌机），0表示不受影响
     */
    int onBombEffect();

    /**
     * 响应冰冻效果
     * @return 冰冻时长（毫秒），0表示不受影响，负数表示减速时长
     */
    int onFreezeEffect();
}

