package com.poethan.gear.module;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
abstract public class BaseVO implements Serializable {
}
