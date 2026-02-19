package com.sitionix.stsssox.pipe.sitemeta.mapper;

import com.app_afesox.stsssox.events.sitemeta.SiteCreatedEvent;
import com.sitionix.stsssox.domain.event.payload.SiteCreatedPayload;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface SiteCreatedEventMapper extends EventMapper<SiteCreatedPayload> {

    @Override
    default Class<SiteCreatedPayload> payloadType() {
        return SiteCreatedPayload.class;
    }

    @Override
    @Mapping(target = "siteId",
            expression = "java(payload.site().siteId() == null ? null : payload.site().siteId().toString())")
    @Mapping(target = "userId", source = "site.userId")
    @Mapping(target = "name", source = "site.name")
    @Mapping(target = "status", source = "site.status")
    @Mapping(target = "type", source = "site.type")
    @Mapping(target = "description", source = "site.description")
    @Mapping(target = "createdAt",
            expression = "java(payload.site().createdAt() == null ? null : payload.site().createdAt().toString())")
    @Mapping(target = "updatedAt",
            expression = "java(payload.site().updatedAt() == null ? null : payload.site().updatedAt().toString())")
    SiteCreatedEvent asPayload(SiteCreatedPayload payload);
}
