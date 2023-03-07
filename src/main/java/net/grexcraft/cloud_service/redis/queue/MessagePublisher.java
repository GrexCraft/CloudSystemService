package net.grexcraft.cloud_service.redis.queue;

public interface MessagePublisher {
    void publish(final String message);
}