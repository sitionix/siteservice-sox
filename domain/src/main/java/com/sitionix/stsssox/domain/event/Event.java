package com.sitionix.stsssox.domain.event;

import com.sitionix.stsssox.domain.Site;
import com.sitionix.stsssox.domain.event.payload.SiteCreatedPayload;
import com.sitionix.stsssox.domain.event.payload.SiteDeletedPayload;
import com.sitionix.stsssox.domain.event.payload.SiteMetaPayload;
import com.sitionix.stsssox.domain.event.payload.SiteUpdatedPayload;
import java.time.Instant;
import java.util.UUID;
import lombok.Getter;

@Getter
public class Event<T> {

    private final T payload;
    private final UUID idempotencyId;
    private final Instant createdAt;
    private final String eventType;
    private final String id;

    private Event(final String id,
                  final T payload,
                  final String eventType,
                  final Instant createdAt) {
        this.id = id;
        this.payload = payload;
        this.idempotencyId = UUID.randomUUID();
        this.createdAt = createdAt;
        this.eventType = eventType;
    }

    public static Event<SiteMetaPayload> siteCreated(final Site site) {
        return new Event<>(site.siteId().toString(),
                new SiteCreatedPayload(site),
                SiteMetaEventType.SITE_CREATED.getValue(),
                Instant.now());
    }

    public static Event<SiteMetaPayload> siteUpdated(final Site site) {
        return new Event<>(site.siteId().toString(),
                new SiteUpdatedPayload(site),
                SiteMetaEventType.SITE_UPDATED.getValue(),
                Instant.now());
    }

    public static Event<SiteMetaPayload> siteDeleted(final UUID siteId,
                                                     final Long userId,
                                                     final Instant deletedAt) {
        return new Event<>(siteId.toString(),
                new SiteDeletedPayload(siteId, userId, deletedAt),
                SiteMetaEventType.SITE_DELETED.getValue(),
                Instant.now());
    }
}
