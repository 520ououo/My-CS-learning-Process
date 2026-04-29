package edu.hitsz.item;

import edu.hitsz.aircraft.HeroAircraft;

import java.util.ArrayList;
import java.util.List;

public class FreezeItem extends BaseItem {

    private int freezeDuration;
    
    private List<ItemObserver> observers = new ArrayList<>();

    public FreezeItem(int locationX, int locationY, int speedX, int speedY, int freezeDuration) {
        super(locationX, locationY, speedX, speedY);
        this.freezeDuration = freezeDuration;
    }

    public void addObserver(ItemObserver observer) {
        observers.add(observer);
    }

    public void notifyObservers() {
        for (ItemObserver observer : observers) {
            observer.onFreezeEffect();
        }
    }

    public List<ItemObserver> getObservers() {
        return observers;
    }

    @Override
    public void active(HeroAircraft hero) {
        notifyObservers();
        System.out.println("Freeze active!");
    }

}
