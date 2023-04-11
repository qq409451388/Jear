package com.poethan.jear.jdbc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.poethan.jear.utils.EzDate;
import com.poethan.utils.EzDateDeserializer;
import com.poethan.utils.EzDateSerializer;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@JsonIgnoreProperties
abstract public class BaseDO<T> implements Serializable {
    @Column(name = "id")
    private T id;

    @JsonSerialize(using = EzDateSerializer.class)
    @JsonDeserialize(using = EzDateDeserializer.class)
    @Column(name = "create_time")
    private EzDate createTime;

    @JsonSerialize(using = EzDateSerializer.class)
    @JsonDeserialize(using = EzDateDeserializer.class)
    @Column(name = "update_time")
    private EzDate updateTime;

    public void setCreateTime(Integer timeStamp) {
        this.createTime = new EzDate(timeStamp);
    }

    public void setUpdateTime(Integer timeStamp) {
        this.updateTime = new EzDate(timeStamp);
    }

    public void setCreateTime(EzDate ezDate) {
        this.createTime = ezDate;
    }

    public void setUpdateTime(EzDate ezDate) {
        this.updateTime = ezDate;
    }

    public void setCreateTime(LocalDateTime localDateTime) {
        this.createTime = new EzDate(localDateTime);
    }

    public void setUpdateTime(LocalDateTime localDateTime) {
        this.updateTime = new EzDate(localDateTime);
    }

}