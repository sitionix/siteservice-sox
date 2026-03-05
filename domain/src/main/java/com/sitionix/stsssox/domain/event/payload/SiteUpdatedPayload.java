package com.sitionix.stsssox.domain.event.payload;

import com.sitionix.stsssox.domain.Site;
import com.sitionix.stsssox.domain.event.SiteMetaEventType;

public record SiteUpdatedPayload(
        Site site
) implements SiteMetaPayload {

    public static final String EVENT_TYPE = SiteMetaEventType.SITE_UPDATED.getValue();

    @Override
    public String eventType() {
        return EVENT_TYPE;
    }
}
