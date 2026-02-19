package com.sitionix.stsssox.pipe.sitemeta.mapper;

import com.app_afesox.events.Metadata;
import com.app_afesox.stsssox.events.sitemeta.SiteCreatedEvent;
import com.app_afesox.stsssox.events.sitemeta.SiteDeletedEvent;
import com.app_afesox.stsssox.events.sitemeta.SiteMetaEnvelope;
import com.app_afesox.stsssox.events.sitemeta.SiteUpdatedEvent;
import com.sitionix.stsssox.domain.event.Event;
import com.sitionix.stsssox.domain.event.payload.SiteCreatedPayload;
import com.sitionix.stsssox.domain.event.payload.SiteDeletedPayload;
import com.sitionix.stsssox.domain.event.payload.SiteMetaPayload;
import com.sitionix.stsssox.domain.event.payload.SiteUpdatedPayload;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface SiteMetaEventMapper {

    default SiteMetaEnvelope asEnvelope(final Event<SiteMetaPayload> event) {
        final Object payload = event.getPayload();
        if (payload instanceof SiteCreatedPayload createdPayload) {
            return this.asCreatedEnvelope(event, createdPayload);
        }
        if (payload instanceof SiteUpdatedPayload updatedPayload) {
            return this.asUpdatedEnvelope(event, updatedPayload);
        }
        if (payload instanceof SiteDeletedPayload deletedPayload) {
            return this.asDeletedEnvelope(event, deletedPayload);
        }
        throw new IllegalArgumentException("Unsupported site meta payload type: " + payload.getClass().getName());
    }

    @Mapping(target = "metadata", source = "event")
    @Mapping(target = "payload", expression = "java(this.asPayload(payload))")
    SiteMetaEnvelope asCreatedEnvelope(Event<SiteMetaPayload> event, SiteCreatedPayload payload);

    @Mapping(target = "metadata", source = "event")
    @Mapping(target = "payload", expression = "java(this.asPayload(payload))")
    SiteMetaEnvelope asUpdatedEnvelope(Event<SiteMetaPayload> event, SiteUpdatedPayload payload);

    @Mapping(target = "metadata", source = "event")
    @Mapping(target = "payload", expression = "java(this.asPayload(payload))")
    SiteMetaEnvelope asDeletedEnvelope(Event<SiteMetaPayload> event, SiteDeletedPayload payload);

    @Mapping(target = "siteId",
            expression = "java(payload.site().siteId() == null ? null : payload.site().siteId().toString())")
    @Mapping(target = "ownerUserId", source = "site.userId")
    @Mapping(target = "name", source = "site.name")
    @Mapping(target = "status", source = "site.status")
    @Mapping(target = "type", source = "site.type")
    @Mapping(target = "description", source = "site.description")
    @Mapping(target = "createdAt",
            expression = "java(payload.site().createdAt() == null ? null : payload.site().createdAt().toString())")
    @Mapping(target = "updatedAt",
            expression = "java(payload.site().updatedAt() == null ? null : payload.site().updatedAt().toString())")
    SiteCreatedEvent asPayload(SiteCreatedPayload payload);

    @Mapping(target = "siteId",
            expression = "java(payload.site().siteId() == null ? null : payload.site().siteId().toString())")
    @Mapping(target = "ownerUserId", source = "site.userId")
    @Mapping(target = "name", source = "site.name")
    @Mapping(target = "status", source = "site.status")
    @Mapping(target = "type", source = "site.type")
    @Mapping(target = "description", source = "site.description")
    @Mapping(target = "updatedAt",
            expression = "java(payload.site().updatedAt() == null ? null : payload.site().updatedAt().toString())")
    SiteUpdatedEvent asPayload(SiteUpdatedPayload payload);

    @Mapping(target = "siteId", expression = "java(payload.siteId() == null ? null : payload.siteId().toString())")
    @Mapping(target = "deletedAt", expression = "java(payload.deletedAt() == null ? null : payload.deletedAt().toString())")
    SiteDeletedEvent asPayload(SiteDeletedPayload payload);

    @Mapping(target = "idempotencyId",
            expression = "java(event.getIdempotencyId() == null ? null : event.getIdempotencyId().toString())")
    @Mapping(target = "createdAt",
            expression = "java(event.getCreatedAt() == null ? null : event.getCreatedAt().toEpochMilli())")
    @Mapping(target = "eventType", source = "eventType")
    Metadata asMetadata(Event<SiteMetaPayload> event);
}
