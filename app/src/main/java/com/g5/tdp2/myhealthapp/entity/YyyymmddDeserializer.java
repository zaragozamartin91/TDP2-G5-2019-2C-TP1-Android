package com.g5.tdp2.myhealthapp.entity;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.g5.tdp2.myhealthapp.util.DateFormatter;

import java.io.IOException;
import java.util.Date;

class YyyymmddDeserializer extends StdDeserializer<Date> {
    public YyyymmddDeserializer() {
        this(null);
    }

    protected YyyymmddDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Date deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        JsonNode node = jp.getCodec().readTree(jp);
        String sbirth = node.asText();
        return DateFormatter.YYYY_MM_DD.deserialize(sbirth);
    }
}

