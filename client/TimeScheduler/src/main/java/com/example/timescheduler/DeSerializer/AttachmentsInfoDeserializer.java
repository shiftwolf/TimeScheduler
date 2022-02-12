package com.example.timescheduler.DeSerializer;

import com.example.timescheduler.Model.AttachmentsInfo;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public class AttachmentsInfoDeserializer extends StdDeserializer<AttachmentsInfo> {

    public AttachmentsInfoDeserializer(){this((Class<?>) null);}

    protected AttachmentsInfoDeserializer(Class<?> vc) {
        super(vc);
    }

    protected AttachmentsInfoDeserializer(JavaType valueType) {
        super(valueType);
    }

    protected AttachmentsInfoDeserializer(StdDeserializer<?> src) {
        super(src);
    }

    @Override
    public AttachmentsInfo deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        AttachmentsInfo info = new AttachmentsInfo();
        ObjectCodec codec = jsonParser.getCodec();
        JsonNode node = codec.readTree(jsonParser);

        JsonNode userId = node.get("id");
        Long id = userId.asLong();
        info.setId(id);

        JsonNode filename = node.get("filename");
        String name = filename.asText();
        info.setFilename(name);

        JsonNode eventId = node.get("eventId");
        Long eventId_ = eventId.asLong();
        info.setEventId(eventId_);

        return null;
    }
}
