package com.sitionix.stsssox.domain.model;

import com.sitionix.stsssox.domain.SiteTemplate;
import com.sitionix.stsssox.domain.SiteType;

public record CreateSiteCommand(
        String name,
        SiteType type,
        String description,
        SiteTemplate template
) {
}
