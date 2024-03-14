package com.poethan.jear.core.utils;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

//@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
public class EzDate implements Serializable {
    public final static String ZERO_TIME = "0000-00-00 00:00:00";
    private final static String FORMAT = "yyyy-MM-dd HH:mm:ss";
    private LocalDateTime localDateTime;
    private String dateTimeString;

    public static EzDate now() {
        return new EzDate(LocalDateTime.now());
    }
    public static EzDate empty() {
        return new EzDate(0);
    }

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
        this.localDateTime = localDateTime;
        if (null == localDateTime) {
            this.dateTimeString = ZERO_TIME;
        } else {
            this.dateTimeString = this.toString();
        }
    }

    public EzDate(String dateTimeString) {
        this.localDateTime = LocalDateTime.parse(dateTimeString, DateTimeFormatter.ofPattern(FORMAT));
        this.dateTimeString = dateTimeString;
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
