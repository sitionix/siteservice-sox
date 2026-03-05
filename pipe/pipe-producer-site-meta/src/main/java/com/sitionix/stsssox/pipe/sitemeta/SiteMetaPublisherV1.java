package com.sitionix.stsssox.pipe.sitemeta;

import com.app_afesox.stsssox.events.sitemeta.SiteMetaEnvelope;
import com.app_afesox.stsssox.events.sitemeta.kafka.SitemetaV1Producer;
import com.sitionix.forge.outbox.core.model.Event;
import com.sitionix.forge.outbox.core.port.ForgeOutboxEventPublisher;
import com.sitionix.stsssox.domain.event.payload.SiteCreatedPayload;
import com.sitionix.stsssox.pipe.sitemeta.mapper.SiteMetaEventMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SiteMetaPublisherV1 implements ForgeOutboxEventPublisher<SiteCreatedPayload> {

    private final SitemetaV1Producer producer;
    private final SiteMetaEventMapper mapper;

    @Override
    public String eventType() {
        return SiteCreatedPayload.EVENT_TYPE;
    }

    @Override
    public Class<SiteCreatedPayload> payloadType() {
        return SiteCreatedPayload.class;
    }

    @Override
    public void publish(final Event<SiteCreatedPayload> event) {
        log.info("Publishing site meta projection event: {}", event);
        if (event == null || event.getPayload() == null || event.getPayload().site() == null
                || event.getPayload().site().siteId() == null) {
            throw new IllegalArgumentException("SiteCreatedPayload.site.siteId is required");
        }

        final SiteMetaEnvelope envelope = this.mapper.asEnvelope(event);
        this.producer.send(event.getPayload().site().siteId().toString(), envelope);
    }
}
