package com.sitionix.stsssox.domain.usecase;

import com.sitionix.stsssox.domain.Site;
import com.sitionix.stsssox.domain.model.CreateSiteCommand;

/**
 * Use case for creating a new site.
 */
public interface CreateSite {

    /**
     * Creates and stores a new site in draft state.
     *
     * @param command create request data.
     * @return created site.
     */
    Site execute(CreateSiteCommand command);
}
