package com.poethan.gear.module.db;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.poethan.gear.utils.EzDate;

import java.io.IOException;

public class EzDateSerializer extends JsonSerializer<EzDate> {
    @Override
    public void serialize(EzDate ezDate, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        jsonGenerator.writeString(ezDate.toString());
    }
}
