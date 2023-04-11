package com.poethan.jear.module.schduler;

import javax.annotation.PostConstruct;
import java.util.Timer;
import java.util.TimerTask;

abstract public class BaseSchduler extends TimerTask implements Schduler {
    /**
     * 延迟时间
     */
    protected int delayMillis;

    /**
     * 每隔多久执行一次
     */
    protected int periodMillis;
    @PostConstruct
    public void init() {
//        this.start();
    }

    public abstract void initDelay();

    public abstract void initPeriod();

    @Override
    public void start() {
        Timer timer = new Timer();
        this.initDelay();
        this.initPeriod();
        timer.scheduleAtFixedRate(this, delayMillis, periodMillis);
    }
}
