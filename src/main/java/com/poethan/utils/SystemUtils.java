package com.poethan.gear.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class SystemUtils {
    private static final Map<String, Timer> TIMER_MAP = new ConcurrentHashMap<>();

    public static String getTraceId(){
        return EncodeUtils.md5(System.currentTimeMillis()+getThreadName());
    }

    public static int random(int start, int end) {
        String str = getTraceId();
        int s = Math.abs(str.hashCode());
        int d = s%end;
        if (d >= start) {
            return d;
        } else {
            int r = s%start;
            return r+start;
        }
    }

    public static int currentTimeStamp() {
        return (int) System.currentTimeMillis()/1000;
    }

    public static void startTimeKeeping(){
        getTimmer().start();
    }
    public static void endTimeKeeping(){
        getTimmer().end();
    }
    public static long consume(){
        return getTimmer().consume();
    }
    public static void closeTimeKeeping(){
        TIMER_MAP.remove(getThreadName());
    }

    private static SystemUtils.Timer getTimmer(){
        String t = getThreadName();
        Timer timer = TIMER_MAP.get(t);
        if(Objects.isNull(timer)){
            timer = new Timer();
            TIMER_MAP.put(t, timer);
        }
        return timer;
    }

    private static String getThreadName(){
        return Thread.currentThread().getName();
    }

    public static void sleep(long millis){
        try{
            Thread.sleep(millis);
        } catch (Exception e) {
            log.error("[Gear] {} Sleep Fail!", SystemUtils.class.getSimpleName());
        }
    }

    static class Timer{
        private long startTime;
        private long endTime;

        public void start(){
            this.startTime = System.currentTimeMillis();
        }

        public void end(){
            this.endTime = System.currentTimeMillis();
        }

        public long consume(){
            DBC.assertNonNull(this.startTime, "TimerKeeping Has Not Started!");
            DBC.assertNonNull(this.endTime, "TimerKeeping Has Not Start!");
            return this.endTime - this.startTime;
        }
    }
}
