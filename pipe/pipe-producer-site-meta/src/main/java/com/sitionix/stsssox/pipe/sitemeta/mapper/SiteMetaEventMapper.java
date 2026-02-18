package com.sitionix.stsssox.pipe.sitemeta.mapper;

import com.app_afesox.events.Metadata;
import com.app_afesox.stsssox.events.sitemeta.SiteCreatedEvent;
import com.app_afesox.stsssox.events.sitemeta.SiteDeletedEvent;
import com.app_afesox.stsssox.events.sitemeta.SiteMetaEnvelope;
import com.app_afesox.stsssox.events.sitemeta.SiteStatusDTO;
import com.app_afesox.stsssox.events.sitemeta.SiteTypeDTO;
import com.app_afesox.stsssox.events.sitemeta.SiteUpdatedEvent;
import com.sitionix.stsssox.domain.Site;
import com.sitionix.stsssox.domain.event.Event;
import com.sitionix.stsssox.domain.event.payload.SiteCreatedPayload;
import com.sitionix.stsssox.domain.event.payload.SiteDeletedPayload;
import com.sitionix.stsssox.domain.event.payload.SiteMetaPayload;
import com.sitionix.stsssox.domain.event.payload.SiteUpdatedPayload;
import java.time.Instant;
import org.springframework.stereotype.Component;

@Component
public class SiteMetaEventMapper {

    public SiteMetaEnvelope asEnvelope(final Event<SiteMetaPayload> event) {
        final Object payload = event.getPayload();
        if (payload instanceof SiteCreatedPayload createdPayload) {
            return this.asCreatedEnvelope(event, createdPayload.site());
        }
        if (payload instanceof SiteUpdatedPayload updatedPayload) {
            return this.asUpdatedEnvelope(event, updatedPayload.site());
        }
        if (payload instanceof SiteDeletedPayload deletedPayload) {
            return this.asDeletedEnvelope(event, deletedPayload);
        }
        throw new IllegalArgumentException("Unsupported site meta payload type: " + payload.getClass().getName());
    }

    private SiteMetaEnvelope asCreatedEnvelope(final Event<SiteMetaPayload> event, final Site site) {
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
                .setMetadata(this.asMetadata(event))
                .setPayload(payload)
                .build();
    }

    private SiteMetaEnvelope asUpdatedEnvelope(final Event<SiteMetaPayload> event, final Site site) {
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
                .setMetadata(this.asMetadata(event))
                .setPayload(payload)
                .build();
    }

    private SiteMetaEnvelope asDeletedEnvelope(final Event<SiteMetaPayload> event, final SiteDeletedPayload deletedPayload) {
        final SiteDeletedEvent payload = SiteDeletedEvent.newBuilder()
                .setSiteId(deletedPayload.siteId().toString())
                .setOwnerUserId(deletedPayload.ownerUserId())
                .setDeletedAt(this.asIsoInstant(deletedPayload.deletedAt()))
                .build();
        return SiteMetaEnvelope.newBuilder()
                .setMetadata(this.asMetadata(event))
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

    private Metadata asMetadata(final Event<SiteMetaPayload> event) {
        return Metadata.newBuilder()
                .setIdempotencyId(event.getIdempotencyId().toString())
                .setCreatedAt(event.getCreatedAt().toEpochMilli())
                .setEventType(event.getEventType())
                .build();
    }
}
