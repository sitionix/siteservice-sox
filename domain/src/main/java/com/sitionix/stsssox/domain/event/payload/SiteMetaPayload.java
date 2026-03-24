package com.sitionix.stsssox.domain.event.payload;

import com.sitionix.forge.outbox.core.model.OutboxAggregateType;
import com.sitionix.forge.outbox.core.port.ForgeOutboxPayload;
import com.sitionix.stsssox.domain.Site;

/**
 * Marker interface for site metadata projection payloads.
 */
public interface SiteMetaPayload extends ForgeOutboxPayload {

    @Override
    default OutboxAggregateType aggregateType() {
        return OutboxAggregateType.USER;
    }

    @Override
    default String traceId() {
        return null;
    }

    static Long resolveUserAggregateId(final Site site) {
        if (site == null) {
            return null;
        }
        return site.userId();
    }
}
