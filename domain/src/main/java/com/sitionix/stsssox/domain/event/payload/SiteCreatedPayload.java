package com.sitionix.stsssox.domain.event.payload;

import com.sitionix.stsssox.domain.Site;
import com.sitionix.stsssox.domain.event.SiteMetaEventType;

public record SiteCreatedPayload(
        Site site
) implements SiteMetaPayload {

    public static final String EVENT_TYPE = SiteMetaEventType.SITE_CREATED.getValue();

    @Override
    public String eventType() {
        return EVENT_TYPE;
    }
}
