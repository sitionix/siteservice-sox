package com.sitionix.stsssox.domain.repository;

import com.sitionix.stsssox.domain.Site;
import java.util.Optional;
import java.util.UUID;

/**
 * Persistence contract for site aggregate operations.
 */
public interface SiteRepository {

    /**
     * Stores a site record.
     *
     * @param site site to persist.
     * @return persisted site state.
     */
    Site save(Site site);

    /**
     * Finds a site by identifier.
     *
     * @param siteId unique site identifier.
     * @return site if found.
     */
    Optional<Site> findById(UUID siteId);
}
