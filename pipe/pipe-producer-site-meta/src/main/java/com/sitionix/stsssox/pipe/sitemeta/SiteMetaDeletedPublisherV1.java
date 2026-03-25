package com.sitionix.stsssox.pipe.sitemeta;

import com.app_afesox.stsssox.events.sitemeta.SiteMetaEnvelope;
import com.app_afesox.stsssox.events.sitemeta.kafka.SitemetaV1Producer;
import com.sitionix.forge.outbox.core.model.Event;
import com.sitionix.forge.outbox.core.port.ForgeOutboxEventPublisher;
import com.sitionix.stsssox.domain.event.payload.SiteDeletedPayload;
import com.sitionix.stsssox.pipe.sitemeta.mapper.SiteMetaEventMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SiteMetaDeletedPublisherV1 implements ForgeOutboxEventPublisher<SiteDeletedPayload> {

    private final SitemetaV1Producer producer;
    private final SiteMetaEventMapper mapper;

    @Override
    public void publish(final Event<SiteDeletedPayload> event) {
        log.info("Publish site meta event");
        final SiteMetaEnvelope envelope = this.mapper.asEnvelope(event);
        final String key = event.getPayload().siteId().toString();
        this.producer.send(key, envelope);
        log.info("Site meta event published type={} idempotencyId={}", event.getEventType(), event.getIdempotencyId());
    }
}
