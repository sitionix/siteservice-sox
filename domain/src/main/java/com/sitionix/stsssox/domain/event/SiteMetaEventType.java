package com.sitionix.stsssox.domain.event;

import com.sitionix.forge.outbox.core.model.ForgeOutboxEventType;
import com.sitionix.forge.outbox.core.port.ForgeOutboxPayload;
import com.sitionix.stsssox.domain.event.payload.SiteCreatedPayload;
import com.sitionix.stsssox.domain.event.payload.SiteDeletedPayload;
import com.sitionix.stsssox.domain.event.payload.SiteUpdatedPayload;

public enum SiteMetaEventType implements ForgeOutboxEventType {
    SITE_CREATED(1L, "SITE_CREATED", SiteCreatedPayload.class),
    SITE_UPDATED(2L, "SITE_UPDATED", SiteUpdatedPayload.class),
    SITE_DELETED(3L, "SITE_DELETED", SiteDeletedPayload.class);

    private final Long id;
    private final String description;
    private final Class<? extends ForgeOutboxPayload> payloadClass;

    SiteMetaEventType(final Long id,
                      final String description,
                      final Class<? extends ForgeOutboxPayload> payloadClass) {
        this.id = id;
        this.description = description;
        this.payloadClass = payloadClass;
    }

    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public Class<? extends ForgeOutboxPayload> payloadClass() {
        return this.payloadClass;
    }

    public String getValue() {
        return this.description;
    }
}
