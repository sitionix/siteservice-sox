package com.sitionix.stsssox.domain.event.payload;

import com.sitionix.forge.outbox.core.model.OutboxAggregateType;
import com.sitionix.stsssox.domain.Site;
import com.sitionix.stsssox.domain.event.SiteMetaEventType;

public record SiteUpdatedPayload(
        Site site
) implements SiteMetaPayload {

    @Override
    public String eventType() {
        return SiteMetaEventType.SITE_UPDATED.getValue();
    }

    @Override
    public OutboxAggregateType aggregateType() {
        return OutboxAggregateType.USER;
    }

    @Override
    public Long aggregateId() {
        if (this.site == null) {
            return null;
        }
        return this.site.userId();
    }

    @Override
    public String traceId() {
        return null;
    }
}
