package com.example.timescheduler.DeSerializer;

import com.example.timescheduler.Model.User;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public class ParticipantDeserializer extends StdDeserializer<User> {

    public ParticipantDeserializer(){
        this((Class<?>) null);
    }
    protected ParticipantDeserializer(Class<?> vc) {
        super(vc);
    }

    protected ParticipantDeserializer(JavaType valueType) {
        super(valueType);
    }

    protected ParticipantDeserializer(StdDeserializer<?> src) {
        super(src);
    }

    @Override
    public User deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        User user = new User();
        ObjectCodec codec = jsonParser.getCodec();
        JsonNode node = codec.readTree(jsonParser);

        JsonNode userId = node.get("userId");
        Long id = userId.asLong();
        user.setId(id);
        return user;
    }

}
