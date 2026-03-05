package com.sitionix.stsssox.domain.event.payload;

import com.sitionix.stsssox.domain.Site;
import com.sitionix.stsssox.domain.event.SiteMetaEventType;

public record SiteCreatedPayload(
        Site site
) implements SiteMetaPayload {

    @Override
    public String eventType() {
        return SiteMetaEventType.SITE_CREATED.getValue();
    }

    @Override
    public Long aggregateId() {
        return SiteMetaPayload.resolveUserAggregateId(this.site);
    }
}
