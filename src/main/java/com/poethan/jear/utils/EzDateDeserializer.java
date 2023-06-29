package com.poethan.jear.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class EzDateDeserializer extends JsonDeserializer<EzDate> {
    @Override
    public EzDate deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        if (jsonParser.getCurrentToken().equals(JsonToken.VALUE_STRING)) {
            if (EzDataUtils.isNumberValue(jsonParser.getText())) {
                return new EzDate(jsonParser.getIntValue());
            }
            return new EzDate(jsonParser.getText());
        } else if (jsonParser.getCurrentToken().equals(JsonToken.VALUE_NUMBER_INT)) {
            return new EzDate(jsonParser.getIntValue());
        }
        return EzDate.empty();
    }
}
