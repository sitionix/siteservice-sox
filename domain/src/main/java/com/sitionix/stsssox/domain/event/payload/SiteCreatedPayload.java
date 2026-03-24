package com.sitionix.stsssox.domain.event.payload;

import com.sitionix.stsssox.domain.Site;

public record SiteCreatedPayload(
        Site site
) implements SiteMetaPayload {
}
