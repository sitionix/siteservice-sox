package com.sitionix.stsssox.domain.event;

import com.sitionix.forge.outbox.core.model.ForgeOutboxEventType;
import com.sitionix.forge.outbox.core.port.ForgeOutboxPayload;
import com.sitionix.stsssox.domain.event.payload.SiteCreatedPayload;
import com.sitionix.stsssox.domain.event.payload.SiteDeletedPayload;
import com.sitionix.stsssox.domain.event.payload.SiteUpdatedPayload;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum SiteMetaEventType implements ForgeOutboxEventType {
    SITE_CREATED(1L, "SITE_CREATED", SiteCreatedPayload.class),
    SITE_UPDATED(2L, "SITE_UPDATED", SiteUpdatedPayload.class),
    SITE_DELETED(3L, "SITE_DELETED", SiteDeletedPayload.class);

    @Getter
    private final Long id;

    @Getter
    private final String description;

    private final Class<? extends ForgeOutboxPayload> payloadClass;

    @Override
    public Class<? extends ForgeOutboxPayload> payloadClass() {
        return this.payloadClass;
    }

    public String getValue() {
        return this.description;
    }
}
