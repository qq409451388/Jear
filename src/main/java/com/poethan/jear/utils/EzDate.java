package com.poethan.jear.utils;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class EzDate {
    public final static String ZERO_TIME = "0000-00-00 00:00:00";

    private LocalDateTime localDateTime;
    private String dateTimeString;

    public EzDate(int timeStamp){
        if (0 == timeStamp) {
            this.localDateTime = null;
            this.dateTimeString = ZERO_TIME;
        } else {
            this.localDateTime = LocalDateTime.ofEpochSecond(timeStamp, 0, ZoneOffset.of("+8"));
            this.dateTimeString = this.toString();
        }
    }

    public EzDate(long timeStamp){
        if (0 == timeStamp) {
            this.localDateTime = null;
            this.dateTimeString = ZERO_TIME;
        } else {
            this.localDateTime = LocalDateTime.ofEpochSecond(timeStamp/1000, 0, ZoneOffset.of("+8"));
            this.dateTimeString = this.toString();
        }
    }

    public EzDate(LocalDateTime localDateTime) {
        if (null == localDateTime) {
            this.dateTimeString = ZERO_TIME;
        } else {
            this.dateTimeString = this.toString();
        }
        this.localDateTime = localDateTime;
    }

    public int getYear() {
        return this.localDateTime.getYear();
    }

    public int getMonth() {
        return this.localDateTime.getMonthValue();
    }

    public int getDay() {
        return this.localDateTime.getDayOfMonth();
    }

    public int getHour() {
        return this.localDateTime.getHour();
    }

    public int getMinute() {
        return this.localDateTime.getMinute();
    }

    public int getSecond() {
        return this.localDateTime.getSecond();
    }

    private String format(String template) {
        return localDateTime.format(DateTimeFormatter.ofPattern(template));
    }

    public String toString(){
        if (ZERO_TIME.equals(this.dateTimeString)) {
            return ZERO_TIME;
        }
        return this.format("yyyy-MM-dd HH:mm:ss");
    }
}
