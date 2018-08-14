package com.example.reinaldomoraes.testetcc;

import java.util.Date;

public class Tap {

    private Hand hand;
    private int intensity;
    private Date time;
    private long interval; //in milliseconds

    public Tap(Date time) {
        this.time = time;
    }

    public Tap(Hand hand, Date time) {
        this.hand = hand;
        this.time = time;
    }

    public Hand getHand() {
        return hand;
    }

    public void setHand(Hand hand) {
        this.hand = hand;
    }

    public int getIntensity() {
        return intensity;
    }

    public void setIntensity(int intensity) {
        this.intensity = intensity;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public long getInterval() {
        return interval;
    }

    public void setInterval(long interval) {
        this.interval = interval;
    }
}
