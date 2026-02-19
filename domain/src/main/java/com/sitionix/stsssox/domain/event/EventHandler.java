package com.sitionix.stsssox.domain.event;

/**
 * Generic publisher contract for domain events.
 *
 * @param <E> event payload type.
 */
public interface EventHandler<E> {

    /**
     * Publishes an event envelope.
     *
     * @param event event envelope with payload and metadata.
     */
    void publish(Event<E> event);
}
