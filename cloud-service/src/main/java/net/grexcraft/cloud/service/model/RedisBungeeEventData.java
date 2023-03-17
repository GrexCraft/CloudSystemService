package net.grexcraft.cloud.service.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;


@RedisHash("RedisBungeeEventData")
@Data
public class RedisBungeeEventData implements Serializable {

    public enum BungeeEventType {
        REGISTER, REMOVE
    }

    @Id
    Long id;

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
