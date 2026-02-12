package com.sitionix.stsssox.domain;

import java.util.stream.Stream;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SiteType {
    PORTFOLIO(1L, "PORTFOLIO"),
    BUSINESS(2L, "BUSINESS"),
    BLOG(3L, "BLOG"),
    STORE(4L, "STORE"),
    LANDING(5L, "LANDING"),
    OTHER(6L, "OTHER");

    private final Long id;
    private final String description;

    public static SiteType fromId(final Long id) {
        return Stream.of(values())
                .filter(type -> type.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No site type found for id: " + id));
    }
}
