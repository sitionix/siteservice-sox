package com.sitionix.stsssox.postgresql.repository;

import com.sitionix.stsssox.domain.Site;
import com.sitionix.stsssox.domain.repository.SiteRepository;
import com.sitionix.stsssox.postgresql.entity.site.SiteEntity;
import com.sitionix.stsssox.postgresql.jpa.SiteJpaRepository;
import com.sitionix.stsssox.postgresql.mapper.SiteInfraMapper;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SiteRepositoryImpl implements SiteRepository {

    private final SiteJpaRepository siteJpaRepository;

    private final SiteInfraMapper siteInfraMapper;

    @Override
    public Site save(final Site site) {
        final SiteEntity entity = this.siteJpaRepository.save(this.siteInfraMapper.asSiteEntity(site));
        return this.siteInfraMapper.asSite(entity);
    }

    @Override
    public Optional<Site> findById(final UUID siteId) {
        return this.siteJpaRepository.findById(siteId)
                .map(this.siteInfraMapper::asSite);
    }
}
