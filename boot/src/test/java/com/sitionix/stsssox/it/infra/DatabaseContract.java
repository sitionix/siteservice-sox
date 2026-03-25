package com.sitionix.stsssox.it.infra;

import com.sitionix.forgeit.core.contract.ForgeDbContracts;
import com.sitionix.forgeit.domain.contract.DbContract;
import com.sitionix.forgeit.domain.contract.DbContractsDsl;
import com.sitionix.forgeit.domain.contract.clean.CleanupPolicy;
import com.sitionix.stsssox.postgresql.entity.site.SiteEntity;

@ForgeDbContracts
public class DatabaseContract {

    public static final DbContract<SiteEntity> SITE_ENTITY_DB_CONTRACT = DbContractsDsl.entity(SiteEntity.class)
            .cleanupPolicy(CleanupPolicy.DELETE_ALL)
            .build();

    private DatabaseContract() {
    }
}
