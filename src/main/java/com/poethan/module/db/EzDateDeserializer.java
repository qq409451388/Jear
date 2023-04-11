package com.poethan.gear.module.db;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.poethan.gear.utils.EzDate;

import java.io.IOException;

public class EzDateDeserializer extends JsonDeserializer<EzDate> {
    @Override
    public EzDate deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        return new EzDate(jsonParser.getIntValue());
    }
}
