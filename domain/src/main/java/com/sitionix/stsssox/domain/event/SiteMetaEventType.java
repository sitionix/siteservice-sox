package com.sitionix.stsssox.domain.event;

public enum SiteMetaEventType {
    SITE_CREATED("SITE_CREATED"),
    SITE_UPDATED("SITE_UPDATED"),
    SITE_DELETED("SITE_DELETED");

    private final String value;

    SiteMetaEventType(final String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
