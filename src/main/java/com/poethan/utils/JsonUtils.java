package com.poethan.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

public class JsonUtils {
    public static <T> T decode(String json, Class<T> tClass){
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(json, tClass);
        }catch(Exception e){
            return null;
        }
    }

    public static <T> List<T> decodeList(String json, Class<T> tClass){
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(json, new TypeReference<List<T>>() {});
        }catch(Exception e){
            return null;
        }
    }

    public static <T, K> Map<T, K> decodeMap(String json, Class<K> valueClass, Class<T> keyClass){
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(json, new TypeReference<Map<T, K>>() {});
        }catch(Exception e){
            return null;
        }
    }

    public static <T> Map<String, T> decodeMap(String json, Class<T> valueClass){
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(json, new TypeReference<Map<String, T>>() {});
        }catch(Exception e){
            return null;
        }
    }

    public static String encode(Object obj){
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(obj);
        }catch(Exception e){
            return "";
        }
    }

}

