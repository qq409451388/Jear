package com.poethan.jear.jdbc;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import java.io.Serializable;

@Getter
@Setter
public class AbstractDO<T> implements Serializable {
    @Column(name = "id")
    private T id;
}
