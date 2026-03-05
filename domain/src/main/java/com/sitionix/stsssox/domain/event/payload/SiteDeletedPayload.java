package com.sitionix.stsssox.domain.event.payload;

import com.sitionix.stsssox.domain.event.SiteMetaEventType;
import java.time.Instant;
import java.util.UUID;

public record SiteDeletedPayload(
        UUID siteId,
        Long userId,
        Instant deletedAt
) implements SiteMetaPayload {

    public static final String EVENT_TYPE = SiteMetaEventType.SITE_DELETED.getValue();

    @Override
    public String eventType() {
        return EVENT_TYPE;
    }
}
