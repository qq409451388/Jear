package com.poethan.jear.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
abstract public class BaseDTO implements Serializable {
}
