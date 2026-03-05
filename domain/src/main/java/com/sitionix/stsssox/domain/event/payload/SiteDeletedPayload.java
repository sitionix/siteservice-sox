package com.sitionix.stsssox.domain.event.payload;

import com.sitionix.stsssox.domain.event.SiteMetaEventType;
import java.time.Instant;
import java.util.UUID;

public record SiteDeletedPayload(
        UUID siteId,
        Long userId,
        Instant deletedAt
) implements SiteMetaPayload {

    @Override
    public String eventType() {
        return SiteMetaEventType.SITE_DELETED.getValue();
    }

    @Override
    public Long aggregateId() {
        return this.userId;
    }
}
