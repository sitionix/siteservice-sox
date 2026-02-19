package com.sitionix.stsssox.pipe.sitemeta.mapper;

import com.app_afesox.stsssox.events.sitemeta.SiteUpdatedEvent;
import com.sitionix.stsssox.domain.event.payload.SiteUpdatedPayload;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface SiteUpdatedEventMapper extends EventMapper<SiteUpdatedPayload> {

    @Override
    default Class<SiteUpdatedPayload> payloadType() {
        return SiteUpdatedPayload.class;
    }

    @Override
    @Mapping(target = "siteId",
            expression = "java(payload.site().siteId() == null ? null : payload.site().siteId().toString())")
    @Mapping(target = "ownerUserId", source = "site.userId")
    @Mapping(target = "name", source = "site.name")
    @Mapping(target = "status", source = "site.status")
    @Mapping(target = "type", source = "site.type")
    @Mapping(target = "description", source = "site.description")
    @Mapping(target = "updatedAt",
            expression = "java(payload.site().updatedAt() == null ? null : payload.site().updatedAt().toString())")
    SiteUpdatedEvent asPayload(SiteUpdatedPayload payload);
}
