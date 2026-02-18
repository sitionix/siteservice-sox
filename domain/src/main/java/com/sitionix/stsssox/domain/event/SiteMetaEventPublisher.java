package com.sitionix.stsssox.domain.event;

import com.sitionix.stsssox.domain.Site;
import java.time.Instant;
import java.util.UUID;

/**
 * Publishes site metadata events to the projection stream.
 */
public interface SiteMetaEventPublisher {

    /**
     * Publishes site created event payload.
     *
     * @param site created site aggregate.
     */
    void publishSiteCreated(Site site);

    /**
     * Publishes site updated event payload.
     *
     * @param site updated site aggregate.
     */
    void publishSiteUpdated(Site site);

    /**
     * Publishes site deleted event payload.
     *
     * @param siteId site identifier.
     * @param ownerUserId owner identifier.
     * @param deletedAt deletion timestamp.
     */
    void publishSiteDeleted(UUID siteId, Long ownerUserId, Instant deletedAt);
}
