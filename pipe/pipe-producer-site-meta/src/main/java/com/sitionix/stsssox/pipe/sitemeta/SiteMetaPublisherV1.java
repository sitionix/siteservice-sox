package com.sitionix.stsssox.pipe.sitemeta;

import com.app_afesox.stsssox.events.sitemeta.SiteMetaEnvelope;
import com.app_afesox.stsssox.events.sitemeta.kafka.SitemetaV1Producer;
import com.sitionix.stsssox.domain.Site;
import com.sitionix.stsssox.domain.event.SiteMetaEventPublisher;
import com.sitionix.stsssox.pipe.sitemeta.mapper.SiteMetaEventMapper;
import java.time.Instant;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static java.util.Objects.isNull;

@Slf4j
@Component
@RequiredArgsConstructor
public class SiteMetaPublisherV1 implements SiteMetaEventPublisher {

    private final SitemetaV1Producer producer;
    private final SiteMetaEventMapper mapper;

    @Override
    public void publishSiteCreated(final Site site) {
        log.info("Publishing site created projection event: {}", site);
        if (isNull(site)) {
            return;
        }
        final SiteMetaEnvelope envelope = this.mapper.asCreatedEnvelope(site);
        this.producer.send(site.siteId().toString(), envelope);
    }

    @Override
    public void publishSiteUpdated(final Site site) {
        log.info("Publishing site updated projection event: {}", site);
        if (isNull(site)) {
            return;
        }
        final SiteMetaEnvelope envelope = this.mapper.asUpdatedEnvelope(site);
        this.producer.send(site.siteId().toString(), envelope);
    }

    @Override
    public void publishSiteDeleted(final UUID siteId, final Long ownerUserId, final Instant deletedAt) {
        log.info("Publishing site deleted projection event: siteId={}", siteId);
        if (isNull(siteId) || isNull(ownerUserId) || isNull(deletedAt)) {
            return;
        }
        final SiteMetaEnvelope envelope = this.mapper.asDeletedEnvelope(siteId, ownerUserId, deletedAt);
        this.producer.send(siteId.toString(), envelope);
    }
}
