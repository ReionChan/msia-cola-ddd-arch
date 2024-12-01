package io.github.reionchan.core.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * BigDecimal 自定义序列化器
 *
 * @author Reion
 * @date 2023-12-16
 **/
public class BigDecimalSerializer extends JsonSerializer<BigDecimal> {
    @Override
    public void serialize(BigDecimal bigDecimal, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (bigDecimal == null) {
            jsonGenerator.writeNull();
        } else {
            jsonGenerator.writeString(bigDecimal.setScale(2).toString());
        }
    }
}
