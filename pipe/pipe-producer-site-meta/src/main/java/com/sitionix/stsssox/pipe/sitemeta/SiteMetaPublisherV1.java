package com.sitionix.stsssox.pipe.sitemeta;

import com.app_afesox.stsssox.events.sitemeta.SiteMetaEnvelope;
import com.app_afesox.stsssox.events.sitemeta.kafka.SitemetaV1Producer;
import com.sitionix.forge.outbox.core.model.Event;
import com.sitionix.forge.outbox.core.port.ForgeTypedOutboxEventPublisher;
import com.sitionix.stsssox.domain.event.payload.SiteCreatedPayload;
import com.sitionix.stsssox.pipe.sitemeta.mapper.SiteMetaEventMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SiteMetaPublisherV1 extends ForgeTypedOutboxEventPublisher<SiteCreatedPayload> {

    private final SitemetaV1Producer producer;
    private final SiteMetaEventMapper mapper;

    public SiteMetaPublisherV1(final SitemetaV1Producer producer,
                               final SiteMetaEventMapper mapper) {
        super(SiteCreatedPayload.class);
        this.producer = producer;
        this.mapper = mapper;
    }

    @Override
    protected void publish(final Event<SiteCreatedPayload> event) {
        log.info("Publish site meta event");
        final SiteMetaEnvelope envelope = this.mapper.asEnvelope(event);
        final String key = event.getPayload().site().siteId().toString();
        this.producer.send(key, envelope);
        log.info("Site meta event published type={} idempotencyId={}", event.getEventType(), event.getIdempotencyId());
    }
}
