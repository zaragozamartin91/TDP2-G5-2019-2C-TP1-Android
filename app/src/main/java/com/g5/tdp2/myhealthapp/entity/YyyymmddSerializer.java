package com.g5.tdp2.myhealthapp.entity;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.g5.tdp2.myhealthapp.util.DateFormatter;

import java.io.IOException;
import java.util.Date;

class YyyymmddSerializer extends StdSerializer<Date> {
    protected YyyymmddSerializer() {
        this(null);
    }

    protected YyyymmddSerializer(Class<Date> t) {
        super(t);
    }

    @Override
    public void serialize(Date value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeString(DateFormatter.YYYY_MM_DD.serialize(value));
    }
}
