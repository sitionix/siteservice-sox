package com.sitionix.stsssox.pipe.sitemeta.mapper;

import com.app_afesox.stsssox.events.sitemeta.SiteDeletedEvent;
import com.sitionix.stsssox.domain.event.payload.SiteDeletedPayload;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface SiteDeletedEventMapper extends EventMapper<SiteDeletedPayload> {

    @Override
    default Class<SiteDeletedPayload> payloadType() {
        return SiteDeletedPayload.class;
    }

    @Override
    @Mapping(target = "siteId", expression = "java(payload.siteId() == null ? null : payload.siteId().toString())")
    @Mapping(target = "deletedAt", expression = "java(payload.deletedAt() == null ? null : payload.deletedAt().toString())")
    SiteDeletedEvent asPayload(SiteDeletedPayload payload);
}
