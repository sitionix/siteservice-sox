package com.sitionix.stsssox.mongodb.mapper;

import com.sitionix.stsssox.domain.Site;
import com.sitionix.stsssox.mongodb.entity.site.SiteEntity;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface SiteInfraMapper {

    SiteEntity asSiteEntity(Site site);

    Site asSite(SiteEntity siteEntity);
}
