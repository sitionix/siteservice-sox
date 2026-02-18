package com.sitionix.stsssox.domain.event.payload;

import java.time.Instant;
import java.util.UUID;

public record SiteDeletedPayload(
        UUID siteId,
        Long ownerUserId,
        Instant deletedAt
) implements SiteMetaPayload {
}
