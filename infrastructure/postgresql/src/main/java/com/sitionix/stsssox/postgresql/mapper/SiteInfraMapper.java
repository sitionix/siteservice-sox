package com.sitionix.stsssox.postgresql.mapper;

import com.sitionix.stsssox.domain.Site;
import com.sitionix.stsssox.postgresql.entity.site.SiteEntity;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = {SiteStatusInfraMapper.class, SiteTypeInfraMapper.class}
)
public interface SiteInfraMapper {

    SiteEntity asSiteEntity(Site site);

    Site asSite(SiteEntity siteEntity);
}
