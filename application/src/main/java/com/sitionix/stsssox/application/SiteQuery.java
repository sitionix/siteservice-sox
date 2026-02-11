package com.sitionix.stsssox.application;

import com.sitionix.stsssox.domain.Site;
import java.util.Optional;
import java.util.UUID;

public interface SiteQuery {
    Optional<Site> findById(UUID id);
}
