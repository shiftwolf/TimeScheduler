package com.example.timescheduler.DeSerializer;

import com.example.timescheduler.Model.Event;
import com.example.timescheduler.Model.User;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class CustomEditEventSerializer extends StdSerializer<Event> {

    public CustomEditEventSerializer() {
        this(null);
    }

    public CustomEditEventSerializer(Class<Event> t) {
        super(t);
    }

    @Override
    public void serialize(Event event, JsonGenerator jsonGenerator, SerializerProvider serializer) throws IOException {
        String participants = "";
        if(event.getParticipantsEntities() != null) {
            for(User user: event.getParticipantsEntities()){
                participants += (user.getId()+",");
            }
            if (participants.length() > 0) {
                participants = participants.substring(0, participants.length() - 1);
            }
        }
        System.out.println(participants);

        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("id", event.getId());
        jsonGenerator.writeStringField("name", event.getName());
        jsonGenerator.writeNumberField("duration", event.getDuration().getTime());
        jsonGenerator.writeNumberField("date", event.getDate().getTime());
        jsonGenerator.writeStringField("location", event.getLocation());
        jsonGenerator.writeNumberField("priority", event.getPriority().ordinal());
        jsonGenerator.writeFieldName("participants");
        jsonGenerator.writeStartArray();
        jsonGenerator.writeRaw(participants);
        jsonGenerator.writeEndArray();
        jsonGenerator.writeNumberField("reminder", event.getReminder().getTime());
        jsonGenerator.writeEndObject();
    }
}
