package net.grexcraft.cloud.service.queue;

public interface MessagePublisher {
    void publish(final String message);
}