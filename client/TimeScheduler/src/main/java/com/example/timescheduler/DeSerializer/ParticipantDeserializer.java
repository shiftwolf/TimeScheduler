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

/**
 * When we deserialize a JSON event to a Model.Event object we also deserialize user entities.
 * To specify the conversion of the JSON attributes names to the java object names we need this class.
 */
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

    /**
     * Here we specify the conversion of the names in the JSON object to the Java object, we also
     * @param jsonParser provides read only access to JSON data
     * @param deserializationContext state information of deserialization
     * @return Model.User object
     * @throws IOException - Occurs if in the client.send or in the mapper.readValue command an error arises
     * @throws InterruptedException - Occurs if in the client.send a thread has been interrupted
     */
    @Override
    public User deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        User user = new User();
        ObjectCodec codec = jsonParser.getCodec();
        JsonNode node = codec.readTree(jsonParser);

        var userId = node.get("id");
        Long id = userId.asLong();
        user.setId(id);
        var username = node.get("username");
        String username_ = username.asText();
        user.setUsername(username_);
        var name = node.get("name");
        String name_ = name.asText();
        user.setName(name_);
        var email = node.get("email");
        String mail = email.asText();
        user.setEmail(mail);
        var is_admin = node.get("admin");
        boolean admin = is_admin.asBoolean();
        user.setAdmin(admin);

        return user;
    }

}
