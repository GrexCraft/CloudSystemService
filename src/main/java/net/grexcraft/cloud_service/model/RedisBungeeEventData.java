package net.grexcraft.cloud_service.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.Data;

import java.io.Serializable;


@Data
public class RedisBungeeEventData implements Serializable {

    public enum BungeeEventType {
        REGISTER, REMOVE
    }

    private String name;
    private String hostname;
    private String port;
    private BungeeEventType eventType;

    public String toJson() {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        try {
            return ow.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
