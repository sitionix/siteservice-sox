package com.sitionix.stsssox.pipe.sitemeta;

import com.app_afesox.stsssox.events.sitemeta.SiteMetaEnvelope;
import com.app_afesox.stsssox.events.sitemeta.kafka.SitemetaV1Producer;
import com.sitionix.stsssox.domain.event.Event;
import com.sitionix.stsssox.domain.event.payload.SiteMetaPayload;
import com.sitionix.stsssox.pipe.sitemeta.mapper.SiteMetaEventMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static java.util.Objects.isNull;

@Slf4j
@Component
@RequiredArgsConstructor
public class SiteMetaPublisherV1 {

    private final SitemetaV1Producer producer;
    private final SiteMetaEventMapper mapper;

    public void publish(final Event<SiteMetaPayload> event) {
        log.info("Publishing site meta projection event: {}", event);
        if (isNull(event)) {
            return;
        }

        final SiteMetaEnvelope envelope = this.mapper.asEnvelope(event);
        this.producer.send(event.getId(), envelope);
    }
}
