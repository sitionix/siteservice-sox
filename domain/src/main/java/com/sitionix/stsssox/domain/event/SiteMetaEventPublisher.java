package com.sitionix.stsssox.domain.event;

import com.sitionix.stsssox.domain.event.payload.SiteMetaPayload;

/**
 * Contract for publishing site metadata projection events.
 */
public interface SiteMetaEventPublisher extends EventHandler<SiteMetaPayload> {
}
