package com.sitionix.stsssox.domain;

import java.util.stream.Stream;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SiteStatus {
    DRAFT(1L, "DRAFT"),
    PUBLISHED(2L, "PUBLISHED"),
    ARCHIVED(3L, "ARCHIVED");

    private final Long id;
    private final String description;

    public static SiteStatus fromId(final Long id) {
        return Stream.of(values())
                .filter(status -> status.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No site status found for id: " + id));
    }
}
