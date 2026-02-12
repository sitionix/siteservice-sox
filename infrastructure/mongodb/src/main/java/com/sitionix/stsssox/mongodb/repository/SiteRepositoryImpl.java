package com.sitionix.stsssox.mongodb.repository;

import com.sitionix.stsssox.domain.Site;
import com.sitionix.stsssox.domain.repository.SiteRepository;
import com.sitionix.stsssox.mongodb.entity.site.SiteEntity;
import com.sitionix.stsssox.mongodb.mapper.SiteInfraMapper;
import com.sitionix.stsssox.mongodb.mongo.SiteMongoRepository;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SiteRepositoryImpl implements SiteRepository {

    private final SiteMongoRepository siteMongoRepository;

    private final SiteInfraMapper siteInfraMapper;

    @Override
    public Site save(final Site site) {
        final SiteEntity entity = this.siteMongoRepository.save(this.siteInfraMapper.asSiteEntity(site));
        return this.siteInfraMapper.asSite(entity);
    }

    @Override
    public Optional<Site> findById(final UUID siteId) {
        return this.siteMongoRepository.findById(siteId)
                .map(this.siteInfraMapper::asSite);
    }
}
