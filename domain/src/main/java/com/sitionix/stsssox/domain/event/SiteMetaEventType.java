package com.sitionix.stsssox.domain.event;

import com.sitionix.stsssox.domain.event.payload.SiteCreatedPayload;
import com.sitionix.stsssox.domain.event.payload.SiteDeletedPayload;
import com.sitionix.stsssox.domain.event.payload.SiteUpdatedPayload;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SiteMetaEventType {
    SITE_CREATED(1L, "SITE_CREATED"),
    SITE_UPDATED(2L, "SITE_UPDATED"),
    SITE_DELETED(3L, "SITE_DELETED");

    private final Long id;
    private final String description;

    public String getValue() {
        return this.description;
    }
}
