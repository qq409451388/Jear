package com.poethan.jear.jdbc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.poethan.jear.utils.EzDate;
import com.poethan.jear.utils.EzDateDeserializer;
import com.poethan.jear.utils.EzDateSerializer;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.Column;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
abstract public class BaseDO<T> extends AbstractDO<T> implements Serializable {
    @Column(name = "ver")
    private Integer ver;

    @JsonSerialize(using = EzDateSerializer.class)
    @Column(name = "create_time")
    private EzDate createTime;

    @JsonSerialize(using = EzDateSerializer.class)
    @Column(name = "update_time")
    private EzDate updateTime;

    public BaseDO() {
        this.ver = 1;
        EzDate now = EzDate.now();
        this.setCreateTime(now);
        this.setUpdateTime(now);
    }

    void setVer(int ver) {
        this.ver = ver;
    }

    public void setCreateTime(Integer timeStamp) {
        this.createTime = new EzDate(timeStamp);
    }

    public void setUpdateTime(Integer timeStamp) {
        this.updateTime = new EzDate(timeStamp);
    }

    @JsonDeserialize(using = EzDateDeserializer.class)
    public void setCreateTime(EzDate ezDate) {
        this.createTime = ezDate;
    }

    @JsonDeserialize(using = EzDateDeserializer.class)
    public void setUpdateTime(EzDate ezDate) {
        this.updateTime = ezDate;
    }

    public void setCreateTime(LocalDateTime localDateTime) {
        this.createTime = new EzDate(localDateTime);
    }

    public void setUpdateTime(LocalDateTime localDateTime) {
        this.updateTime = new EzDate(localDateTime);
    }

    public void setCreateTime(String dateTimeString) {
        this.createTime = new EzDate(dateTimeString);
    }

    public void setUpdateTime(String dateTimeString) {
        this.updateTime = new EzDate(dateTimeString);
    }

}