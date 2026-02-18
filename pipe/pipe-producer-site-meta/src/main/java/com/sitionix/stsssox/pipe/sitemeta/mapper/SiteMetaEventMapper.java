package com.sitionix.stsssox.pipe.sitemeta.mapper;

import com.app_afesox.events.Metadata;
import com.app_afesox.stsssox.events.sitemeta.SiteCreatedEvent;
import com.app_afesox.stsssox.events.sitemeta.SiteDeletedEvent;
import com.app_afesox.stsssox.events.sitemeta.SiteMetaEnvelope;
import com.app_afesox.stsssox.events.sitemeta.SiteStatusDTO;
import com.app_afesox.stsssox.events.sitemeta.SiteTypeDTO;
import com.app_afesox.stsssox.events.sitemeta.SiteUpdatedEvent;
import com.sitionix.stsssox.domain.Site;
import java.time.Instant;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class SiteMetaEventMapper {

    public SiteMetaEnvelope asCreatedEnvelope(final Site site) {
        final SiteCreatedEvent payload = SiteCreatedEvent.newBuilder()
                .setSiteId(site.siteId().toString())
                .setOwnerUserId(site.userId())
                .setName(site.name())
                .setStatus(this.asSiteStatus(site))
                .setType(this.asSiteType(site))
                .setDescription(site.description())
                .setCreatedAt(this.asIsoInstant(site.createdAt()))
                .setUpdatedAt(this.asIsoInstant(site.updatedAt()))
                .build();
        return SiteMetaEnvelope.newBuilder()
                .setMetadata(this.asMetadata("SITE_CREATED"))
                .setPayload(payload)
                .build();
    }

    public SiteMetaEnvelope asUpdatedEnvelope(final Site site) {
        final SiteUpdatedEvent payload = SiteUpdatedEvent.newBuilder()
                .setSiteId(site.siteId().toString())
                .setOwnerUserId(site.userId())
                .setName(site.name())
                .setStatus(this.asSiteStatus(site))
                .setType(this.asSiteType(site))
                .setDescription(site.description())
                .setUpdatedAt(this.asIsoInstant(site.updatedAt()))
                .build();
        return SiteMetaEnvelope.newBuilder()
                .setMetadata(this.asMetadata("SITE_UPDATED"))
                .setPayload(payload)
                .build();
    }

    public SiteMetaEnvelope asDeletedEnvelope(final UUID siteId, final Long ownerUserId, final Instant deletedAt) {
        final SiteDeletedEvent payload = SiteDeletedEvent.newBuilder()
                .setSiteId(siteId.toString())
                .setOwnerUserId(ownerUserId)
                .setDeletedAt(this.asIsoInstant(deletedAt))
                .build();
        return SiteMetaEnvelope.newBuilder()
                .setMetadata(this.asMetadata("SITE_DELETED"))
                .setPayload(payload)
                .build();
    }

    private SiteStatusDTO asSiteStatus(final Site site) {
        if (site.status() == null) {
            return null;
        }
        return SiteStatusDTO.valueOf(site.status().name());
    }

    private SiteTypeDTO asSiteType(final Site site) {
        if (site.type() == null) {
            return null;
        }
        return SiteTypeDTO.valueOf(site.type().name());
    }

    private String asIsoInstant(final Instant instant) {
        if (instant == null) {
            return null;
        }
        return instant.toString();
    }

    private Metadata asMetadata(final String eventType) {
        return Metadata.newBuilder()
                .setIdempotencyId(UUID.randomUUID().toString())
                .setCreatedAt(Instant.now().toEpochMilli())
                .setEventType(eventType)
                .build();
    }
}
