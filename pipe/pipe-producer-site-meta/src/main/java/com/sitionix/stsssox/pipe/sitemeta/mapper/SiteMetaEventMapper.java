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
import java.time.Instant;
import java.util.UUID;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

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

    @Mapping(target = "siteId", source = "site.siteId", qualifiedByName = "uuidToString")
    @Mapping(target = "ownerUserId", source = "site.userId")
    @Mapping(target = "name", source = "site.name")
    @Mapping(target = "status", source = "site.status")
    @Mapping(target = "type", source = "site.type")
    @Mapping(target = "description", source = "site.description")
    @Mapping(target = "createdAt", source = "site.createdAt", qualifiedByName = "instantToString")
    @Mapping(target = "updatedAt", source = "site.updatedAt", qualifiedByName = "instantToString")
    SiteCreatedEvent asPayload(SiteCreatedPayload payload);

    @Mapping(target = "siteId", source = "site.siteId", qualifiedByName = "uuidToString")
    @Mapping(target = "ownerUserId", source = "site.userId")
    @Mapping(target = "name", source = "site.name")
    @Mapping(target = "status", source = "site.status")
    @Mapping(target = "type", source = "site.type")
    @Mapping(target = "description", source = "site.description")
    @Mapping(target = "updatedAt", source = "site.updatedAt", qualifiedByName = "instantToString")
    SiteUpdatedEvent asPayload(SiteUpdatedPayload payload);

    @Mapping(target = "siteId", source = "siteId", qualifiedByName = "uuidToString")
    @Mapping(target = "deletedAt", source = "deletedAt", qualifiedByName = "instantToString")
    SiteDeletedEvent asPayload(SiteDeletedPayload payload);

    @Mapping(target = "idempotencyId", source = "idempotencyId", qualifiedByName = "uuidToString")
    @Mapping(target = "createdAt", source = "createdAt", qualifiedByName = "instantToEpochMillis")
    @Mapping(target = "eventType", source = "eventType")
    Metadata asMetadata(Event<SiteMetaPayload> event);

    @Named("instantToString")
    default String instantToString(final Instant value) {
        if (value == null) {
            return null;
        }
        return value.toString();
    }

    @Named("instantToEpochMillis")
    default Long instantToEpochMillis(final Instant value) {
        if (value == null) {
            return null;
        }
        return value.toEpochMilli();
    }

    @Named("uuidToString")
    default String uuidToString(final UUID value) {
        if (value == null) {
            return null;
        }
        return value.toString();
    }
}
