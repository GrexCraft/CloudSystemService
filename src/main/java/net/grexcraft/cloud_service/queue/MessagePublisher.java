package net.grexcraft.cloud_service.queue;

public interface MessagePublisher {
    void publish(final String message);
}