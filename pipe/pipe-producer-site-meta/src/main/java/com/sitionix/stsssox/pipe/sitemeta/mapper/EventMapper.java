package com.sitionix.stsssox.pipe.sitemeta.mapper;

import com.sitionix.stsssox.domain.event.payload.SiteMetaPayload;

public interface EventMapper<T extends SiteMetaPayload> {

    Class<T> payloadType();

    Object asPayload(T payload);

    default boolean supports(final SiteMetaPayload payload) {
        return payload != null && this.payloadType().isInstance(payload);
    }
}
