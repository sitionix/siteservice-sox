package com.sitionix.stsssox.domain.event.payload;

import com.sitionix.forge.outbox.core.model.OutboxAggregateType;
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
    public OutboxAggregateType aggregateType() {
        return OutboxAggregateType.USER;
    }

    @Override
    public Long aggregateId() {
        return this.userId;
    }

    @Override
    public String traceId() {
        if (this.siteId == null) {
            return null;
        }
        return this.siteId.toString();
    }
}
