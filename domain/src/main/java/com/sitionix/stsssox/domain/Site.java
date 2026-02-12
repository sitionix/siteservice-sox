package com.sitionix.stsssox.domain;

import java.time.Instant;
import java.util.UUID;
import lombok.Builder;

@Builder
public record Site(
        UUID siteId,
        Long userId,
        String name,
        SiteStatus status,
        Instant createdAt,
        Instant updatedAt,
        SiteType type,
        String description
) {
}
